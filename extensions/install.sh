#!/usr/bin/env bash
set -x
echo "Running $PWD/install.sh"
injected_dir=$1
# copy any needed files into the target build.
cp -rf ${injected_dir} $JBOSS_HOME/extensions

cp $PROJECT_ROOT/custom-module/custom-rolemapper/target/custom-role-mapper.jar $JBOSS_HOME/modules/com/example/helloworld/security/main/