#!/bin/sh
cp=~/.m2/repository/com/h2database/h2/1.3.158/h2-1.3.158.jar
if [ -n "$H2DRIVERS" ] ; then
  cp="$cp:$H2DRIVERS"
fi
if [ -n "$CLASSPATH" ] ; then
  cp="$cp:$CLASSPATH"
fi
java -cp "$cp" org.h2.tools.Console -url jdbc:h2:../db-storage/jbpm -user sa

