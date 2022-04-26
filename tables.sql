BEGIN;
DROP TABLE IF EXISTS CLIENTS_RETURN      CASCADE;
DROP TABLE IF EXISTS PRODUCTS_SOLD       CASCADE; --delete before PRODUKTY (COMMENT)
DROP TABLE IF EXISTS SALES               CASCADE; --delete after PRODUKTY_SPRZEDAZ
DROP TABLE IF EXISTS PRODUCTS_DELIVERIES CASCADE;
DROP TABLE IF EXISTS DELIVERIES          CASCADE;
DROP TABLE IF EXISTS STORE_STATUS        CASCADE; --delete before PRODUKTY
DROP TABLE IF EXISTS SUPPLIERS           CASCADE;
DROP TABLE IF EXISTS PRICE_HISTORY       CASCADE; --delete before PRODUKTY
DROP TABLE IF EXISTS PARAMETER_PRODUCTS  CASCADE;
DROP TABLE IF EXISTS REQUIRED_PARAMETERS CASCADE;
DROP TABLE IF EXISTS PRODUCTS            CASCADE; --(COMMENT) 
DROP TABLE IF EXISTS PARAMETERS          CASCADE;
DROP TABLE IF EXISTS CATEGORIES          CASCADE;
DROP TABLE IF EXISTS BRAND               CASCADE; --delete after PRODUKTY

---------------------------------------------------------------

CREATE TABLE BRAND (
    id_brand NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT UNIQUE
);

CREATE TABLE CATEGORIES(
    id_category NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    vat NUMERIC(10) NOT NULL
);

CREATE TABLE PARAMETERS(
    id_parameter NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit VARCHAR(100) NOT NULL
);

CREATE TABLE PRODUCTS (
    id NUMERIC(10) PRIMARY KEY,
    id_category NUMERIC(10) REFERENCES CATEGORIES(id),
    name VARCHAR(100) NOT NULL,
    id_brand NUMERIC(10) REFERENCES BRAND(id_brand)
);

CREATE TABLE REQUIRED_PARAMETERS (
    id_category NUMERIC(10) NOT NULL REFERENCES CATEGORIES(id_category),
    id_parameter NUMERIC(10) NOT NULL REFERENCES PARAMETERS(id_parameter)
);

CREATE TABLE PARAMETER_PRODUCTS(
    id_parameter NUMERIC(10) NOT NULL,
    id_product NUMERIC(10) NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL
);

CREATE TABLE PRICE_HISTORY (
    id_product NUMERIC(10) REFERENCES PRODUCTS(id), --check if types agree
    launch_date DATE,
    net_price NUMERIC(8,2)
);

CREATE TABLE SUPPLIERS (
    id_supplier NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE STORE_STATUS (
    id_product NUMERIC(10) REFERENCES PRODUCTS(id) UNIQUE, --check if types agree
    quantity NUMERIC(10) CHECK (quantity >= 0)
);

CREATE TABLE DELIVERIES (
    id_delivery NUMERIC(10) PRIMARY KEY,
    id_supplier NUMERIC(10) REFERENCES DOSTAWCY(id_supplier),
    date_delivery DATE
);

CREATE TABLE PRODUCTS_DELIVERIES (
    id_delivery NUMERIC(10) REFERENCES DELIVERIES(id_delivery),
    id_product NUMERIC(10) REFERENCES PRODUCTS(id),
    CONSTRAINT pd_key UNIQUE (id_delivery, id_product),
    quantity NUMERIC(10) CHECK (quantity > 0)
);

CREATE TABLE SALES (
    id_sale NUMERIC(10) PRIMARY KEY,
    "date" DATE
);

CREATE TABLE PRODUCTS_SOLD (
    id_sale NUMERIC(10) REFERENCES SALES(id_sale),
    id_product NUMERIC(10) REFERENCES PRODUCTS(id),
    CONSTRAINT ps_key UNIQUE (id_sale, id_product),
    quantity NUMERIC(10) CHECK (quantity > 0)
);

CREATE TABLE CLIENTS_RETURN (
    id_return NUMERIC(10) PRIMARY KEY,
    id_sale NUMERIC(10) REFERENCES SALES(id_sale),
    id_product NUMERIC(10) REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) CHECK (quantity > 0),
    "date" DATE
);

---------------------------------------------------------------

CREATE OR REPLACE FUNCTION returned_amount_check(NUMERIC, NUMERIC)
    RETURNS NUMERIC(10) AS
$$
BEGIN
    RETURN (
        SELECT COALESCE(SUM(quantity), 0)
        FROM PRODUCTS_SOLD
        WHERE id_sale = $1 AND id_product = $2
    )-(
        SELECT COALESCE(SUM(quantity), 0)
        FROM CLIENTS_RETURN
        WHERE id_sale = $1 AND id_product = $2
    );
END;
$$ LANGUAGE 'plpgsql';

--additional constraint to control zwroty ilosc
ALTER TABLE CLIENTS_RETURN
ADD CONSTRAINT return_check CHECK(returned_amount_check(id_sale, id_product) >= 0);

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

