#!/bin/bash

echo "================================"
echo "      DBUNIT DEMO PROJECT"
echo "================================"
echo

echo "1. Compiling project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo
echo "2. Running demo application..."
echo "================================"
mvn exec:java -Dexec.mainClass="com.demo.DemoDBUnitMain"
if [ $? -ne 0 ]; then
    echo "Demo application failed!"
    exit 1
fi

echo
echo "3. Running DBUnit tests..."
echo "================================"
mvn test
if [ $? -ne 0 ]; then
    echo "Tests failed!"
    exit 1
fi

echo
echo "================================"
echo "   DEMO COMPLETED SUCCESSFULLY!"
echo "================================"
echo
echo "All tests passed! DBUnit demo works perfectly."
echo "Check the output above to see all DBUnit features in action."
echo 