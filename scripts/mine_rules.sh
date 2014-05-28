#!/bin/bash

DATA_DIR="visible"
SUPPORT=".1"

$SEERDB_HOME/third_party/cSPADE/util/makebin $SEERDB_HOME/data/$DATA_DIR/$1/$1.asc $SEERDB_HOME/data/$DATA_DIR/$1/$1.data
$SEERDB_HOME/third_party/cSPADE/util/getconf -i $SEERDB_HOME/data/$DATA_DIR/$1/$1 -o $SEERDB_HOME/data/$DATA_DIR/$1/$1
$SEERDB_HOME/third_party/cSPADE/util/exttpose -i $SEERDB_HOME/data/$DATA_DIR/$1/$1 -o $SEERDB_HOME/data/$DATA_DIR/$1/$1 -s $SUPPORT -l -x
$SEERDB_HOME/third_party/cSPADE/spade -r -o -e 1 -Z 2 -l 1 -u 1 -s $SUPPORT -i $SEERDB_HOME/data/$DATA_DIR/$1/$1  > $SEERDB_HOME/data/$DATA_DIR/$1/$1.rules

# use -z to limit number of items 