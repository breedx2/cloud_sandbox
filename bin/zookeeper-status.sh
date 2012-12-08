#!/bin/bash

MY_DIR=`dirname $0`
ZK=${MY_DIR}/../servers/zookeeper

"${ZK}/bin/zkServer.sh" status
