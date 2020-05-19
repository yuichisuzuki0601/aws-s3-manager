#!/usr/bin/env bash
cd `dirname $0`

sudo java -jar target/aws-s3-manager-1.0.0.jar upload sample.txt