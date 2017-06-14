#!/bin/sh
find */src/{main,test}/java -type f -name '*.java' -print0 | xargs -0 sed -i -e 's@/\*IFJAVA8_START@//IFJAVA8_START@' -e 's@IFJAVA8_END\*/@//IFJAVA8_END@'
