package com.jbossmanager.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ServerConnection model.
 */
public class ServerConnectionTest {
    
    @Test
    public void testDefaultConstructor() {
        ServerConnection connection = new ServerConnection();
        
        // Verify default values
        assertEquals("localhost", connection.getHost());
        assertEquals(9990, connection.getPort());
        assertEquals("", connection.getUsername());
        assertEquals("", connection.getPassword());
        assertFalse(connection.isConnected());
        assertEquals("Unknown", connection.getServerVersion());
        assertEquals("Disconnected", connection.getConnectionStatus());
    }
    
    @Test
    public void testParameterizedConstructor() {
        ServerConnection connection = new ServerConnection("example.com", 8080, "admin", "password");
        
        // Verify constructor sets values correctly
        assertEquals("example.com", connection.getHost());
        assertEquals(8080, connection.getPort());
        assertEquals("admin", connection.getUsername());
        assertEquals("password", connection.getPassword());
        assertFalse(connection.isConnected());
        assertEquals("Unknown", connection.getServerVersion());
        assertEquals("Disconnected", connection.getConnectionStatus());
    }
    
    @Test
    public void testSettersAndGetters() {
        ServerConnection connection = new ServerConnection();
        
        // Set values
        connection.setHost("example.com");
        connection.setPort(8080);
        connection.setUsername("admin");
        connection.setPassword("password");
        connection.setConnected(true);
        connection.setServerVersion("WildFly 23.0.2.Final");
        connection.setConnectionStatus("Connected");
        
        // Verify values
        assertEquals("example.com", connection.getHost());
        assertEquals(8080, connection.getPort());
        assertEquals("admin", connection.getUsername());
        assertEquals("password", connection.getPassword());
        assertTrue(connection.isConnected());
        assertEquals("WildFly 23.0.2.Final", connection.getServerVersion());
        assertEquals("Connected", connection.getConnectionStatus());
    }
    
    @Test
    public void testToString() {
        ServerConnection connection = new ServerConnection("example.com", 8080, "admin", "password");
        
        // Verify toString returns host:port
        assertEquals("example.com:8080", connection.toString());
    }
    
    @Test
    public void testProperties() {
        ServerConnection connection = new ServerConnection();
        
        // Verify properties are not null
        assertNotNull(connection.hostProperty());
        assertNotNull(connection.portProperty());
        assertNotNull(connection.usernameProperty());
        assertNotNull(connection.passwordProperty());
        assertNotNull(connection.connectedProperty());
        assertNotNull(connection.serverVersionProperty());
        assertNotNull(connection.connectionStatusProperty());
        
        // Test property binding
        connection.hostProperty().set("newHost");
        assertEquals("newHost", connection.getHost());
        
        connection.portProperty().set(8888);
        assertEquals(8888, connection.getPort());
        
        connection.usernameProperty().set("newUser");
        assertEquals("newUser", connection.getUsername());
        
        connection.passwordProperty().set("newPass");
        assertEquals("newPass", connection.getPassword());
        
        connection.connectedProperty().set(true);
        assertTrue(connection.isConnected());
        
        connection.serverVersionProperty().set("newVersion");
        assertEquals("newVersion", connection.getServerVersion());
        
        connection.connectionStatusProperty().set("newStatus");
        assertEquals("newStatus", connection.getConnectionStatus());
    }
}
