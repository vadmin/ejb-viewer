package com.jbossmanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Model class representing a connection to a JBoss/WildFly server.
 */
public class ServerConnection {
    
    private final StringProperty host = new SimpleStringProperty(this, "host", "localhost");
    private final IntegerProperty port = new SimpleIntegerProperty(this, "port", 9990);
    private final StringProperty username = new SimpleStringProperty(this, "username", "");
    private final StringProperty password = new SimpleStringProperty(this, "password", "");
    private final BooleanProperty connected = new SimpleBooleanProperty(this, "connected", false);
    private final StringProperty serverVersion = new SimpleStringProperty(this, "serverVersion", "Unknown");
    private final StringProperty connectionStatus = new SimpleStringProperty(this, "connectionStatus", "Disconnected");
    
    public ServerConnection() {
    }
    
    public ServerConnection(String host, int port, String username, String password) {
        setHost(host);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }
    
    // Host property
    public StringProperty hostProperty() {
        return host;
    }
    
    public String getHost() {
        return host.get();
    }
    
    public void setHost(String host) {
        this.host.set(host);
    }
    
    // Port property
    public IntegerProperty portProperty() {
        return port;
    }
    
    public int getPort() {
        return port.get();
    }
    
    public void setPort(int port) {
        this.port.set(port);
    }
    
    // Username property
    public StringProperty usernameProperty() {
        return username;
    }
    
    public String getUsername() {
        return username.get();
    }
    
    public void setUsername(String username) {
        this.username.set(username);
    }
    
    // Password property
    public StringProperty passwordProperty() {
        return password;
    }
    
    public String getPassword() {
        return password.get();
    }
    
    public void setPassword(String password) {
        this.password.set(password);
    }
    
    // Connected property
    public BooleanProperty connectedProperty() {
        return connected;
    }
    
    public boolean isConnected() {
        return connected.get();
    }
    
    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }
    
    // Server Version property
    public StringProperty serverVersionProperty() {
        return serverVersion;
    }
    
    public String getServerVersion() {
        return serverVersion.get();
    }
    
    public void setServerVersion(String serverVersion) {
        this.serverVersion.set(serverVersion);
    }
    
    // Connection Status property
    public StringProperty connectionStatusProperty() {
        return connectionStatus;
    }
    
    public String getConnectionStatus() {
        return connectionStatus.get();
    }
    
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus.set(connectionStatus);
    }
    
    @Override
    public String toString() {
        return getHost() + ":" + getPort();
    }
}
