#!/bin/sh
## Define the node name and init.
NODE_NAME=`hostname`

## Kill the current process first.
ps -ef|grep ${project.artifactId}-.*.jar|grep -v grep|awk '{print $2}'|xargs kill -9

## Wait for a while
sleep 2

## Restart the Process
nohup java -server -Xms${jvm.param.xms} -Xmx${jvm.param.xmx} -Xss${jvm.param.xss} -XX:+AggressiveOpts -XX:+UseBiasedLocking -XX:PermSize=${jvm.param.permsize} -XX:MaxPermSize=${jvm.param.permsize.max} -XX:+DisableExplicitGC -XX:MaxTenuringThreshold=31 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=${jvm.param.largepagesize} -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -Djava.awt.headless=true -Dnode.name=$NODE_NAME -jar ../${project.artifactId}-${project.version}.jar >console.log 2>1 &