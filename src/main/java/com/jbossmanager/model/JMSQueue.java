package com.jbossmanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Model class representing a JMS Queue on the JBoss/WildFly server.
 */
public class JMSQueue {
    
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final StringProperty jndiName = new SimpleStringProperty(this, "jndiName", "");
    private final StringProperty status = new SimpleStringProperty(this, "status", "Unknown");
    private final IntegerProperty messageCount = new SimpleIntegerProperty(this, "messageCount", 0);
    private final IntegerProperty consumerCount = new SimpleIntegerProperty(this, "consumerCount", 0);
    private final BooleanProperty durable = new SimpleBooleanProperty(this, "durable", false);
    
    public JMSQueue() {
    }
    
    public JMSQueue(String name, String jndiName) {
        setName(name);
        setJndiName(jndiName);
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
    
    // Message Count property
    public IntegerProperty messageCountProperty() {
        return messageCount;
    }
    
    public int getMessageCount() {
        return messageCount.get();
    }
    
    public void setMessageCount(int messageCount) {
        this.messageCount.set(messageCount);
    }
    
    // Consumer Count property
    public IntegerProperty consumerCountProperty() {
        return consumerCount;
    }
    
    public int getConsumerCount() {
        return consumerCount.get();
    }
    
    public void setConsumerCount(int consumerCount) {
        this.consumerCount.set(consumerCount);
    }
    
    // Durable property
    public BooleanProperty durableProperty() {
        return durable;
    }
    
    public boolean isDurable() {
        return durable.get();
    }
    
    public void setDurable(boolean durable) {
        this.durable.set(durable);
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
