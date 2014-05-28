#!/bin/bash

cat $SEERDB_HOME/data/visible/sequence-0/sequence-0-clean.rules > sequence-all.rules
cat $SEERDB_HOME/data/visible/sequence-1/sequence-1-clean.rules >> sequence-all.rules
cat $SEERDB_HOME/data/visible/sequence-2/sequence-2-clean.rules >> sequence-all.rules
#cat ../data/sequence-3/sequence-3-clean.rules >> sequence-all.rules
#cat ../data/sequence-4/sequence-4-clean.rules >> sequence-all.rules
mv sequence-all.rules $SEERDB_HOME/data/visible/