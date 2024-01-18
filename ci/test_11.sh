#!/bin/bash

set -e

#
# This script is for running inside the docker container
#

cd /workspace/bc-java
source ci/common.sh


export JAVA_HOME=`openjdk_11`
export PATH=$JAVA_HOME/bin:$PATH
export PATH=$PATH:`gradle-bin-6`

gradle -stacktrace clean build




