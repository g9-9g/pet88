# Pet Management API

This is a Spring Boot application for pet management with REST API endpoints.

## API Documentation

This project uses Spring REST Docs to generate API documentation. The documentation is automatically generated during the build process.

### Generating API Documentation

To generate the API documentation, run:

```bash
mvn clean package
```

This will:
1. Run the tests that generate API documentation snippets
2. Process the AsciiDoc files that include these snippets
3. Generate HTML documentation
4. Include the documentation in the built JAR file

### Accessing the Documentation

Once the application is running, the API documentation is available at:

```
http://localhost:8080/api/docs
```

This URL redirects to the full documentation page.

### Documentation Structure

The API documentation includes:

- Authentication endpoints
- Pet management endpoints
- Request and response formats
- Authentication requirements
- Error handling information

### Extending the Documentation

To document new endpoints:

1. Create test classes that use Spring REST Docs annotations
2. Run the tests to generate snippets
3. Update the AsciiDoc files to include the new snippets
4. Rebuild the application

## Development

### Running the Application

```bash
mvn spring-boot:run
```

### Running Tests

```bash
mvn test
``` 