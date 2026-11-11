#!/bin/sh
# Maven Wrapper script
# This is a minimal script that invokes java directly with the correct classpath
# to avoid shell argument parsing issues with -q flag

set -e

MAVEN_MULTI_MODULE_DIR="$(cd "$(dirname "$0")" && pwd)"
export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-21-openjdk-amd64}

exec java -Dmaven.multiModuleProjectDirectory="$MAVEN_MULTI_MODULE_DIR" \
  -cp "$MAVEN_MULTI_MODULE_DIR/.mvn/wrapper/maven-wrapper.jar" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"