CREATE OR REPLACE FUNCTION get_current_price(id INTEGER)   -- dodaj timestamp
RETURNS numeric(8, 2) AS 
$$
BEGIN
    RETURN (SELECT net_price
    FROM PRICE_HISTORY
    WHERE id_product = id
    ORDER BY launch_date DESC
    LIMIT 1);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_current_vat(id_arg INTEGER)
    RETURNS integer AS
$$
BEGIN
    RETURN (SELECT vat
    FROM VAT_HISTORY
    WHERE id_category = (SELECT id_category FROM products WHERE id = id_arg)
    ORDER BY launch_date DESC
    LIMIT 1);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_gross_price(id INTEGER)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN ROUND(get_current_price(id)*(1 + (get_current_vat(id))/100), 2);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION nice_repr_of_products()  -- dodaj timestamp (celowo to nie jest view)
    RETURNS table(id int, 
    brand varchar(100), 
    name varchar(100), 
    category varchar(100), 
    amount numeric(10), 
    net_price numeric(8, 2)) AS
$$
BEGIN
    RETURN query
    SELECT products.id,
    (SELECT brand.name FROM brand WHERE id_brand = products.id_brand),
    products.name,
    (SELECT categories.name FROM CATEGORIES WHERE id_category = products.id_category),
    (SELECT store_status.quantity FROM STORE_STATUS WHERE store_status.id_product = products.id),
    get_current_price(products.id)
    FROM products;
END;
$$ LANGUAGE 'plpgsql';    