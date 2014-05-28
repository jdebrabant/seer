#!/bin/bash

DATA_DIR="visible"

cd ../bin
echo "cleaning rules for workload-$1..."
java -Xmx4096m edu/brown/common/RuleCleaner $SEERDB_HOME/data/$DATA_DIR/sequence-$1/sequence-$1.rules $SEERDB_HOME/data/$DATA_DIR/sequence-$1/sequence-$1-clean.rules 5
cd ../scripts
cat $SEERDB_HOME/data/visible/sequence-$1/sequence-$1-clean.rules > $SEERDB_HOME/data/visible/sequence-$1/sequence-all.rules
#./merge_rules.sh
