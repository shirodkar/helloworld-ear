# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Jakarta EE 10 multi-module Maven project packaging a frontend web application with a backend REST API into an Enterprise Archive (EAR). Targets WildFly 27+, EAP 8.1+, or compatible Jakarta EE 10 servers.

## Build Commands

```bash
# Standard build
mvn clean package

# OpenShift build with Galleon provisioning (runs only in ear module)
mvn clean package -P openshift

# Skip tests
mvn clean package -DskipTests
```

**Build outputs:**

- `backend-api/target/backend-api.war`
- `frontend/target/frontend.war`
- `ear/target/helloworld-ear.ear`

## Module Architecture

- **backend-api** (WAR, /api) - REST API with JPA persistence
- **frontend** (WAR, /) - Static HTML5 web interface
- **ear** (EAR) - Packages both WARs, contains openshift profile
- **custom-module/custom-rolemapper** (JAR) - Security role mapper for WildFly

## Key Locations

- **persistence.xml**: `backend-api/src/main/resources/META-INF/persistence.xml`
- **Database seed data**: Use `import.sql` in the same META-INF directory (standard JPA/Hibernate approach)
- **OpenShift profile**: Located in `ear/pom.xml` (not parent pom) so Galleon provisioning runs once
- **WildFly CLI scripts**: `ear/scripts/configure.cli`
- **Module installation**: `extensions/install.sh` copies JARs to WildFly module directories during S2I build

## REST Endpoints

- `GET /api/api/hello` - Returns JSON message with timestamp
- `GET /api/api/hello/greetings` - Returns all greetings from database

## Database Configuration

PostgreSQL datasource configured as `java:jboss/datasources/PostgreSQLDS`.

The Galleon `postgresql-datasource` and `postgresql-driver` layers provision the driver automatically when using `-P openshift`.

## Kubernetes/OpenShift Manifests

Located in `manifests/`:

- `deploy/` - WildFly server configs, ConfigMaps, Secrets, External Secrets
- `infra/` - PostgreSQL, namespaces, operator subscriptions (EAP subscription is namespace-specific to 'demo')
- `s2i/` - Source-to-Image build configs and image streams

## OpenShift Profile

The `ear/pom.xml` openshift profile uses eap-maven-plugin with:

- EAP 8.1 channel
- Galleon layers: cloud-server, elytron, elytron-oidc-client, postgresql-datasource, postgresql-driver, mail
- CLI packaging scripts from `ear/scripts/configure.cli`

## Dependencies

All Jakarta EE APIs use `provided` scope (supplied by app server). PostgreSQL JDBC driver uses `compile` scope.
