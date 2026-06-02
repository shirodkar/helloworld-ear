# Jakarta EE 10 Hello World - EAR Project

A simple multi-module Jakarta EE 10 Enterprise Archive (EAR) application demonstrating best practices for packaging a frontend web application with a backend REST API.

## Project Overview

This project showcases a complete Jakarta EE 10 application structure with:
- **Frontend WAR**: Interactive HTML5 web interface with dark theme
- **Backend API WAR**: REST API providing simple endpoints
- **EAR Module**: Packages both WARs into a single enterprise archive for deployment

## Project Structure

```
helloworld-ear/
├── backend-api/          # REST API module (WAR)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/helloworld/api/
│       │   ├── HelloWorldApplication.java
│       │   └── HelloWorldResource.java
│       └── webapp/WEB-INF/beans.xml
├── frontend/             # Web UI module (WAR)
│   ├── pom.xml
│   └── src/main/webapp/
│       ├── index.html
│       └── WEB-INF/beans.xml
├── ear/                  # Enterprise Archive module
│   └── pom.xml
├── pom.xml              # Parent POM with modules
├── .gitignore
└── README.md
```

## Prerequisites

- Java 17 or higher
- Apache Maven 3.8.0 or higher
- Jakarta EE 10 compatible application server:
  - WildFly 27+ or EAP 8.1+
  - Payara 6+
  - GlassFish 7+

## Building the Project

### Standard Build

```bash
mvn clean package
```

This produces:
- `backend-api/target/backend-api.war` - Backend API module
- `frontend/target/frontend.war` - Frontend module
- `ear/target/helloworld-ear.ear` - Complete EAR package

### OpenShift Build with EAP Plugin

```bash
mvn clean package -P openshift
```

This uses the EAP Maven plugin to build an OpenShift-optimized image with:
- EAP 8.1 Galleon pack
- Cloud server layer
- OIDC client support

## Deployment

### Local Deployment (WildFly/EAP)

1. Deploy the EAR to your application server:
   ```bash
   cp ear/target/helloworld-ear.ear $WILDFLY_HOME/standalone/deployments/
   ```

2. Start the application server:
   ```bash
   $WILDFLY_HOME/bin/standalone.sh
   ```

3. Access the application:
   - Frontend: `http://localhost:8080/`
   - Backend API: `http://localhost:8080/api/api/hello`

### OpenShift Deployment

The `openshift` profile configures the project for deployment to Red Hat OpenShift Container Platform (OCP).

## API Endpoints

### GET /api/api/hello

Returns a JSON response from the backend API.

**Response:**
```json
{
  "message": "Hello, World!",
  "timestamp": 1671234567890
}
```

**Example:**
```bash
curl http://localhost:8080/api/api/hello
```

## Technologies Used

- **Jakarta EE 10**: Latest enterprise Java standard
- **Jakarta REST (JAX-RS)**: RESTful web services
- **Jakarta CDI**: Dependency injection
- **JSON-B/JSON-P**: JSON processing
- **Maven**: Build and dependency management

## Development

### Project Dependencies

All dependencies use `provided` scope, assuming they will be provided by the Jakarta EE 10 application server.

- `jakarta.platform:jakarta.jakartaee-api:10.0.0`
- `jakarta.ws.rs:jakarta.ws.rs-api:3.1.0`
- `jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1`
- `jakarta.json:jakarta.json-api:2.1.0`
- `jakarta.json.bind:jakarta.json.bind-api:3.0.0`

### Maven Profiles

- **openshift**: Configures EAP Maven plugin for OpenShift deployment with cloud-optimized layers

## File Structure Details

### backend-api Module
- JAX-RS application configured at `/api` context
- Single REST endpoint: `GET /api/hello`
- Returns JSON via Jakarta JSON-P

### frontend Module
- Static HTML5 page with responsive dark theme
- JavaScript client making CORS-friendly API calls
- Error handling and user feedback
- Deployed at root context `/`

### ear Module
- Packages both WARs with correct context roots
- Generates Jakarta EE 10 compliant `application.xml`
- Configures library bundling directory

## Building with Specific Configurations

### Skip Tests
```bash
mvn clean package -DskipTests
```

### Compile Only
```bash
mvn clean compile
```

### Install to Local Repository
```bash
mvn clean install
```

## Troubleshooting

### Build fails with dependency resolution errors
Ensure you have internet access and Maven central repository is accessible.

### Deployment fails to WildFly
Verify your WildFly version supports Jakarta EE 10 (WildFly 27+ or EAP 8.1+).

### Frontend cannot reach backend API
Check that the API context root is correctly set to `/api` and that both applications are deployed.

## License

This is a sample project provided as-is for educational purposes.

## Contributing

This is a simple demonstration project. For production use, consider:
- Adding comprehensive error handling
- Implementing security (authentication, authorization)
- Adding logging and monitoring
- Including unit and integration tests
- Implementing API versioning
- Adding data persistence layer
