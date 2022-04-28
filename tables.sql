BEGIN;
DROP TABLE IF EXISTS CLIENTS_RETURN      CASCADE;
DROP TABLE IF EXISTS PRODUCTS_SOLD       CASCADE;
DROP TABLE IF EXISTS SALES               CASCADE;
DROP TABLE IF EXISTS PRODUCTS_DELIVERIES CASCADE;
DROP TABLE IF EXISTS DELIVERIES          CASCADE;
DROP TABLE IF EXISTS STORE_STATUS        CASCADE;
DROP TABLE IF EXISTS SUPPLIERS           CASCADE;
DROP TABLE IF EXISTS PRICE_HISTORY       CASCADE;
DROP TABLE IF EXISTS PARAMETER_PRODUCTS  CASCADE;
DROP TABLE IF EXISTS POSSIBLE_PARAMETERS CASCADE;
DROP TABLE IF EXISTS PRODUCTS            CASCADE;
DROP TABLE IF EXISTS PARAMETERS          CASCADE;
DROP TABLE IF EXISTS CATEGORIES          CASCADE;
DROP TABLE IF EXISTS BRAND               CASCADE;

---------------------------------------------------------------

CREATE TABLE BRAND (
    id_brand NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE CATEGORIES (
    id_category NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    vat NUMERIC(10) NOT NULL CHECK (vat >= 0)
);

CREATE TABLE PARAMETERS (
    id_parameter NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit VARCHAR(100) -- unit can be NULL
);

CREATE TABLE PRODUCTS (
    id NUMERIC(10) PRIMARY KEY,
    id_category NUMERIC(10) NOT NULL REFERENCES CATEGORIES(id_category),
    name VARCHAR(100) NOT NULL,
    id_brand NUMERIC(10) NOT NULL REFERENCES BRAND(id_brand) 
);

CREATE TABLE POSSIBLE_PARAMETERS (
    id_category NUMERIC(10) NOT NULL REFERENCES CATEGORIES(id_category),
    id_parameter NUMERIC(10) NOT NULL REFERENCES PARAMETERS(id_parameter),
    CONSTRAINT poss_param_key UNIQUE (id_category, id_parameter)
);

CREATE TABLE PARAMETER_PRODUCTS (
    id_parameter NUMERIC(10) NOT NULL REFERENCES PARAMETERS(id_parameter),
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL, -- this should be VARCHAR ?
    CONSTRAINT param_pr_key UNIQUE (id_parameter, id_product) -- additional checks via trigger
);

CREATE TABLE PRICE_HISTORY (
    id_product NUMERIC(10) REFERENCES PRODUCTS(id),
    launch_date DATE NOT NULL,
    CONSTRAINT ph_key UNIQUE (id_product, launch_date),
    net_price NUMERIC(8,2) NOT NULL 
);

CREATE TABLE SUPPLIERS (
    id_supplier NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE STORE_STATUS (
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id) UNIQUE,
    quantity NUMERIC(10) NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE DELIVERIES (
    id_delivery NUMERIC(10) PRIMARY KEY,
    id_supplier NUMERIC(10) NOT NULL REFERENCES SUPPLIERS(id_supplier),
    date_delivery DATE NOT NULL 
);

CREATE TABLE PRODUCTS_DELIVERIES (
    id_delivery NUMERIC(10) NOT NULL REFERENCES DELIVERIES(id_delivery),
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT pd_key UNIQUE (id_delivery, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE SALES (
    id_sale NUMERIC(10) PRIMARY KEY,
    sales_date DATE NOT NULL 
);

CREATE TABLE PRODUCTS_SOLD (
    id_sale NUMERIC(10) NOT NULL REFERENCES SALES(id_sale),
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT ps_key UNIQUE (id_sale, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE CLIENTS_RETURN (
    id_return NUMERIC(10) PRIMARY KEY,
    id_sale NUMERIC(10) NOT NULL REFERENCES SALES(id_sale),
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0), 
    return_date DATE NOT NULL -- additional checks via trigger
);

---------------------------------------------------------------

CREATE OR REPLACE FUNCTION returned_check()
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

CREATE OR REPLACE FUNCTION allowed_param_check()
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

CREATE OR REPLACE FUNCTION update_decrease_store_status()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity - NEW.quantity
    WHERE id_product = NEW.id_product;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_increase_store_status()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STORE_STATUS
    SET quantity = quantity + NEW.quantity
    WHERE id_product = NEW.id_product;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--trigger below disables putting new record in PRODUKTY_SPRZEDAZ where ilosc > ilosc in 
--respective record in stan_magazynu, also changes respective ilosc value
CREATE TRIGGER trigger_ilosc_ps  
AFTER INSERT
ON PRODUCTS_SOLD
FOR EACH ROW
EXECUTE PROCEDURE update_decrease_store_status();

--updates record in stan_magazynu, after inserting into PRODUKTY_DOSTAWY
CREATE TRIGGER trigger_ilosc_pd  
AFTER INSERT
ON PRODUCTS_DELIVERIES
FOR EACH ROW
EXECUTE PROCEDURE update_increase_store_status();

---------------------------------------------------------------

CREATE OR REPLACE FUNCTION insert_id_products_to_store_status()
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

