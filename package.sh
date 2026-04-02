#!/usr/bin/env bash
set -e
mvn verify -P linux-package
echo "App image built at: target/dist/ejb-viewer/"
