#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SERVER_HOME="${SERVER_HOME:-/opt/server}"
SECURITY_MODULE_DIR="$SERVER_HOME/modules/com/example/helloworld/security/main"
MYSQL_MODULE_DIR="$SERVER_HOME/modules/system/layers/base/com/mysql/main"

echo "Installing custom modules..."
echo "Project Root: $PROJECT_ROOT"
echo "Server Home: $SERVER_HOME"
echo ""

# =============================================================================
# Install Custom Role Mapper Module
# =============================================================================
echo "Installing custom role mapper module..."
echo "Target Module Dir: $SECURITY_MODULE_DIR"

CUSTOM_JAR_SOURCE="$PROJECT_ROOT/custom-module/custom-rolemapper/target/custom-rolemapper.jar"
cp "$CUSTOM_JAR_SOURCE" "$SECURITY_MODULE_DIR/custom-rolemapper.jar"

# =============================================================================
# Install Custom Role Mapper Module
# =============================================================================
echo "Installing mysql module..."
echo "Target Module Dir: $MYSQL_MODULE_DIR"

MYSQL_JAR_SOURCE="$PROJECT_ROOT/backend-api/target/module-jars/mysql-connector-java-8.0.26.jar"
cp "$MYSQL_JAR_SOURCE" "$MYSQL_MODULE_DIR/mysql-connector-java-8.0.26.jar"