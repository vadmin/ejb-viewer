package com.jbossmanager.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JMSQueue model.
 */
public class JMSQueueTest {
    
    @Test
    public void testDefaultConstructor() {
        JMSQueue queue = new JMSQueue();
        
        // Verify default values
        assertEquals("", queue.getName());
        assertEquals("", queue.getJndiName());
        assertEquals("Unknown", queue.getStatus());
        assertEquals(0, queue.getMessageCount());
        assertEquals(0, queue.getConsumerCount());
        assertFalse(queue.isDurable());
    }
    
    @Test
    public void testParameterizedConstructor() {
        JMSQueue queue = new JMSQueue("testQueue", "java:/jms/queue/test");
        
        // Verify constructor sets values correctly
        assertEquals("testQueue", queue.getName());
        assertEquals("java:/jms/queue/test", queue.getJndiName());
        assertEquals("Unknown", queue.getStatus());
        assertEquals(0, queue.getMessageCount());
        assertEquals(0, queue.getConsumerCount());
        assertFalse(queue.isDurable());
    }
    
    @Test
    public void testSettersAndGetters() {
        JMSQueue queue = new JMSQueue();
        
        // Set values
        queue.setName("testQueue");
        queue.setJndiName("java:/jms/queue/test");
        queue.setStatus("Running");
        queue.setMessageCount(10);
        queue.setConsumerCount(2);
        queue.setDurable(true);
        
        // Verify values
        assertEquals("testQueue", queue.getName());
        assertEquals("java:/jms/queue/test", queue.getJndiName());
        assertEquals("Running", queue.getStatus());
        assertEquals(10, queue.getMessageCount());
        assertEquals(2, queue.getConsumerCount());
        assertTrue(queue.isDurable());
    }
    
    @Test
    public void testToString() {
        JMSQueue queue = new JMSQueue("testQueue", "java:/jms/queue/test");
        
        // Verify toString returns the name
        assertEquals("testQueue", queue.toString());
    }
    
    @Test
    public void testProperties() {
        JMSQueue queue = new JMSQueue();
        
        // Verify properties are not null
        assertNotNull(queue.nameProperty());
        assertNotNull(queue.jndiNameProperty());
        assertNotNull(queue.statusProperty());
        assertNotNull(queue.messageCountProperty());
        assertNotNull(queue.consumerCountProperty());
        assertNotNull(queue.durableProperty());
        
        // Test property binding
        queue.nameProperty().set("newName");
        assertEquals("newName", queue.getName());
        
        queue.jndiNameProperty().set("newJndi");
        assertEquals("newJndi", queue.getJndiName());
        
        queue.statusProperty().set("newStatus");
        assertEquals("newStatus", queue.getStatus());
        
        queue.messageCountProperty().set(20);
        assertEquals(20, queue.getMessageCount());
        
        queue.consumerCountProperty().set(5);
        assertEquals(5, queue.getConsumerCount());
        
        queue.durableProperty().set(true);
        assertTrue(queue.isDurable());
    }
}
