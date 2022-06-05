BEGIN;

CREATE FUNCTION returned_check()
    RETURNS trigger AS
$$
BEGIN
    IF (
        SELECT SUM(quantity)
        FROM PRODUCTS_SOLD
        WHERE id_sale = NEW.id_sale AND id_product = NEW.id_product
    )-(
        SELECT SUM(quantity)
        FROM CLIENTS_RETURN
        WHERE id_sale = NEW.id_sale AND id_product = NEW.id_product
    ) < 0 THEN
        RAISE EXCEPTION 'returned too many products';
    END IF;

    IF (
        SELECT sales_date 
        FROM SALES
        WHERE id_sale = NEW.id_sale
    ) > NEW.return_date THEN 
        RAISE EXCEPTION 'returned before sale';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER trigger_returned_check
AFTER INSERT
ON CLIENTS_RETURN
FOR EACH ROW
EXECUTE PROCEDURE returned_check();

---------------------------------------------------------------

CREATE FUNCTION allowed_param_check()
    RETURNS trigger AS
$$
BEGIN
    IF (
        SELECT COUNT(*)
        FROM POSSIBLE_PARAMETERS
        JOIN PRODUCTS 
        ON PRODUCTS.id = NEW.id_product AND POSSIBLE_PARAMETERS.id_category = PRODUCTS.id_category
    ) = 0 THEN
        RAISE EXCEPTION 'this parameter is not allowed';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER trigger_returned_check
AFTER INSERT
ON PARAMETER_PRODUCTS
FOR EACH ROW
EXECUTE PROCEDURE allowed_param_check();

---------------------------------------------------------------

CREATE FUNCTION check_insert_products_sold()
    RETURNS trigger AS
$$
DECLARE
    val NUMERIC;
BEGIN
    SELECT quantity FROM store_status WHERE id_product = NEW.id_product INTO val;

    IF val < NEW.quantity THEN
        RETURN NULL;
    END IF;

    UPDATE STORE_STATUS
    SET quantity = quantity - NEW.quantity
    WHERE id_product = NEW.id_product;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--trigger below disables putting new record in PRODUKTY_SPRZEDAZ where ilosc > ilosc in
--respective record in stan_magazynu, also changes respective ilosc value
CREATE TRIGGER trigger_check_insert_products_sold
    BEFORE INSERT
    ON PRODUCTS_SOLD
    FOR EACH ROW
EXECUTE PROCEDURE check_insert_products_sold();

---------------------------------------------------------------

CREATE RULE check_mod_products_sold AS ON UPDATE TO PRODUCTS_SOLD DO INSTEAD NOTHING;

CREATE FUNCTION check_delete_products_sold()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity + OLD.quantity
    WHERE id_product = OLD.id_product;
    RETURN OLD;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER check_delete_products_sold
    BEFORE DELETE
    ON PRODUCTS_SOLD
    FOR EACH ROW
EXECUTE PROCEDURE check_delete_products_sold();

---------------------------------------------------------------

--updates record in STORE_STATUS, after inserting into PRODUCTS_DELIVERIES
CREATE FUNCTION update_increase_store_status()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity + NEW.quantity
    WHERE id_product = NEW.id_product;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER trigger_ilosc_pd
AFTER INSERT
ON PRODUCTS_DELIVERIES
FOR EACH ROW
EXECUTE PROCEDURE update_increase_store_status();

---------------------------------------------------------------

CREATE FUNCTION insert_id_products_to_store_status()
    RETURNS trigger AS
$$
BEGIN
    INSERT INTO STORE_STATUS (id_product, quantity) VALUES (NEW.id, 0);
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--name of trigger is long, but self-explanatory
CREATE TRIGGER trigger_add_id_to_store_status  
AFTER INSERT
ON PRODUCTS
FOR EACH ROW
EXECUTE PROCEDURE insert_id_products_to_store_status();

---------------------------------------------

CREATE FUNCTION check_insert_products_problems_sold()
    RETURNS trigger AS
$$
DECLARE
    val NUMERIC;
BEGIN
    SELECT quantity FROM products_problems WHERE id_product_with_problem = NEW.id_product_with_problem INTO val;

    IF val < NEW.quantity THEN
        RETURN NULL;
    END IF;

    UPDATE products_problems
    SET quantity = quantity - NEW.quantity
    WHERE id_product_with_problem = NEW.id_product_with_problem;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--trigger below disables putting new record in PRODUKTY_SPRZEDAZ where ilosc > ilosc in
--respective record in stan_magazynu, also changes respective ilosc value
CREATE TRIGGER trigger_check_insert_products_problems_sold
    BEFORE INSERT
    ON PRODUCTS_PROBLEMS_SOLD
    FOR EACH ROW
EXECUTE PROCEDURE check_insert_products_problems_sold();

---------------------------------------------------------------

CREATE FUNCTION check_delete_products_problems_sold()
    RETURNS trigger AS
$$
BEGIN
    UPDATE products_problems
    SET quantity = quantity + OLD.quantity
    WHERE id_product_with_problem = OLD.id_product_with_problem;
    RETURN OLD;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER check_delete_products_problems_sold
    BEFORE DELETE
    ON PRODUCTS_PROBLEMS_SOLD
    FOR EACH ROW
EXECUTE PROCEDURE check_delete_products_problems_sold();

---------------------------------------------------------------

COMMIT;
