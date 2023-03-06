#!/bin/bash
set -e

echo $DOCS_KEY | base64 -d > .travis/id_rsa
eval "$(ssh-agent -s)" # Start ssh-agent cache
chmod 600 .travis/id_rsa # Allow read access to the private key
ssh-add .travis/id_rsa # Add the private key to SSH


if [ ! -z "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    DOCS_RELEASE_DIR=$TRAVIS_TAG
    mvn --settings $TRAVIS_BUILD_DIR/.travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DskipTests=true -DnewVersion=$TRAVIS_TAG 1>/dev/null 2>/dev/null
else
    echo "not on a tag -> keep snapshot version in pom.xml"
    DOCS_RELEASE_DIR=snapshot
fi

mvn clean deploy --settings $TRAVIS_BUILD_DIR/.travis/settings.xml -DskipTests=true -Drelease=true -B -U

DOCS_RELEASES_DIR=sbtqa.github.io/releases/$DOCS_RELEASE_DIR

rm -rf .git/
git clone git@github.com:sbtqa/sbtqa.github.io.git
mkdir -p $DOCS_RELEASES_DIR
rm -rf $DOCS_RELEASES_DIR/*
cp -r page-factory-doc/target/doc/index.html page-factory-doc/target/doc/images/ $DOCS_RELEASES_DIR/
cd $DOCS_RELEASES_DIR
ls -1p |sort -V|tail -n +8| xargs -I {} rm -rf -- {}
cd ../../
curl https://${GITHUB_AUTH_TOKEN}@api.github.com/repositories/172893709/contents/releases -o releases.json
pwd
git add -A
git commit -m "$(printf "Add docs for $DOCS_RELEASE_DIR release\n")"
git push
