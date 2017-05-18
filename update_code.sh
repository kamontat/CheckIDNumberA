#!/bin/bash

FEATURE="feature/circle2/unit_test"
UPDATE="update/gradle-3-alpha"
UPDATE_MESSAGE="update code from master branch!"

branch=

# ------------------------------------------------
# Function
# ------------------------------------------------

function update_code {
    git checkout master -- ./app/src/main
    git checkout master -- ./app/src/test 
    git checkout master -- ./app/src/androidTest
}

function update_gradle {
    git checkout master -- build.gradle
    git checkout master -- app/build.gradle
}

function update_other {
    git checkout master -- README.md
    git checkout master -- app/libs
}

function commit {
    git add .
    git commit -m "$UPDATE_MESSAGE"
}

function push {
    git push
}

function back {
    git checkout master
}

# ------------------------------------------------
# code
# ------------------------------------------------

printf "1 = feature/circleci1 \n"
printf "2 = update gradle \n"
printf "update branch[1|2]: "

read -n 1 ans

if [[ $ans == 1 ]]; then
    branch="$FEATURE"
else 
    branch="$UPDATE"
fi

git checkout $branch

update_code
update_gradle
update_other

commit
push

back
