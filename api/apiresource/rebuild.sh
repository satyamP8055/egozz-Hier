echo "start building application"
echo "------------------------------------------"
curl -X POST localhost:8055/actuator/shutdown
echo "------------------------------------------"
docker exec api mvn clean package -DskipTests
echo "------------------------------------------"
echo "Executing Built package"
docker exec api java -jar ./target/egoz-1.2.jar
sleep 2
echo "Starting in Background "
echo "=========================================="
echo "Starting Count Down"
echo "3"
sleep 1
echo "2"
sleep 1
echo "1"
sleep 1