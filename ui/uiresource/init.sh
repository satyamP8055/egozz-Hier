# Validate project by git based on angular.json
if [ ! -f "angular.json" ]; then
    echo "PULLING UI FROM GIT for PROD"
    echo "__________________________________________________________________"
    git init
    git remote add origin https://github.com/satyamP8055/egozzFront.git
    git pull origin master
else
    echo "MOVING FOR NODE_MODULES VALIDATION BEFORE BUILD"
fi

echo "INSTALLING NPM DEPENDENCIES"
# Install NPM Dependencies
npm install

echo "BUILDING APPLICATION FOR PRODUCTION"
# Build Angular App for PRODUCTION
ng build --prod