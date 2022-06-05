BEGIN;
DROP TABLE IF EXISTS INTERNET_SALE                              CASCADE;
DROP TABLE IF EXISTS COMPLAINT                                  CASCADE;
DROP TABLE IF EXISTS CLIENTS_RETURN                             CASCADE;
DROP TABLE IF EXISTS PRODUCTS_SOLD                              CASCADE;
DROP TABLE IF EXISTS PRODUCTS_PROBLEMS_SOLD                     CASCADE;
DROP TABLE IF EXISTS SALES                                      CASCADE;
DROP TABLE IF EXISTS PRODUCTS_PROBLEMS                          CASCADE;
DROP TABLE IF EXISTS VAT_HISTORY                                CASCADE;
DROP TABLE IF EXISTS CLIENTS                                    CASCADE;
DROP TABLE IF EXISTS ADDRESSES                                  CASCADE;
DROP TABLE IF EXISTS EMPLOYEES                                  CASCADE;
DROP TABLE IF EXISTS PRODUCTS_DELIVERIES                        CASCADE;
DROP TABLE IF EXISTS DELIVERIES                                 CASCADE;
DROP TABLE IF EXISTS STORE_STATUS                               CASCADE;
DROP TABLE IF EXISTS SUPPLIERS                                  CASCADE;
DROP TABLE IF EXISTS PRICE_HISTORY                              CASCADE;
DROP TABLE IF EXISTS PARAMETER_PRODUCTS                         CASCADE;
DROP TABLE IF EXISTS POSSIBLE_PARAMETERS                        CASCADE;
DROP TABLE IF EXISTS PRODUCTS                                   CASCADE;
DROP TABLE IF EXISTS PARAMETERS                                 CASCADE;
DROP TABLE IF EXISTS CATEGORIES                                 CASCADE;
DROP TABLE IF EXISTS BRAND                                      CASCADE;



DROP FUNCTION IF EXISTS returned_check                          CASCADE;
DROP FUNCTION IF EXISTS allowed_param_check                     CASCADE;
DROP FUNCTION IF EXISTS check_insert_products_sold              CASCADE;
DROP FUNCTION IF EXISTS check_delete_products_sold              CASCADE;
DROP FUNCTION IF EXISTS update_increase_store_status            CASCADE;
DROP FUNCTION IF EXISTS insert_id_products_to_store_status      CASCADE;


DROP FUNCTION IF EXISTS nice_repr_of_products                   CASCADE;
DROP FUNCTION IF EXISTS get_gross_price                         CASCADE;
DROP FUNCTION IF EXISTS get_current_vat                         CASCADE;
DROP FUNCTION IF EXISTS get_current_price                       CASCADE;
DROP FUNCTION IF EXISTS get_price                               CASCADE;
DROP FUNCTION IF EXISTS get_vat                                 CASCADE;
DROP FUNCTION IF EXISTS get_gross_price_time                    CASCADE;
DROP FUNCTION IF EXISTS get_sale_price                          CASCADE;
DROP FUNCTION IF EXISTS get_gross_sale_price                    CASCADE;
DROP FUNCTION IF EXISTS sale_product_info                       CASCADE;
DROP FUNCTION IF EXISTS sale_full_product_info                  CASCADE;

COMMIT;
