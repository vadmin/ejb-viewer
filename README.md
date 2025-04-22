# JBoss/WildFly Manager

A comprehensive JavaFX application for managing JBoss EAP and WildFly servers. This application provides a user-friendly interface to connect to remote JBoss/WildFly servers and manage JMS queues and EJBs.

![JBoss/WildFly Manager Screenshot](docs/scr-shot.png)

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Building from Source](#building-from-source)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
  - [Connecting to a Server](#connecting-to-a-server)
  - [Managing JMS Queues](#managing-jms-queues)
  - [Managing EJBs](#managing-ejbs)
- [Server Configuration](#server-configuration)
- [Troubleshooting](#troubleshooting)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Server Connection Management**
  - Connect to remote JBoss EAP 7.4 and WildFly 23.0.2 servers
  - Secure authentication with management user credentials
  - Connection status monitoring

- **JMS Queue Management**
  - View all JMS queues on the server
  - Display queue properties (name, JNDI name, status, message count, consumer count)
  - Start, stop, and restart queues
  - Real-time queue status updates

- **EJB Management**
  - View all EJB components deployed on the server
  - Display EJB properties (name, type, deployment, JNDI name)
  - Deploy new EJB packages (JAR, WAR, EAR)
  - Undeploy existing EJB packages

- **User Interface**
  - Modern, responsive JavaFX UI
  - Split pane layout for efficient navigation
  - Detailed information panels
  - Operation feedback and status updates

## Requirements

- **Java**: Java 17 or higher
- **Maven**: Maven 3.6 or higher (for building from source)
- **JBoss/WildFly Server**: JBoss EAP 7.4 or WildFly 23.0.2
- **Operating System**: Windows, macOS, or Linux

## Installation

### Pre-built Package

1. Download the latest release from the [Releases](https://github.com/yourusername/jboss-manager/releases) page
2. Extract the ZIP file to a directory of your choice
3. Run the application using the provided launcher script or JAR file

### Building from Source

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/jboss-manager.git
   cd jboss-manager
   ```

2. Build the application using Maven:
   ```
   mvn clean package
   ```

3. The built application will be available in the `target` directory

## Running the Application

### Using the JAR File

```
java -jar target/ejb-viewer-1.0.0.jar
```

### Using Maven

```
mvn javafx:run
```

### Using the Launcher Script

#### Windows
```
start.bat
```

#### macOS/Linux
```
./start.sh
```

## Usage Guide

### Connecting to a Server

1. Launch the application
2. Enter the server connection details:
   - **Host**: The hostname or IP address of the JBoss/WildFly server (e.g., `localhost` or `192.168.1.100`)
   - **Port**: The management port (usually `9990`)
   - **Username**: The management user username
   - **Password**: The management user password
3. Click the "Connect" button
4. The connection status will be displayed below the form
5. Once connected, the resources tree will be populated with JMS queues and EJBs

### Managing JMS Queues

1. Connect to a server
2. Expand the "JMS Queues" node in the resources tree
3. Select a queue to view its details
4. The details panel will show:
   - Queue name
   - JNDI name
   - Status (Running, Paused, etc.)
   - Message count
   - Consumer count
   - Durable status
5. Use the operation buttons to:
   - **Start**: Resume a paused queue
   - **Stop**: Pause a running queue
   - **Restart**: Stop and then start a queue

### Managing EJBs

1. Connect to a server
2. Expand the "EJBs" node in the resources tree
3. EJBs are grouped by deployment (JAR, WAR, EAR)
4. Select an EJB to view its details
5. The details panel will show:
   - EJB name
   - Type (Stateless Session Bean, Stateful Session Bean, etc.)
   - Deployment name
   - JNDI name
   - Status
   - Stateful status
6. Use the operation buttons to:
   - **Deploy**: Upload and deploy a new EJB package
   - **Undeploy**: Remove a deployed EJB package

## Server Configuration

### Management User

To connect to a JBoss/WildFly server, you need to have a management user configured. You can create a management user using the `add-user.sh` or `add-user.bat` script in the `bin` directory of your JBoss/WildFly installation.

Example:

```
# For JBoss EAP
./bin/add-user.sh -u admin -p password -g admin

# For WildFly
./bin/add-user.sh -u admin -p password -g admin
```

### Management Port

The default management port for JBoss/WildFly is `9990`. If your server is configured to use a different port, you'll need to specify that port when connecting.

### Firewall Configuration

Ensure that the management port (default: `9990`) is accessible from the machine running the JBoss/WildFly Manager application.

## Troubleshooting

### Connection Issues

- **Connection Refused**: Ensure the server is running and the management port is accessible
- **Authentication Failed**: Verify the username and password are correct
- **Unknown Host**: Check the hostname or IP address is correct

### Operation Failures

- **Deploy Failed**: Ensure the EJB package is valid and compatible with the server
- **Undeploy Failed**: The EJB might be in use by other components
- **Queue Operation Failed**: The queue might be in an incompatible state for the operation

### Application Issues

- **JavaFX Error**: Ensure you have JavaFX installed or included in the classpath
- **Java Version Error**: Verify you're using Java 17 or higher

## Architecture

The application follows a Model-View-Controller (MVC) architecture:

- **Model**: Represents the data and business logic
  - `ServerConnection`: Represents a connection to a JBoss/WildFly server
  - `JMSQueue`: Represents a JMS queue on the server
  - `EJBComponent`: Represents an EJB component on the server

- **View**: Represents the UI components
  - JavaFX FXML files define the layout
  - CSS files define the styling

- **Controller**: Handles user input and updates the model and view
  - `MainController`: Manages the main application window
  - `ConnectionService`: Handles server connections
  - `JMSService`: Manages JMS queue operations
  - `EJBService`: Manages EJB operations

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
