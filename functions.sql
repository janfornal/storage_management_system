CREATE OR REPLACE FUNCTION get_current_price(id INTEGER)   -- pierwszych 3 funkcji nie powinno być, jak bedzie czas to refactor
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
    RETURN ROUND(get_current_price(id)*(1 + (get_current_vat(id))/100.0), 2);
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

CREATE OR REPLACE FUNCTION get_price(id INTEGER, t TIMESTAMP)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN (SELECT net_price
            FROM PRICE_HISTORY
            WHERE id_product = id AND launch_date <= t
            ORDER BY launch_date DESC
            LIMIT 1);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_price_problem(id_problem INTEGER, t TIMESTAMP)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN (SELECT ROUND(get_price(id_product, t)*(100 - discount)/100.0, 2)
            FROM products_problems WHERE id_product_with_problem = id_problem);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_vat(id_arg INTEGER, t TIMESTAMP)
    RETURNS integer AS
$$
BEGIN
    RETURN (SELECT vat
            FROM VAT_HISTORY
            WHERE id_category = (SELECT id_category FROM products WHERE id = id_arg) AND launch_date <= t
            ORDER BY launch_date DESC
            LIMIT 1);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_gross_price_time(id INTEGER, t TIMESTAMP)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN ROUND(get_price(id, t)*(1 + (get_vat(id, t))/100.0), 2);
END;
$$ LANGUAGE 'plpgsql';


CREATE OR REPLACE FUNCTION get_gross_price_time_problem(id_problem INTEGER, t TIMESTAMP)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN (SELECT ROUND(get_price(id_product, t)*(1 + (get_vat(id_product, t))/100.0)*((100 - discount)/100.0), 2)
            FROM products_problems WHERE id_product_with_problem = id_problem);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_sale_price(id INTEGER)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN (SELECT SUM(get_price(id_product, (SELECT sales_date FROM sales WHERE id_sale = id))*quantity)
            FROM products_sold
            WHERE id_sale = id) +
           (SELECT SUM(get_price_problem(id_product_with_problem, (SELECT sales_date FROM sales WHERE id_sale = id))*quantity)
            FROM products_problems_sold
            WHERE id_sale = id);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION get_gross_sale_price(id INTEGER)
    RETURNS numeric(8, 2) AS
$$
BEGIN
    RETURN (SELECT SUM(get_gross_price_time(id_product, (SELECT sales_date FROM sales WHERE id_sale = id))*quantity)
            FROM products_sold
            WHERE id_sale = id) +
           (SELECT SUM(get_gross_price_time_problem(id_product_with_problem, (SELECT sales_date FROM sales WHERE id_sale = id))*quantity)
            FROM products_problems_sold
            WHERE id_sale = id);
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION sale_product_info(id INTEGER)
    RETURNS TABLE(
        id_product INTEGER,
        product_name VARCHAR(100),
        quantity NUMERIC(10),
        net_price NUMERIC(8, 2),
        gross_price NUMERIC(8, 2)
    ) AS
$$
DECLARE
    id_s INTEGER := id;
    time TIMESTAMP := (SELECT sales_date FROM SALES s WHERE s.id_sale = id_s);
BEGIN
    RETURN QUERY SELECT
    p.id, p.name, ps.quantity - (
        SELECT COALESCE(SUM(cr.quantity), 0)
        FROM CLIENTS_RETURN cr
        WHERE cr.id_sale = id_s
        AND cr.id_product = p.id
    ) - (
        SELECT COALESCE(SUM(co.quantity), 0)
        FROM complaint co
        WHERE co.id_sale = id_s
        AND co.id_product = p.id
    ), get_price(p.id, time), get_gross_price_time(p.id, time)
    FROM products_sold ps JOIN products p
    ON p.id = ps.id_product WHERE ps.id_sale = id_s;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION sale_full_product_info(id INTEGER)
    RETURNS TABLE(
        id_product INTEGER,
        product_name VARCHAR(100),
        quantity NUMERIC(10),
        net_price NUMERIC(8, 2),
        gross_price NUMERIC(8, 2),
        new BOOLEAN
    ) AS
$$
DECLARE
    id_s INTEGER := id;
    time TIMESTAMP := (SELECT sales_date FROM SALES s WHERE s.id_sale = id_s);
BEGIN
    RETURN QUERY SELECT *, TRUE FROM sale_product_info(id_s) UNION SELECT
    p.id, p.name, ps.quantity,
    get_price(p.id, time) * (100 - pp.discount) / 100.0,
    get_gross_price_time(p.id, time) * (100 - pp.discount) / 100.0, FALSE
    FROM PRODUCTS_PROBLEMS_SOLD ps JOIN PRODUCTS_PROBLEMS pp USING (id_product_with_problem)
    JOIN products p ON p.id = pp.id_product
    WHERE ps.id_sale = id_s;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION repr_of_products_problems(id INTEGER)
    RETURNS TABLE(
         id_return INTEGER,
         description VARCHAR(100),
         quant NUMERIC(10)
     ) AS
$$
BEGIN
    RETURN QUERY
    SELECT id_product_with_problem, problem_description, quantity
    FROM PRODUCTS_PROBLEMS
    WHERE id_product = id AND quantity > 0;
END;
$$ LANGUAGE 'plpgsql';
