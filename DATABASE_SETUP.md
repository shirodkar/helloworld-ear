# MySQL Database Integration

This document explains how to set up and use the MySQL database integration with the Hello World application.

## Components Added

### Backend API

1. **JPA Entity Classes**:
   - `User.java` - User entity with fields: id, username, email, firstName, lastName
   - `Product.java` - Product entity with fields: id, name, description, price, stockQuantity
   - `Order.java` - Order entity with fields: id, userId, orderDate, totalAmount, status

2. **DatabaseInitializer.java** (`backend-api/src/main/java/com/example/helloworld/api/DatabaseInitializer.java`)
   - EJB singleton that automatically initializes the database on startup
   - Uses `@Singleton` and `@Startup` annotations to run at application startup
   - Automatically populates tables with sample data if empty:
     - 5 sample users
     - 8 sample products
     - 5 sample orders
   - Uses JPA `EntityManager` to persist entities

3. **persistence.xml** (`backend-api/src/main/webapp/WEB-INF/persistence.xml`)
   - JPA configuration file
   - Defines the "primary" persistence unit
   - Configures Hibernate with MySQL 8 dialect
   - Auto-creates tables on startup (`hibernate.hbm2ddl.auto=create`)

4. **DataService.java** (`backend-api/src/main/java/com/example/helloworld/api/DataService.java`)
   - Manages database queries via SQL
   - Methods:
     - `getDataFromDatabase(String query)` - Returns a single row as a Map
     - `getAllDataFromDatabase(String query)` - Returns all rows as a List of Maps

5. **DataResource.java** (`backend-api/src/main/java/com/example/helloworld/api/DataResource.java`)
   - REST resource exposing database operations
   - Endpoints:
     - `GET /api/data/single?query=<sql>` - Execute query and return first row (defaults to `SELECT VERSION()`)
     - `GET /api/data/all?query=<sql>` - Execute query and return all rows

### Frontend

The `index.html` has been updated with:
- A new "Get Database Data" button
- `getDatabaseData()` JavaScript function that calls the `/api/api/data/single` endpoint
- Displays the database response in the response div

## Automatic Database Initialization

The database tables are automatically created and populated when the application starts. The `DatabaseInitializer` singleton bean:
1. Checks if the database is empty on application startup
2. If empty, creates tables and inserts sample data
3. If data already exists, skips initialization
4. Logs progress to the application server logs

## Configuration

To use the database integration, you need to configure a MySQL datasource in WildFly.

### Create MySQL Datasource in WildFly

1. **Using CLI Script**:
   ```bash
   # Connect to WildFly CLI
   $WILDFLY_HOME/bin/jboss-cli.sh -c
   
   # Create the datasource
   /subsystem=datasources/xa-data-source=MysqlDS:add(jndi-name=java:jboss/datasources/MysqlDS,driver-name=mysql,xa-datasource-class=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)
   /subsystem=datasources/xa-data-source=MysqlDS/xa-datasource-property=ServerName:add(value=localhost)
   /subsystem=datasources/xa-data-source=MysqlDS/xa-datasource-property=Port:add(value=3306)
   /subsystem=datasources/xa-data-source=MysqlDS/xa-datasource-property=DatabaseName:add(value=testdb)
   /subsystem=datasources/xa-data-source=MysqlDS/xa-datasource-property=User:add(value=root)
   /subsystem=datasources/xa-data-source=MysqlDS/xa-datasource-property=Password:add(value=yourpassword)
   /subsystem=datasources/xa-data-source=MysqlDS:create-connection-profile()
   /subsystem=datasources/xa-data-source=MysqlDS:test-connection-in-pool()
   ```

2. **Or using Configuration File** (standalone.xml):
   ```xml
   <datasources>
       <xa-datasource jndi-name="java:jboss/datasources/MysqlDS" pool-name="MysqlDS_Pool">
           <xa-datasource-property name="ServerName">localhost</xa-datasource-property>
           <xa-datasource-property name="Port">3306</xa-datasource-property>
           <xa-datasource-property name="DatabaseName">testdb</xa-datasource-property>
           <xa-datasource-property name="User">root</xa-datasource-property>
           <xa-datasource-property name="Password">yourpassword</xa-datasource-property>
           <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
           <driver>mysql</driver>
           <validation>
               <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
               <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
           </validation>
       </xa-datasource>
   </datasources>
   ```

3. **Ensure MySQL Driver is Deployed**:
   Copy `mysql-connector-java-5.1.49.jar` to `$WILDFLY_HOME/modules/system/layers/base/com/mysql/main/`

## Usage Examples

### From Frontend
Click the "Get Database Data" button to fetch the MySQL server version.

### From curl
```bash
# Get MySQL version
curl http://localhost:8080/api/api/data/single

# Query a table (replace with your table name)
curl "http://localhost:8080/api/api/data/single?query=SELECT%20*%20FROM%20users%20LIMIT%201"

# Get all rows
curl "http://localhost:8080/api/api/data/all?query=SELECT%20*%20FROM%20users"
```

## Database Setup Example

Create a test database and table:
```sql
CREATE DATABASE testdb;
USE testdb;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

INSERT INTO users (name, email) VALUES ('John Doe', 'john@example.com');
INSERT INTO users (name, email) VALUES ('Jane Smith', 'jane@example.com');
```

Then query it:
```bash
curl "http://localhost:8080/api/api/data/all?query=SELECT%20*%20FROM%20users"
```

## Dependencies Added

- `jakarta.persistence:jakarta.persistence-api:3.1.0` - Jakarta Persistence (JPA)
- `mysql:mysql-connector-java:5.1.49` - MySQL JDBC Driver

Both are marked as `provided` scope, assuming they will be provided by the WildFly application server.

## Security Notes

- **Query Parameters**: The current implementation allows arbitrary SQL queries via query parameters. For production, implement:
  - Query validation and whitelisting
  - Prepared statements with parameterized queries
  - Authentication and authorization checks
  - SQL injection prevention
  
- **Error Handling**: The implementation returns database error messages to the client. For production, consider:
  - Logging detailed errors server-side
  - Returning generic error messages to clients
  - Implementing proper exception handling
