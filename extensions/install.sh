#!/usr/bin/env bash
set -x
echo "Running $PWD/install.sh"
injected_dir=$1
# copy any needed files into the target build.
cp -rf ${injected_dir} $JBOSS_HOME/extensions

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SERVER_HOME="${SERVER_HOME:-/opt/server}"

echo "Installing custom modules..."
echo "Project Root: $PROJECT_ROOT"
echo "Server Home: $SERVER_HOME"
echo ""

# =============================================================================
# Install Custom Role Mapper Module
# =============================================================================
echo "Installing custom role mapper module..."
SECURITY_MODULE_DIR="$SERVER_HOME/modules/com/example/helloworld/security/main"
echo "Target Module Dir: $SECURITY_MODULE_DIR"

mkdir -p "$SECURITY_MODULE_DIR"
CUSTOM_JAR_SOURCE="$PROJECT_ROOT/custom-module/custom-rolemapper/target/custom-rolemapper.jar"
if [ ! -f "$CUSTOM_JAR_SOURCE" ]; then
    echo "ERROR: Custom role mapper JAR not found at $CUSTOM_JAR_SOURCE"
    exit 1
fi
cp "$CUSTOM_JAR_SOURCE" "$SECURITY_MODULE_DIR/custom-rolemapper.jar"
echo "✓ Custom role mapper installed"

# =============================================================================
# Install CyberArk ASCP Module
# =============================================================================
echo "Installing CyberArk ASCP module..."
ASCP_MODULE_DIR="$SERVER_HOME/modules/com/cyberark/jdbc/main"
echo "Target Module Dir: $ASCP_MODULE_DIR"

mkdir -p "$ASCP_MODULE_DIR"
ASCP_JAR_SOURCE="$PROJECT_ROOT/extensions/CyberArk.jdbc.JBoss.jar"
if [ ! -f "$ASCP_JAR_SOURCE" ]; then
    echo "ERROR: CyberArk ASCP JAR not found at $ASCP_JAR_SOURCE"
    exit 1
fi
cp "$ASCP_JAR_SOURCE" "$ASCP_MODULE_DIR/CyberArk.jdbc.JBoss.jar"
echo "✓ CyberArk ASCP installed"