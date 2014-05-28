#!/bin/bash

rm -f ../data/visible/*
#rm -f ../data/visible/sequence-0/*
#rm -f ../data/visible/sequence-1/*
#rm -f ../data/visible/sequence-2/*
#rm -f ../data/visible/sequence-3/*
cd ../bin
java edu/brown/visible/VisibleHumanWorkloadGenerator $1
mv ../data/visible/*.* ../data/visible/sequence-$1/
cd ../scripts
