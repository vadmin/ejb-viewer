package com.jbossmanager.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EJBComponent model.
 */
public class EJBComponentTest {
    
    @Test
    public void testDefaultConstructor() {
        EJBComponent ejb = new EJBComponent();
        
        // Verify default values
        assertEquals("", ejb.getName());
        assertEquals("", ejb.getType());
        assertEquals("", ejb.getDeploymentName());
        assertEquals("", ejb.getJndiName());
        assertEquals("Unknown", ejb.getStatus());
        assertFalse(ejb.isStateful());
    }
    
    @Test
    public void testParameterizedConstructor() {
        EJBComponent ejb = new EJBComponent("testEJB", "Stateless Session Bean", "test.ear");
        
        // Verify constructor sets values correctly
        assertEquals("testEJB", ejb.getName());
        assertEquals("Stateless Session Bean", ejb.getType());
        assertEquals("test.ear", ejb.getDeploymentName());
        assertEquals("", ejb.getJndiName());
        assertEquals("Unknown", ejb.getStatus());
        assertFalse(ejb.isStateful());
    }
    
    @Test
    public void testSettersAndGetters() {
        EJBComponent ejb = new EJBComponent();
        
        // Set values
        ejb.setName("testEJB");
        ejb.setType("Stateful Session Bean");
        ejb.setDeploymentName("test.ear");
        ejb.setJndiName("java:global/test/TestEJB");
        ejb.setStatus("Deployed");
        ejb.setStateful(true);
        
        // Verify values
        assertEquals("testEJB", ejb.getName());
        assertEquals("Stateful Session Bean", ejb.getType());
        assertEquals("test.ear", ejb.getDeploymentName());
        assertEquals("java:global/test/TestEJB", ejb.getJndiName());
        assertEquals("Deployed", ejb.getStatus());
        assertTrue(ejb.isStateful());
    }
    
    @Test
    public void testToString() {
        EJBComponent ejb = new EJBComponent("testEJB", "Stateless Session Bean", "test.ear");
        
        // Verify toString returns the name
        assertEquals("testEJB", ejb.toString());
    }
    
    @Test
    public void testProperties() {
        EJBComponent ejb = new EJBComponent();
        
        // Verify properties are not null
        assertNotNull(ejb.nameProperty());
        assertNotNull(ejb.typeProperty());
        assertNotNull(ejb.deploymentNameProperty());
        assertNotNull(ejb.jndiNameProperty());
        assertNotNull(ejb.statusProperty());
        assertNotNull(ejb.statefulProperty());
        
        // Test property binding
        ejb.nameProperty().set("newName");
        assertEquals("newName", ejb.getName());
        
        ejb.typeProperty().set("newType");
        assertEquals("newType", ejb.getType());
        
        ejb.deploymentNameProperty().set("newDeployment");
        assertEquals("newDeployment", ejb.getDeploymentName());
        
        ejb.jndiNameProperty().set("newJndi");
        assertEquals("newJndi", ejb.getJndiName());
        
        ejb.statusProperty().set("newStatus");
        assertEquals("newStatus", ejb.getStatus());
        
        ejb.statefulProperty().set(true);
        assertTrue(ejb.isStateful());
    }
}
