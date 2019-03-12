#!/bin/bash
userName=$1
filename='machines'
fileLines=`cat $filename`

for line in $fileLines ; do
    echo $line
    gnome-terminal --tab -t Messenger_Node -- bash -c "ssh $line 'pkill -u $userName java'"
done

