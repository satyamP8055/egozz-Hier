date

# Validate that the api code is already there to be built via pom.xml

if [ ! -f "pom.xml" ]; then
    echo "PULLING API FROM GIT"
    echo "__________________________________________________________________"
    git init
    echo "GIT INITIALISED"
    git remote add origin https://github.com/satyamP8055/egozz-1.2.git 
    echo "ALIAS REMOTE ADDED"
    git pull origin master
    echo "SOURCE PULLED FROM GIT"
else
    echo "DATA VALIDATED THROUGH POM.xml";
fi

# BUILD THE PROJECT
echo "========================================================================="
echo "MAVEN BUILDING FROM POM.XML"
mvn -version
echo "Java Version : " 
java -version
echo "Javac Version"
javac -version
 mvn clean package spring-boot:repackage -DskipTests
echo "========================================================================="
echo "Executing Final JAR"
# sleep 2000

if [ $EXEC_MODE = "dev" ]; then
    echo "Launching Development Artifact"
    java -jar ./target/egoz-1.2.jar
else
    echo "Launching Production Artifact"
    java -jar ./target/egoz-1.2.jar --db.host=$API_DB_HOST --db.user=$API_DB_USER --db.port=$API_DB_PORT --db.password=$API_DB_PASSWORD --db.name=$API_DB
fi

tail -f /dev/null