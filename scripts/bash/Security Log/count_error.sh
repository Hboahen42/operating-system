#!/usr/bin/bash

COUNT=$(grep -c "Error" server.log)
echo "The string 'Error' appears in $COUNT lines"