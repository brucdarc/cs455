#!/bin/bash
machineList=$1
numNodes=$2
registryIp=$3
registryPort=$4
echo Required Arguments
echo Machine List: $machineList
echo Number of Nodes: $numNodes
echo Registry Ip: $registryIp
echo Registry Port: $registryPort
echo The registry must be the machine you are on.

dir=$(pwd)



echo $dir

machines=()

for F in $(cat $machineList) ; do
  machines+=(${F})
done

#echo ${machines[0]}


gnome-terminal -- bash -c "java -cp $dir/build/classes/java/main/ cs455.overlay.node.Registry $registryPort;"

for ((i=1; i<=$numNodes; i++))
do 
	
	gnome-terminal --tab -t Messenger_Node -- bash -c "ssh ${machines[i]} 'cd /; sleep 2; java -cp $dir/build/classes/java/main/ cs455.overlay.node.MessagingNode $registryIp $registryPort'"
	echo Started Node: $i
done


