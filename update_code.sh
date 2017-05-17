#!/bin/bash

# checkout circle2 test
git checkout feature/circle2/unit_test
# update main code
git checkout master -- ./app/src/main/
# update test code
git checkout master -- ./app/src/test/
# no need to update android

git add .
git commit -m "Update code from master"