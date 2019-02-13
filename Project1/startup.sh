#!/bin/bash
machineList=$1
numNodes=$2
registryPort=$3
echo Number of Nodes $numNodes
echo Registry Port $registryPort

machines=()

for F in $(cat $machineList) ; do
  machines+=(${F})
done

echo ${machines[0]}


gnome-terminal -- bash -c "ls; java -cp build/classes/java/main/ cs455.overlay.node.Registry $registryPort;"

for ((i=1; i<=$numNodes; i++))
do 
	
	gnome-terminal --tab -t Messenger_Node -- bash -c "ssh ${machines[i]} 'sleep 2; java -cp Desktop/cs455/Project1/build/classes/java/main/ cs455.overlay.node.MessagingNode denver.cs.colostate.edu $registryPort'"
	echo $i
done


