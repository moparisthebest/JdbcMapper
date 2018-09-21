#!/bin/bash
if [[ ! -f $HOME/.traviscache/oracle-xe-11.2.0-1.0.x86_64.rpm.zip ]]
then
    wget -O $HOME/.traviscache/oracle-xe-11.2.0-1.0.x86_64.rpm.zip "$ORACLE_XE_URL"
fi
