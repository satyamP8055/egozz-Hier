# Validate project by git based on angular.json

if [ ! -f "angular.json" ]; then
    echo "PULLING UI FROM GIT"
    echo "__________________________________________________________________"
    git init
    git remote add origin https://github.com/satyamP8055/egozzFront.git
    git pull origin master
else
    echo "MOVING FOR NODE_MODULES VALIDATION"
fi

# Install NPM Dependencies
npm install

# Deploy Angular App on dev
ng serve --host 0.0.0.0 --disable-host-check