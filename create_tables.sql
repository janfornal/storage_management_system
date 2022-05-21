BEGIN;

CREATE TABLE BRAND (
    id_brand INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE CATEGORIES (
    id_category INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(100) NOT NULL UNIQUE,
    vat NUMERIC(10) NOT NULL CHECK (vat >= 0)
);

CREATE TABLE PARAMETERS (
    id_parameter INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit VARCHAR(100) -- unit can be NULL
);

CREATE TABLE PRODUCTS (
    id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_category INTEGER NOT NULL REFERENCES CATEGORIES(id_category),
    name VARCHAR(100) NOT NULL,
    id_brand INTEGER NOT NULL REFERENCES BRAND(id_brand) 
);

CREATE TABLE POSSIBLE_PARAMETERS (
    id_category INTEGER NOT NULL REFERENCES CATEGORIES(id_category),
    id_parameter INTEGER NOT NULL REFERENCES PARAMETERS(id_parameter),
    CONSTRAINT poss_param_key UNIQUE (id_category, id_parameter)
);

CREATE TABLE PARAMETER_PRODUCTS (
    id_parameter INTEGER NOT NULL REFERENCES PARAMETERS(id_parameter),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    quantity VARCHAR(100) NOT NULL, -- this should be VARCHAR ?
    CONSTRAINT param_pr_key UNIQUE (id_parameter, id_product) -- additional checks via trigger
);

CREATE TABLE PRICE_HISTORY (
    id_product INTEGER REFERENCES PRODUCTS(id),
    launch_date DATE NOT NULL,
    CONSTRAINT ph_key UNIQUE (id_product, launch_date),
    net_price NUMERIC(8,2) NOT NULL 
);

CREATE TABLE SUPPLIERS (
    id_supplier NUMERIC(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE STORE_STATUS (
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id) UNIQUE,
    quantity NUMERIC(10) NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE DELIVERIES (
    id_delivery NUMERIC(10) PRIMARY KEY,
    id_supplier NUMERIC(10) NOT NULL REFERENCES SUPPLIERS(id_supplier),
    date_delivery DATE NOT NULL 
);

CREATE TABLE PRODUCTS_DELIVERIES (
    id_delivery NUMERIC(10) NOT NULL REFERENCES DELIVERIES(id_delivery),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT pd_key UNIQUE (id_delivery, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE SALES (
    id_sale NUMERIC(10) PRIMARY KEY,
    sales_date DATE NOT NULL 
);

CREATE TABLE PRODUCTS_SOLD (
    id_sale NUMERIC(10) NOT NULL REFERENCES SALES(id_sale),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT ps_key UNIQUE (id_sale, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE CLIENTS_RETURN (
    id_return NUMERIC(10) PRIMARY KEY,
    id_sale NUMERIC(10) NOT NULL REFERENCES SALES(id_sale),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0), 
    return_date DATE NOT NULL -- additional checks via trigger
);

COMMIT;
