#!/usr/bin/bash

echo "Date & Time: $(date)"
echo "Hostname: $(hostname)"
echo "User: $(whoami)"
df -h / | awk 'NR==2 {print "Total: " $2 ", Free: " $4}'