\copy categories FROM './data/categories.csv' with delimiter ',' csv header;
\copy brand FROM './data/brand.csv' with delimiter ',' csv header;
\copy products FROM './data/products.csv' with delimiter ',' csv header;
\copy vat_history FROM './data/vat_history.csv' with delimiter ',' csv header;
\copy price_history FROM './data/price_history.csv' with delimiter ',' csv header;
\copy parameters FROM './data/parameters.csv' with delimiter ',' csv header;
\copy possible_parameters FROM './data/possible_parameters.csv' with delimiter ',' csv header;
\copy parameter_products FROM './data/parameter_products.csv' with delimiter ',' csv header;