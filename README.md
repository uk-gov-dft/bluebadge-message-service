# DFT BLUE BADGE BETA - MESSAGE-SERVICE

## Getting Started in few minutes
From command line:
```
git clone git@github.com:uk-gov-dft/message-service.git
cd message-service
gradle wrapper
gradle build
gradle bootRun
```

## DEPLOYING ARTIFACTS TO LOCAL MAVEN REPOSITORY
```
cd model
gradle install
cd ..
cd client
gradle install
```
