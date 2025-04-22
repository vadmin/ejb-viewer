package com.jbossmanager.service;

import com.jbossmanager.model.ServerConnection;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Service class for managing connections to JBoss/WildFly servers.
 */
public class ConnectionService {
    
    private ModelControllerClient client;
    private ServerConnection serverConnection;
    
    public ConnectionService() {
        this.serverConnection = new ServerConnection();
    }
    
    /**
     * Connect to the JBoss/WildFly server using the provided connection details.
     * 
     * @param host The server hostname or IP address
     * @param port The management port (usually 9990)
     * @param username The management user username
     * @param password The management user password
     * @return true if connection was successful, false otherwise
     */
    public boolean connect(String host, int port, String username, String password) {
        serverConnection.setHost(host);
        serverConnection.setPort(port);
        serverConnection.setUsername(username);
        serverConnection.setPassword(password);
        
        try {
            // Create a callback handler for authentication
            CallbackHandler callbackHandler = new CallbackHandler() {
                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof NameCallback) {
                            ((NameCallback) callback).setName(username);
                        } else if (callback instanceof PasswordCallback) {
                            ((PasswordCallback) callback).setPassword(password.toCharArray());
                        }
                    }
                }
            };
            
            // Create the client connection
            client = ModelControllerClient.Factory.create(
                InetAddress.getByName(host), 
                port,
                callbackHandler
            );
            
            // Test connection with a simple operation
            ModelNode op = new ModelNode();
            op.get("operation").set("read-attribute");
            op.get("name").set("server-state");
            op.get("address").setEmptyList();
            
            ModelNode result = client.execute(op);
            boolean success = "success".equals(result.get("outcome").asString());
            
            if (success) {
                serverConnection.setConnected(true);
                serverConnection.setConnectionStatus("Connected");
                
                // Get server version
                ModelNode versionOp = new ModelNode();
                versionOp.get("operation").set("read-attribute");
                versionOp.get("name").set("product-version");
                versionOp.get("address").setEmptyList();
                
                ModelNode versionResult = client.execute(versionOp);
                if ("success".equals(versionResult.get("outcome").asString())) {
                    String version = versionResult.get("result").asString();
                    serverConnection.setServerVersion(version);
                }
            } else {
                serverConnection.setConnected(false);
                serverConnection.setConnectionStatus("Failed: " + result.get("failure-description").asString());
            }
            
            return success;
        } catch (Exception e) {
            serverConnection.setConnected(false);
            serverConnection.setConnectionStatus("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                // Ignore
            } finally {
                client = null;
                serverConnection.setConnected(false);
                serverConnection.setConnectionStatus("Disconnected");
            }
        }
    }
    
    /**
     * Get the current server connection.
     * 
     * @return The server connection
     */
    public ServerConnection getServerConnection() {
        return serverConnection;
    }
    
    /**
     * Get the model controller client.
     * 
     * @return The model controller client
     */
    public ModelControllerClient getClient() {
        return client;
    }
    
    /**
     * Check if connected to the server.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return client != null && serverConnection.isConnected();
    }
}
