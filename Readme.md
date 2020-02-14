# Applications Required

## Development Mode
* Docker
* git Bash
* JDK 1.8
* Eclipse with Spring Tool Suite Plugin
* Node 12.3.1
* Angular CLI 8

## Production Mode
* Docker
* git Bash

# Initial Configurations
* Global CRLF to LF Configurations
```
git config --global core.autocrlf input
```
# Environment Configurations
* Configuration in "credentials.env", Details as follows
    - EXEC_MODE : "dev" for Development & "prod" for production Mode...
    - API_DB : Database Credentials for Production Environment
    > DB credentials will strictly be required for production environment & won't affect the development Environment