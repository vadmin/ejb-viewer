package com.jbossmanager.service;

import com.jbossmanager.model.JMSQueue;
import org.jboss.dmr.ModelNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing JMS queues on JBoss/WildFly servers.
 */
public class JMSService {
    
    private final ConnectionService connectionService;
    
    public JMSService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }
    
    /**
     * Get all JMS queues from the server.
     * 
     * @return List of JMS queues
     * @throws Exception if an error occurs
     */
    public List<JMSQueue> getQueues() throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        List<JMSQueue> queues = new ArrayList<>();
        
        // Create operation to read all JMS queues
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-resources");
        op.get("child-type").set("jms-queue");
        
        // Set the subsystem address
        ModelNode address = op.get("address");
        address.add("subsystem", "messaging-activemq");
        address.add("server", "default");
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        if ("success".equals(result.get("outcome").asString())) {
            ModelNode queuesNode = result.get("result");
            for (String queueName : queuesNode.keys()) {
                ModelNode queueNode = queuesNode.get(queueName);
                JMSQueue queue = new JMSQueue();
                queue.setName(queueName);
                
                // Get JNDI name from entries (first entry)
                if (queueNode.hasDefined("entries") && queueNode.get("entries").asList().size() > 0) {
                    queue.setJndiName(queueNode.get("entries").asList().get(0).asString());
                }
                
                // Get durable property
                if (queueNode.hasDefined("durable")) {
                    queue.setDurable(queueNode.get("durable").asBoolean());
                }
                
                // Get queue status and metrics
                updateQueueStatus(queue);
                
                queues.add(queue);
            }
        } else {
            throw new Exception("Failed to get JMS queues: " + result.get("failure-description").asString());
        }
        
        return queues;
    }
    
    /**
     * Update the status and metrics of a JMS queue.
     * 
     * @param queue The JMS queue to update
     * @throws Exception if an error occurs
     */
    public void updateQueueStatus(JMSQueue queue) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Create operation to read queue runtime information
        ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("include-runtime").set(true);
        
        // Set the queue address
        ModelNode address = op.get("address");
        address.add("subsystem", "messaging-activemq");
        address.add("server", "default");
        address.add("jms-queue", queue.getName());
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        if ("success".equals(result.get("outcome").asString())) {
            ModelNode queueNode = result.get("result");
            
            // Get message count
            if (queueNode.hasDefined("message-count")) {
                queue.setMessageCount(queueNode.get("message-count").asInt());
            }
            
            // Get consumer count
            if (queueNode.hasDefined("consumer-count")) {
                queue.setConsumerCount(queueNode.get("consumer-count").asInt());
            }
            
            // Determine status
            if (queueNode.hasDefined("paused")) {
                boolean paused = queueNode.get("paused").asBoolean();
                queue.setStatus(paused ? "Paused" : "Running");
            } else {
                queue.setStatus("Unknown");
            }
        } else {
            queue.setStatus("Error");
        }
    }
    
    /**
     * Start a JMS queue.
     * 
     * @param queueName The name of the queue to start
     * @return true if successful, false otherwise
     * @throws Exception if an error occurs
     */
    public boolean startQueue(String queueName) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Create operation to resume the queue
        ModelNode op = new ModelNode();
        op.get("operation").set("resume-queue");
        
        // Set the queue address
        ModelNode address = op.get("address");
        address.add("subsystem", "messaging-activemq");
        address.add("server", "default");
        address.add("jms-queue", queueName);
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        return "success".equals(result.get("outcome").asString());
    }
    
    /**
     * Stop a JMS queue.
     * 
     * @param queueName The name of the queue to stop
     * @return true if successful, false otherwise
     * @throws Exception if an error occurs
     */
    public boolean stopQueue(String queueName) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Create operation to pause the queue
        ModelNode op = new ModelNode();
        op.get("operation").set("pause-queue");
        
        // Set the queue address
        ModelNode address = op.get("address");
        address.add("subsystem", "messaging-activemq");
        address.add("server", "default");
        address.add("jms-queue", queueName);
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        return "success".equals(result.get("outcome").asString());
    }
    
    /**
     * Restart a JMS queue.
     * 
     * @param queueName The name of the queue to restart
     * @return true if successful, false otherwise
     * @throws Exception if an error occurs
     */
    public boolean restartQueue(String queueName) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Stop and then start the queue
        boolean stopped = stopQueue(queueName);
        if (stopped) {
            return startQueue(queueName);
        }
        return false;
    }
}
