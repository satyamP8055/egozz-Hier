source credentials.env

if [ $EXEC_MODE = "dev" ]; then
    echo "Launching in Development Mode"
    echo "============================================================================================"
    echo "DB Credentials for Development Mode are not synchronized with environment variables"
    echo "Check application.properties for more info..."
    docker-compose up --remove-orphans
else
    echo "Launching in Production Mode"
    echo "============================================================================================"
    docker-compose -f docker-compose-prod.yaml up --remove-orphans
fi

echo "============================================================================================"
echo "Starting Countdown"
for i in 3 2 1
do
    echo $i
    sleep 1
done