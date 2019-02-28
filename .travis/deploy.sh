set -e
DOCS_RELEASE_DIR=snapshot

if [ ! -z "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    $DOCS_RELEASE_DIR=$TRAVIS_TAG
    mvn --settings $TRAVIS_BUILD_DIR/.travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DskipTests=true -DnewVersion=$TRAVIS_TAG 1>/dev/null 2>/dev/null
else
    echo "not on a tag -> keep snapshot version in pom.xml"
fi

mvn clean deploy --settings $TRAVIS_BUILD_DIR/.travis/settings.xml -DskipTests=true -Drelease=true -B -U

DOCS_RELEASES_DIR=page-factory-2-site/releases/$DOCS_RELEASE_DIR

rm -rf .git/
git clone https://${GITHUB_AUTH_TOKEN}@github.com/sbtqa/page-factory-2-site.git
mkdir -p $DOCS_RELEASES_DIR
rm -rf $DOCS_RELEASES_DIR/*
cp -r page-factory-doc/target/doc/index.html page-factory-doc/target/doc/images/ $DOCS_RELEASES_DIR/
cd $DOCS_RELEASES_DIR
git add -A
git commit -m "Add docs for `$DOCS_RELEASE_DIR` release"
git push
