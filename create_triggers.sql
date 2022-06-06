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

    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER trigger_returned_check
AFTER INSERT
ON CLIENTS_RETURN
FOR EACH ROW
EXECUTE PROCEDURE returned_check();

CREATE TRIGGER trigger_complaints_check
AFTER INSERT
ON COMPLAINT
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

CREATE FUNCTION before_insert_on_clients_return()
    RETURNS trigger AS
$$
BEGIN
    IF NEW.return_date < (SELECT sales_date FROM SALES WHERE id_sale = NEW.id_sale) THEN
        RETURN NULL;
    END IF;

    INSERT INTO
    PRODUCTS_PROBLEMS (id_product, quantity, exhibition, returned, problem_description, discount)
    VALUES (NEW.id_product, NEW.quantity, FALSE, TRUE, 'produkt zwrócony po zakupie', 10);

    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER before_insert_on_clients_return
BEFORE INSERT
ON CLIENTS_RETURN
FOR EACH ROW
EXECUTE PROCEDURE before_insert_on_clients_return();

---------------------------------------------------------------

CREATE FUNCTION before_update_or_insert_on_complaint()
    RETURNS trigger AS
$$
BEGIN
    IF NEW.complaint_date < (SELECT sales_date FROM SALES WHERE id_sale = NEW.id_sale) THEN
        RETURN OLD;
    END IF;

    IF OLD IS NOT NULL AND OLD.result_date IS NOT NULL THEN
        RETURN OLD;
    END IF;

    IF NEW.result_date IS NULL AND
       NEW.complaint_accepted IS NULL AND
       NEW.id_employee IS NULL THEN
        RETURN NEW;
    END IF;

    IF NEW.complaint_description IS NOT NULL AND
       NEW.result_date IS NOT NULL AND
       NEW.complaint_accepted IS NOT NULL AND
       NEW.id_employee IS NOT NULL THEN
        IF NEW.result_date < NEW.complaint_date THEN
            RETURN OLD;
        END IF;

        IF NEW.complaint_accepted THEN
            INSERT INTO
            PRODUCTS_PROBLEMS (id_product, quantity, exhibition, returned, problem_description, discount)
            VALUES (NEW.id_product, NEW.quantity, FALSE, FALSE, NEW.complaint_description, 10);
        END IF;

        RETURN NEW;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER before_update_or_insert_on_complaint
BEFORE INSERT OR UPDATE
ON COMPLAINT
FOR EACH ROW
EXECUTE PROCEDURE before_update_or_insert_on_complaint();

---------------------------------------------------------------

COMMIT;
