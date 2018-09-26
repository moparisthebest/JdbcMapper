#!/bin/bash
# maven central only supports TLS 1.2+ now, and java 6 does not, ouch, use java 7 to download them...

set -e
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64 mvn -B -Dhttps.protocols=TLSv1.2 --settings .travis-settings.xml -P oracle clean install
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64 mvn --offline -B clean
git checkout -- .
git clean -dxf
rm -rf ~/.m2/repository/com/moparisthebest/jdbcmapper
