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