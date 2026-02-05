#!/bin/bash

echo "Top 5 memory Consuming Process:"
ps -eo comm,pid,%mem, --sort=-%mem | head -n 6