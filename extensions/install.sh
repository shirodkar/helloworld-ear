#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SERVER_HOME="${SERVER_HOME:-/opt/server}"
MODULE_DIR="$SERVER_HOME/modules/com/example/helloworld/security/main"

echo "Installing custom role mapper module..."
echo "Project Root: $PROJECT_ROOT"
echo "Server Home: $SERVER_HOME"
echo "Target Module Dir: $MODULE_DIR"

# Check if JAR exists
JAR_SOURCE="$PROJECT_ROOT/custom-module/custom-rolemapper/target/custom-rolemapper.jar"
if [ ! -f "$JAR_SOURCE" ]; then
    echo "ERROR: JAR file not found at $JAR_SOURCE"
    echo "Please run: mvn clean package -DskipTests"
    exit 1
fi

# Create module directory if it doesn't exist
mkdir -p "$MODULE_DIR"

# Copy JAR to module directory
echo "Copying custom-rolemapper.jar to $MODULE_DIR..."
cp "$JAR_SOURCE" "$MODULE_DIR/custom-rolemapper.jar"

# Copy module.xml if it exists in the project
if [ -f "$PROJECT_ROOT/modules/com/example/helloworld/security/main/module.xml" ]; then
    echo "Copying module.xml to $MODULE_DIR..."
    cp "$PROJECT_ROOT/modules/com/example/helloworld/security/main/module.xml" "$MODULE_DIR/module.xml"
fi

echo "Installation complete!"
echo "Module installed at: $MODULE_DIR"
