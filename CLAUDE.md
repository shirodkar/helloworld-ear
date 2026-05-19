# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Jakarta EE 10 multi-module Maven project** that demonstrates best practices for packaging a frontend web application with a backend REST API into a single Enterprise Archive (EAR). The project includes:

- **backend-api**: REST API module (WAR) providing JSON endpoints
- **frontend**: Static web UI module (WAR) with HTML5 and JavaScript
- **ear**: Enterprise Archive module that packages both WARs
- **custom-module/custom-rolemapper**: Custom JAR for role mapping functionality
- **gitops**: ArgoCD application manifests for Kubernetes deployment
- **manifests**: Kubernetes/OpenShift manifests for infrastructure and deployment

## Build Commands

All build commands use Maven (Java 17+ required). Run from the project root directory.

### Standard Build

```bash
mvn clean package
```

Produces:

- `backend-api/target/backend-api.war`
- `frontend/target/frontend.war`
- `ear/target/helloworld-ear.ear`

### OpenShift Build with EAP Plugin

```bash
mvn clean package -P openshift
```

Configures EAP Maven plugin for OpenShift deployment with cloud-optimized layers, Galleon feature packs, and EAP 8.1 server configuration.

### Build Without Tests

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

## Project Structure and Architecture

### Module Hierarchy

The project uses a parent POM (`pom.xml`) with four modules:

1. **backend-api** (WAR)
   - REST API endpoints under `/api` context root
   - JAX-RS application configured via `HelloWorldApplication` class
   - Single endpoint: `GET /api/hello` returns JSON with message and timestamp
   - Uses Jakarta REST (JAX-RS), CDI, and JSON processing APIs
   - Includes PostgreSQL JDBC driver for database connectivity

2. **frontend** (WAR)
   - Static HTML5 web interface deployed at root context `/`
   - Dark-themed responsive design
   - JavaScript client that makes CORS-friendly calls to the backend API
   - Includes error handling and user feedback mechanisms

3. **ear** (Enterprise Archive)
   - Parent module that packages both WARs into a single deployable unit
   - Maven EAR plugin configures context roots: `/api` for backend, `/` for frontend
   - Maven Dependency plugin copies the custom role mapper JAR to `module-jars` directory for inclusion
   - Uses library bundling directory: `lib`
   - Supports standard build and OpenShift profile build

4. **custom-module/custom-rolemapper** (JAR)
   - Custom security role mapper implementation
   - Packaged and included during the EAR build process
   - Role definitions stored in `src/main/resources/roles.properties`

### Deployment Architecture

- **Jakarta EE 10 Application Server**: WildFly 27+, EAP 8.1+, Payara 6+, or GlassFish 7+
- **PostgreSQL Integration**: JDBC driver included for database connectivity
- **OpenShift Support**: EAP Maven plugin layers enable cloud-optimized deployment with OIDC client support

### Dependencies

All non-container dependencies are in `provided` scope (supplied by the application server):

- jakarta.platform:jakarta.jakartaee-api (full EE 10 API)
- jakarta.ws.rs:jakarta.ws.rs-api (REST endpoints)
- jakarta.enterprise:jakarta.enterprise.cdi-api (dependency injection)
- jakarta.json:jakarta.json-api and jakarta.json.bind:jakarta.json.bind-api (JSON processing)
- jakarta.persistence:jakarta.persistence-api (JPA for database)

The PostgreSQL JDBC driver is in `compile` scope for inclusion in the backend-api WAR.

## Key Build Plugins

- **maven-compiler-plugin**: Java 17 source/target compilation
- **maven-war-plugin**: WAR packaging for frontend and backend-api modules
- **maven-ear-plugin**: Enterprise Archive creation with context root configuration
- **maven-dependency-plugin**: Copies custom-rolemapper JAR for EAR inclusion
- **eap-maven-plugin** (openshift profile): Generates cloud-optimized EAP server images using Galleon

## Deployment Locations

### Local WildFly/EAP

Deploy the EAR to: `$WILDFLY_HOME/standalone/deployments/helloworld-ear.ear`

### Application URLs (Local)

- Frontend: `http://localhost:8080/`
- Backend API: `http://localhost:8080/api/api/hello`

### OpenShift/Kubernetes

Deployment manifests are organized in:

- `manifests/deploy/`: WildFly server configs, service accounts, ConfigMaps, Secrets, External Secrets, and deployment configurations
- `manifests/infra/`: PostgreSQL, namespaces, and Red Hat operator subscriptions
- `manifests/s2i/`: Source-to-Image (S2I) build configs and image streams
- `gitops/app-of-apps/`: ArgoCD application manifests for GitOps-based deployments

## Configuration

### EAR Build Configuration (ear/pom.xml)

The `ear/scripts/configure.cli` file is used by the EAP Maven plugin when building with the openshift profile to apply WildFly CLI commands for:

- Cloud server configuration
- PostgreSQL datasource setup
- OIDC client configuration
- Mail service setup

## Important Notes

- **Java Version**: All modules target Java 17 as both source and target
- **Jakarta EE 10**: This project uses the latest Jakarta EE standard with all `jakarta.*` namespace APIs
- **WAR Context Roots**: Backend is at `/api`, frontend is at `/` (configured in ear/pom.xml)
- **No web.xml Required**: Both WAR modules use `failOnMissingWebXml=false` since they use annotation-based configuration
- **Custom Role Mapper**: The custom-rolemapper JAR is copied to the EAR during build and made available to the application server through the module-jars directory
