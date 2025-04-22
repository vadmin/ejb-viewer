package com.jbossmanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Model class representing an EJB component on the JBoss/WildFly server.
 */
public class EJBComponent {
    
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final StringProperty type = new SimpleStringProperty(this, "type", "");
    private final StringProperty deploymentName = new SimpleStringProperty(this, "deploymentName", "");
    private final StringProperty jndiName = new SimpleStringProperty(this, "jndiName", "");
    private final StringProperty status = new SimpleStringProperty(this, "status", "Unknown");
    private final BooleanProperty stateful = new SimpleBooleanProperty(this, "stateful", false);
    
    public EJBComponent() {
    }
    
    public EJBComponent(String name, String type, String deploymentName) {
        setName(name);
        setType(type);
        setDeploymentName(deploymentName);
    }
    
    // Name property
    public StringProperty nameProperty() {
        return name;
    }
    
    public String getName() {
        return name.get();
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    // Type property
    public StringProperty typeProperty() {
        return type;
    }
    
    public String getType() {
        return type.get();
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    // Deployment Name property
    public StringProperty deploymentNameProperty() {
        return deploymentName;
    }
    
    public String getDeploymentName() {
        return deploymentName.get();
    }
    
    public void setDeploymentName(String deploymentName) {
        this.deploymentName.set(deploymentName);
    }
    
    // JNDI Name property
    public StringProperty jndiNameProperty() {
        return jndiName;
    }
    
    public String getJndiName() {
        return jndiName.get();
    }
    
    public void setJndiName(String jndiName) {
        this.jndiName.set(jndiName);
    }
    
    // Status property
    public StringProperty statusProperty() {
        return status;
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public void setStatus(String status) {
        this.status.set(status);
    }
    
    // Stateful property
    public BooleanProperty statefulProperty() {
        return stateful;
    }
    
    public boolean isStateful() {
        return stateful.get();
    }
    
    public void setStateful(boolean stateful) {
        this.stateful.set(stateful);
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
