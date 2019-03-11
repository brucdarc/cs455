#!/bin/bash

filename='machines'
fileLines=`cat $filename`

for line in $fileLines ; do
    echo $line
    ssh $line 'pkill -u cutreap java'
done

