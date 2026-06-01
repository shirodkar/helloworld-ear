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

cp -r $PROJECT_ROOT/ear/target/module-jars/modules/ $SERVER_HOME/modules/