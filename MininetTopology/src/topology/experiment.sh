#!/bin/bash
# script for running a ycsb workload on a client node
echo "Hello World!"
cd ../../../ycsb/YCSB-master/
bin/ycsb load elasticsearch -s -P workloads/workload_csd
bin/ycsb run elasticsearch -s -P workloads/workload_csd
