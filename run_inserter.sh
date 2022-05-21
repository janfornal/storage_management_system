#!/bin/bash

for item in `find ./data/ -name "*.json"`:
do
    item=${item:7:-5}
    cmd="sed -e 's/XXXXXXX/${item}/g' inserter.sql | psql -d storage"
    eval $cmd
done;
cmd="sed -e 's/XXXXXXX/wireless-adapters/g' inserter.sql | psql -d storage"
eval $cmd