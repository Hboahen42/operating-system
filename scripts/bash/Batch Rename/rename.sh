#!/usr/bin/bash

for file in *.txt; do
    mv "$file" "OLD_$file"
    echo "Renamed $file to OLD_$file"
done
