DOCS_DIR=master

if [ ! -z "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    $DOCS_DIR=$TRAVIS_TAG
    mvn --settings $TRAVIS_BUILD_DIR/.travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=$TRAVIS_TAG 1>/dev/null 2>/dev/null
else
    echo "not on a tag -> keep snapshot version in pom.xml"
fi

mvn clean deploy --settings $TRAVIS_BUILD_DIR/.travis/settings.xml -DskipTests=true -Drelease=true -B -U

git clone https://github.com/sbtqa/page-factory-2-site.git
mkdir -p page-factory-2-site/releases/$DOCS_DIR
rm -rf page-factory-2-site/releases/$DOCS_DIR/*
cp -r page-factory-doc/target/doc/index.html page-factory-doc/target/doc/images/ page-factory-2-site/releases/$DOCS_DIR/
cd page-factory2-site/
git add -A
git ci -m "Add docs for ${DOCS_DIR} release"
git push
