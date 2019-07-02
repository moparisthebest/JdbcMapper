#!/bin/sh
set -e

updateSnapshots=false
if [ "$1" == "--update" ]
then
    updateSnapshots=true
    shift
fi

mvn="mvn -B -pl test clean test -Dtest=SnapshotTest -DupdateSnapshots=$updateSnapshots"

mvn "$@" -B -pl '!test' clean install
$mvn "$@" -DJdbcMapper.beanSuffix=Bean
$mvn "$@" -DjdbcMapper.databaseType=BIND -DJdbcMapper.beanSuffix=BindBean
$mvn "$@" -DjdbcMapper.databaseType=ANY -DJdbcMapper.beanSuffix=AnyBean
$mvn "$@" -DjdbcMapper.databaseType=UNNEST -DJdbcMapper.beanSuffix=UnNestBean
#$mvn "$@" -DjdbcMapper.databaseType=ORACLE -P oracle -DJdbcMapper.beanSuffix=OracleBean
