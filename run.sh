#!/bin/bash

# Demonstration script for Library Management System
# This script shows how to run the application

echo "========================================"
echo "  Library Management System Demo"
echo "========================================"
echo ""

# Check if SQLite JDBC driver is present
if [ ! -f "sqlite-jdbc-3.45.0.0.jar" ]; then
    echo "Downloading SQLite JDBC driver..."
    wget -q https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
fi

if [ ! -f "slf4j-api-2.0.9.jar" ]; then
    echo "Downloading SLF4J API..."
    wget -q https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
fi

if [ ! -f "slf4j-simple-2.0.9.jar" ]; then
    echo "Downloading SLF4J Simple..."
    wget -q https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar
fi

# Compile the project
echo "Compiling Java files..."
javac -cp ".:sqlite-jdbc-3.45.0.0.jar" src/model/*.java src/interfaces/*.java src/exception/*.java src/manager/*.java src/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "To run the application, use:"
    echo "  java -cp \".:sqlite-jdbc-3.45.0.0.jar:slf4j-api-2.0.9.jar:slf4j-simple-2.0.9.jar:src\" Main"
    echo ""
    echo "Starting application..."
    echo ""
    java -cp ".:sqlite-jdbc-3.45.0.0.jar:slf4j-api-2.0.9.jar:slf4j-simple-2.0.9.jar:src" Main
else
    echo "✗ Compilation failed!"
    exit 1
fi
