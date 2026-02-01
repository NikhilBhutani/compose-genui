#!/bin/sh

#
# Gradle wrapper script for POSIX systems
#

# Resolve APP_HOME
APP_HOME=$( cd "${0%[/\\]*}" > /dev/null && pwd -P ) || exit

# Download gradle-wrapper.jar if missing
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Gradle wrapper..."
    WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v8.5.0/gradle/wrapper/gradle-wrapper.jar"
    if command -v curl > /dev/null 2>&1; then
        curl -sL "$WRAPPER_URL" -o "$WRAPPER_JAR"
    elif command -v wget > /dev/null 2>&1; then
        wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
    else
        echo "Error: curl or wget required to download Gradle wrapper" >&2
        exit 1
    fi
fi

# Find Java
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

# Run Gradle
exec "$JAVACMD" \
    -Xmx64m -Xms64m \
    -Dorg.gradle.appname="${0##*/}" \
    -classpath "$WRAPPER_JAR" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
