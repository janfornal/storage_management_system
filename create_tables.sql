BEGIN;

CREATE TABLE BRAND (
    id_brand INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE CATEGORIES (
    id_category INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    vat INTEGER NOT NULL CHECK (vat >= 0)
);

CREATE TABLE PARAMETERS (
    id_parameter INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit VARCHAR(100) -- unit can be NULL
);

CREATE TABLE PRODUCTS (
    id INTEGER PRIMARY KEY,
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
    quantity NUMERIC(10) NOT NULL, -- this should be VARCHAR ?
    CONSTRAINT param_pr_key UNIQUE (id_parameter, id_product) -- additional checks via trigger
);

CREATE TABLE PRICE_HISTORY (
    id_product INTEGER REFERENCES PRODUCTS(id),
    launch_date DATE NOT NULL,
    CONSTRAINT ph_key UNIQUE (id_product, launch_date),
    net_price NUMERIC(8,2) NOT NULL 
);

CREATE TABLE SUPPLIERS (
    id_supplier INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE STORE_STATUS (
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id) UNIQUE,
    quantity NUMERIC(10) NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE DELIVERIES (
    id_delivery INTEGER PRIMARY KEY,
    id_supplier INTEGER NOT NULL REFERENCES SUPPLIERS(id_supplier),
    date_delivery DATE NOT NULL 
);

CREATE TABLE PRODUCTS_DELIVERIES (
    id_delivery INTEGER NOT NULL REFERENCES DELIVERIES(id_delivery),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT pd_key UNIQUE (id_delivery, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE SALES (
    id_sale INTEGER PRIMARY KEY,
    sales_date DATE NOT NULL 
);

CREATE TABLE PRODUCTS_SOLD (
    id_sale INTEGER NOT NULL REFERENCES SALES(id_sale),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    CONSTRAINT ps_key UNIQUE (id_sale, id_product),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0)
);

CREATE TABLE CLIENTS_RETURN (
    id_return INTEGER PRIMARY KEY,
    id_sale INTEGER NOT NULL REFERENCES SALES(id_sale),
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0), 
    return_date DATE NOT NULL -- additional checks via trigger
);

CREATE TABLE EMPLOYEES (
    id_employee INTEGER PRIMARY KEY,
    "login" VARCHAR(16) NOT NULL,
    "password" INTEGER NOT NULL, --remember to change to hex or varchar
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL

);

CREATE TABLE ADDRESSES (
    id_address INTEGER PRIMARY KEY,
    postal_code VARCHAR(8) NOT NULL,
    city VARCHAR(20) NOT NULL,
    street VARCHAR(20),
    building_number INTEGER NOT NULL,
    flat_number INTEGER

);

CREATE TABLE CLIENTS (
    id_client INTEGER PRIMARY KEY,
    "login" VARCHAR(16) NOT NULL,
    "password" INTEGER NOT NULL, --remember to change to hex or varchar
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    email VARCHAR(40),
    phone VARCHAR(12),
    id_address INTEGER REFERENCES ADDRESSES(id_address)
);

CREATE TABLE VAT_HISTORY (
    id_category INTEGER NOT NULL REFERENCES CATEGORIES(id_category),
    launch_date DATE NOT NULL,
    vat SMALLINT NOT NULL    
);

CREATE TABLE PRODUCTS_PROBLEMS (
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0),
    exhibition BOOLEAN NOT NULL,
    returned BOOLEAN NOT NULL,
    problem_description VARCHAR(100),
    discount SMALLINT CHECK (discount>0)
);

CREATE TABLE COMPLAINT (
    id_complaint INTEGER PRIMARY KEY,
    id_product INTEGER NOT NULL REFERENCES PRODUCTS(id),
    id_sale INTEGER NOT NULL REFERENCES SALES(id_sale),
    quantity NUMERIC(10) NOT NULL CHECK (quantity > 0),
    complaint_date DATE NOT NULL,
    complaint_description VARCHAR(100),
    result_date DATE,
    complaint_accepted BOOLEAN,
    id_employee INTEGER REFERENCES EMPLOYEES(id_employee)
    --data odbioru?
);

CREATE TABLE INTERNET_SALE(
    id_sale INTEGER NOT NULL REFERENCES SALES(id_sale),
    date_delivery DATE NOT NULL,
    method_delivery VARCHAR(10),
    id_address INTEGER REFERENCES ADDRESSES(id_address)
);

COMMIT;
