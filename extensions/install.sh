#!/usr/bin/env bash
set -x
echo "Running $PWD/install.sh"
injected_dir=$1
# copy any needed files into the target build.
cp -rf ${injected_dir} $JBOSS_HOME/extensions

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
cp '$PROJECT_ROOT/ear/target/module-jars/custom-rolemapper-1.0.0-SNAPSHOT.jar' '$JBOSS_HOME/modules/com/example/helloworld/security/main/custom-rolemapper.jar'