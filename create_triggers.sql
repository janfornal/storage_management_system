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

CREATE FUNCTION update_decrease_store_status()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity - NEW.quantity
    WHERE id_product = NEW.id_product;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

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

CREATE FUNCTION update_increase_store_status_2()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity + OLD.quantity
    WHERE id_product = OLD.id_product;
    RETURN OLD;
END;
$$ LANGUAGE 'plpgsql';

--trigger below disables putting new record in PRODUKTY_SPRZEDAZ where ilosc > ilosc in
--respective record in stan_magazynu, also changes respective ilosc value
CREATE TRIGGER trigger_ilosc_ps  
AFTER INSERT
ON PRODUCTS_SOLD
FOR EACH ROW
EXECUTE PROCEDURE update_decrease_store_status();

CREATE TRIGGER trigger_ilosc_ps_2
    BEFORE DELETE
    ON PRODUCTS_SOLD
    FOR EACH ROW
EXECUTE PROCEDURE update_increase_store_status_2();
--updates record in stan_magazynu, after inserting into PRODUKTY_DOSTAWY
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

COMMIT;
