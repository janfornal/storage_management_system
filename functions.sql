CREATE OR REPLACE FUNCTION get_current_price(product_name VARCHAR(100))
RETURNS numeric(8, 2) AS 
$$
BEGIN
    RETURN (SELECT net_price
    FROM PRICE_HISTORY
    WHERE id_product = (SELECT id FROM PRODUCTS WHERE name = product_name)
    ORDER BY launch_date DESC
    LIMIT 1);
END;
$$ LANGUAGE 'plpgsql';
SELECT * FROM get_current_price('Valletta 130 Camera Bag - Black');