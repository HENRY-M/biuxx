#!/bin/sh
## Kill the current process.
ps -ef|grep ${project.artifactId}-${project.version}.jar|grep -v grep|awk '{print $2}'|xargs kill -9