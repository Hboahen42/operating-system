#!/bin/bash

FILE="$1"
LIMIT=1048576

if [[ -f "$FILE" ]]; then
	SIZE=$(stat -c%s "$FILE")
	if [ "$SIZE" -gt "$LIMIT" ]; then
		echo "Warning: File is too large"
	else
		echo "File size is within limits"
	fi
else
	echo "File does not exist"
fi
