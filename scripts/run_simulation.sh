#!/bin/bash

cd ../src
javac *.java
java SequenceGenerator $1
cd ../scripts
./mine_all.sh
./clean_rules.sh
cd ../src
java -cp $CLASSPATH:lib/trove.jar PredictionModelTester ../data/vmm_train.seq ../data/sequence-all.rules ../data/sequence-all.asc 
cd ../scripts
