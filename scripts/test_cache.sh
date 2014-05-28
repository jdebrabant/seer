#!/bin/bash

cd ../scripts
#rm -f ../data/*
#./generate_workload.sh $1

#./mine_rules.sh sequence-$1

#./clean_rules.sh $1
cd ../bin
#java -cp $CLASSPATH:.:../src/lib/trove.jar edu/brown/test/CacheTester ../data/visible/vmm_train.seq ../data/visible/sequence-all.rules ../data/visible/sequence-all.asc 
java -cp $CLASSPATH:.:../src/lib/trove.jar edu/brown/test/RuntimeTester ../data/visible/sequence-$1/vmm_train.seq ../data/visible/sequence-$1/sequence-all.rules ../data/visible/sequence-$1/sequence-all.asc ../data/visible/sequence-$1/meta.txt
cd ../scripts