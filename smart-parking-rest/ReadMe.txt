## Build

The build uses Apache Maven. Simply use:

```
mvn clean install
```

## Feature and Deployment

On a running Karaf instance, register the features repository using:

```
karaf@root()> feature:repo-add mvn:com.ericsson.iot/smart-parking-rest-features/1.0-SNAPSHOT/xml
```

Then, you can install the service provider feature:

```
karaf@root()> feature:install smart-parking-rest-provider
```

And the service client feature using Apache CXF:

```
karaf@root()> feature:install smart-parking-rest-client-cxf
```

## Usage

this client feature will start a thread which simulates IoT sensors by calling Rest service to send data, list the current status of parking lots 
