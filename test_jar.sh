#!/bin/bash

echo "Testing JAR from project directory..."
cd /Users/clement/Documents/uvinersity/CS2103T/ip/ip
echo "list" | gtimeout 5s java -jar build/libs/udin.jar 2>/dev/null || echo "list" | java -jar build/libs/udin.jar &

echo -e "\n\nTesting JAR from different directory..."
cd /tmp
echo "list" | gtimeout 5s java -jar /Users/clement/Documents/uvinersity/CS2103T/ip/ip/build/libs/udin.jar 2>/dev/null || echo "list" | java -jar /Users/clement/Documents/uvinersity/CS2103T/ip/ip/build/libs/udin.jar &
