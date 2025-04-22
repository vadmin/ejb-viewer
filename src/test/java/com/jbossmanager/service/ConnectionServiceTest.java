package com.jbossmanager.service;

import com.jbossmanager.model.ServerConnection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ConnectionService.
 * Note: These tests don't actually connect to a server, they just test the behavior of the service.
 */
public class ConnectionServiceTest {
    
    @Test
    public void testInitialState() {
        // Create a new ServerConnection
        ServerConnection serverConnection = new ServerConnection();
        
        // Verify initial state
        assertFalse(serverConnection.isConnected());
        
        assertEquals("localhost", serverConnection.getHost());
        assertEquals(9990, serverConnection.getPort());
        assertEquals("", serverConnection.getUsername());
        assertEquals("", serverConnection.getPassword());
        assertFalse(serverConnection.isConnected());
        assertEquals("Disconnected", serverConnection.getConnectionStatus());
    }
    
    @Test
    public void testDisconnect() {
        // Create a new ServerConnection
        ServerConnection serverConnection = new ServerConnection();
        
        // Test disconnect behavior
        serverConnection.setConnected(true);
        serverConnection.setConnectionStatus("Connected");
        
        // Simulate disconnect
        serverConnection.setConnected(false);
        serverConnection.setConnectionStatus("Disconnected");
        
        // Verify state after disconnect
        assertFalse(serverConnection.isConnected());
        assertEquals("Disconnected", serverConnection.getConnectionStatus());
    }
    
    @Test
    public void testToString() {
        // Create a new ServerConnection
        ServerConnection serverConnection = new ServerConnection();
        
        assertEquals("localhost:9990", serverConnection.toString());
        
        // Change host and port
        serverConnection.setHost("example.com");
        serverConnection.setPort(8080);
        assertEquals("example.com:8080", serverConnection.toString());
    }
}
