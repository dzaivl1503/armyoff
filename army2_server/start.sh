#!/bin/bash
cd "$(dirname "$0")"

echo "========================================================="
echo "   Starting Mobi Army 2 VPS Hybrid Server (Linux)..."
echo "========================================================="

if [ -f "Army2Server.jar" ]; then
    java -cp "Army2Server.jar:lib/*" Army2Server
elif [ -d "bin" ]; then
    java -cp "bin:lib/*" Army2Server
else
    chmod +x build.sh 2>/dev/null
    ./build.sh
    java -cp "bin:lib/*" Army2Server
fi
