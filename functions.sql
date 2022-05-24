CREATE OR REPLACE FUNCTION get_current_price(id INTEGER)
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
