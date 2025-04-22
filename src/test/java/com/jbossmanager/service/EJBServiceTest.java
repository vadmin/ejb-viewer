package com.jbossmanager.service;

import com.jbossmanager.model.EJBComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EJBService.
 * Note: These tests don't actually connect to a server, they just test the behavior of the service.
 */
public class EJBServiceTest {
    
    @Test
    public void testEJBProperties() {
        // Create a new EJBComponent
        EJBComponent ejbComponent = new EJBComponent();
        
        // Set EJB properties
        ejbComponent.setName("TestEJB");
        ejbComponent.setType("Stateless Session Bean");
        ejbComponent.setDeploymentName("test.ear");
        ejbComponent.setJndiName("java:global/test/TestEJB");
        ejbComponent.setStatus("Deployed");
        ejbComponent.setStateful(false);
        
        // Verify properties
        assertEquals("TestEJB", ejbComponent.getName());
        assertEquals("Stateless Session Bean", ejbComponent.getType());
        assertEquals("test.ear", ejbComponent.getDeploymentName());
        assertEquals("java:global/test/TestEJB", ejbComponent.getJndiName());
        assertEquals("Deployed", ejbComponent.getStatus());
        assertFalse(ejbComponent.isStateful());
    }
    
    @Test
    public void testEJBStatus() {
        // Create a new EJBComponent
        EJBComponent ejbComponent = new EJBComponent();
        
        // Test status changes
        ejbComponent.setStatus("Deployed");
        assertEquals("Deployed", ejbComponent.getStatus());
        
        ejbComponent.setStatus("Undeployed");
        assertEquals("Undeployed", ejbComponent.getStatus());
        
        ejbComponent.setStatus("Failed");
        assertEquals("Failed", ejbComponent.getStatus());
    }
    
    @Test
    public void testEJBTypes() {
        // Create a new EJBComponent
        EJBComponent ejbComponent = new EJBComponent();
        
        // Test different EJB types
        ejbComponent.setType("Stateless Session Bean");
        assertEquals("Stateless Session Bean", ejbComponent.getType());
        assertFalse(ejbComponent.isStateful());
        
        ejbComponent.setType("Stateful Session Bean");
        assertEquals("Stateful Session Bean", ejbComponent.getType());
        ejbComponent.setStateful(true);
        assertTrue(ejbComponent.isStateful());
        
        ejbComponent.setType("Message-Driven Bean");
        assertEquals("Message-Driven Bean", ejbComponent.getType());
        
        ejbComponent.setType("Singleton Bean");
        assertEquals("Singleton Bean", ejbComponent.getType());
    }
}
