package com.jbossmanager.service;

import com.jbossmanager.model.JMSQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JMSService.
 * Note: These tests don't actually connect to a server, they just test the behavior of the service.
 */
public class JMSServiceTest {
    
    @Test
    public void testQueueProperties() {
        // Create a new JMSQueue
        JMSQueue jmsQueue = new JMSQueue();
        
        // Set queue properties
        jmsQueue.setName("testQueue");
        jmsQueue.setJndiName("java:/jms/queue/test");
        jmsQueue.setStatus("Running");
        jmsQueue.setMessageCount(10);
        jmsQueue.setConsumerCount(2);
        jmsQueue.setDurable(true);
        
        // Verify properties
        assertEquals("testQueue", jmsQueue.getName());
        assertEquals("java:/jms/queue/test", jmsQueue.getJndiName());
        assertEquals("Running", jmsQueue.getStatus());
        assertEquals(10, jmsQueue.getMessageCount());
        assertEquals(2, jmsQueue.getConsumerCount());
        assertTrue(jmsQueue.isDurable());
    }
    
    @Test
    public void testQueueStatus() {
        // Create a new JMSQueue
        JMSQueue jmsQueue = new JMSQueue();
        
        // Test status changes
        jmsQueue.setStatus("Running");
        assertEquals("Running", jmsQueue.getStatus());
        
        jmsQueue.setStatus("Paused");
        assertEquals("Paused", jmsQueue.getStatus());
        
        jmsQueue.setStatus("Stopped");
        assertEquals("Stopped", jmsQueue.getStatus());
    }
}
