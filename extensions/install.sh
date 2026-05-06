#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SERVER_HOME="${SERVER_HOME:-/opt/server}"
SECURITY_MODULE_DIR="$SERVER_HOME/modules/com/example/helloworld/security/main"

echo "Installing custom modules..."
echo "Project Root: $PROJECT_ROOT"
echo "Server Home: $SERVER_HOME"
echo ""

# =============================================================================
# Install Custom Role Mapper Module
# =============================================================================
echo "Installing custom role mapper module..."
echo "Target Module Dir: $SECURITY_MODULE_DIR"

mkdir -p "$SECURITY_MODULE_DIR"
CUSTOM_JAR_SOURCE="$PROJECT_ROOT/custom-module/custom-rolemapper/target/custom-rolemapper.jar"
if [ ! -f "$CUSTOM_JAR_SOURCE" ]; then
    echo "ERROR: Custom role mapper JAR not found at $CUSTOM_JAR_SOURCE"
    exit 1
fi
cp "$CUSTOM_JAR_SOURCE" "$SECURITY_MODULE_DIR/custom-rolemapper.jar"
echo "✓ Custom role mapper installed"