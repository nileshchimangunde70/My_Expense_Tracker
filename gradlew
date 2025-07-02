#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under a License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass any JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

# Attempt to find java
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum number of open file descriptors to prevent
# "Too many open files" errors.
#
# Usually, this can be done with 'ulimit -n', but some systems
# don't support it, and others use 'limit' instead.
#
# Some systems have a soft limit that can be raised with 'ulimit -n'.
# A hard limit can be configured in '/etc/security/limits.conf'.
#
# On Mac OS X, the ulimit is lower than what's needed for some
# projects, and this needs to be adjust.
#
# On Solaris, the default ulimit is often 256, which is too low
# for gradle. It is recommended to set it to 1024 or higher.
#
# On HP-UX, 'ulimit -n' is used to show the limit, but 'ulimit -n unlimited'
# is not supported.
# Instead, the 'maxfiles' and 'maxfiles_lim' kernel parameters need to
# be configured.
if [ "$nonstop" = "false" ] ; then
    if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
        # Maximum supported open files is the hard limit of the system.
        # Some systems use 'ulimit -H -n' or 'limit -h descriptors' to retrieve it.
        if [ "$darwin" = "true" ] ; then
            # On OS X, the hard limit is unlimited.
            # We apply a new soft limit to the shell.
            # Note: This will not work in subshells, where the hard limit is
            #       not inherited.
            # Note: The 'classic' build of gradle on OS X is a fat binary
            #       that includes both a 32-bit and a 64-bit executable.
            #       This means that the following call to ulimit may fail, but
            #       that's ok.
            ulimit -n 10240
        else
            ulimit -n `ulimit -H -n`
        fi
    else
        ulimit -n "$MAX_FD"
    fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$APP_HOME" ] &&
        APP_HOME=`cygpath --unix "$APP_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$CLASSPATH" ] &&
        CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# For MSYS, ensure paths are in UNIX format before anything is touched
if $msys ; then
    [ -n "$APP_HOME" ] &&
        APP_HOME="`( cd "$APP_HOME" && pwd )`"
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME="`( cd "$JAVA_HOME" && pwd )`"
    [ -n "$CLASSPATH" ] &&
        CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# Set-up parameters for the invocation of the LTW
# For -d32/-d64: The JRockit JVM does not support these options.
# The JRockit JVM is identified by "BEA".
#
# The J9 JVM does not support these options.
# The J9 JVM is identified by "J9".
#
# The HP-UX JVM does not support these options.
# The HP-UX is identified by "HP-UX".
#
# The GNU interpreter for Java does not support these options.
# The GNU interpreter for Java is identified by "gij".
#
# The SAP JVM does not support these options.
# The SAP JVM is identified by "SAP".
#
# A 64-bit JVM can be identified by the "-d64" option.
#
# A 32-bit JVM can be identified by the "-d32" option.
#
# A 64-bit JVM will be preferred if it is available.
#
# A 32-bit JVM will be used if a 64-bit JVM is not available.
#
# If the JVM is 64-bit, the -d64 option will be removed.
#
# If the JVM is 32-bit, the -d32 option will be removed.
#
# If the JVM is neither 64-bit nor 32-bit, the -d64 and -d32 options
# will be removed.

# Collect all arguments for the java command, following the shell quoting rules.
# This is a replacement for the simpler, but not quote-safe `java_args=("$@")`
# which is not available in all shells.
# This is required to support arguments with spaces, like `-Dfoo="bar baz"`.
java_args=()
while [ $# -gt 0 ]; do
    case "$1" in
        # If the argument includes a space, we must wrap it in double quotes.
        *\ * )
            java_args[${#java_args[@]}]="\"$1\""
        ;;
        # Otherwise, we can just pass it as is.
        * )
            java_args[${#java_args[@]}]="$1"
        ;;
    esac
    shift
done

# Set title for Windows terminals
if $cygwin || $msys ; then
    TITLE_VALUE=$APP_NAME
    if [ -n "$GRADLE_BUILD_NAME" ] ; then
        TITLE_VALUE="$GRADLE_BUILD_NAME"
    fi
    TITLE="title \"$TITLE_VALUE\""
    if $cygwin ; then
        CYGWIN_ARGS="nontty"
        if [ -n "$GRADLE_BUILD_NAME" ] ; then
            CYGWIN_ARGS="$CYGWIN_ARGS $TITLE"
        fi
        export CYGWIN="$CYGWIN_ARGS"
    fi
fi

# Escape the arguments for the exec command.
# This is a replacement for the simpler, but not as portable `exec "$JAVACMD" "${java_args[@]}"`
# which is not available in all shells.
# This is required to support arguments with spaces, like `-Dfoo="bar baz"`.
eval set -- "$java_args"
# The "ev al" is a workaround for a bug in the Dash shell.
# https://bugs.launchpad.net/ubuntu/+source/dash/+bug/1043905
ev\
al exec "\"$JAVACMD\"" "$DEFAULT_JVM_OPTS" "$JAVA_OPTS" "$GRADLE_OPTS" "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
