package com.jbossmanager.service;

import com.jbossmanager.model.EJBComponent;
import org.jboss.dmr.ModelNode;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing EJB components on JBoss/WildFly servers.
 */
public class EJBService {
    
    private final ConnectionService connectionService;
    
    public EJBService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }
    
    /**
     * Get all EJB components from the server.
     * 
     * @return List of EJB components
     * @throws Exception if an error occurs
     */
    public List<EJBComponent> getEJBs() throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        List<EJBComponent> ejbs = new ArrayList<>();
        
        // First, get all deployments
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-resources");
        op.get("child-type").set("deployment");
        op.get("address").setEmptyList();
        
        ModelNode result = connectionService.getClient().execute(op);
        if ("success".equals(result.get("outcome").asString())) {
            ModelNode deploymentsNode = result.get("result");
            for (String deploymentName : deploymentsNode.keys()) {
                // For each deployment, get EJBs
                ejbs.addAll(getEJBsForDeployment(deploymentName));
            }
        } else {
            throw new Exception("Failed to get deployments: " + result.get("failure-description").asString());
        }
        
        return ejbs;
    }
    
    /**
     * Get all EJB components for a specific deployment.
     * 
     * @param deploymentName The name of the deployment
     * @return List of EJB components in the deployment
     * @throws Exception if an error occurs
     */
    private List<EJBComponent> getEJBsForDeployment(String deploymentName) throws Exception {
        List<EJBComponent> ejbs = new ArrayList<>();
        
        // Query the EJB subsystem for this deployment
        ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set(true);
        
        // Set the address to the EJB3 subsystem for this deployment
        ModelNode address = op.get("address");
        address.add("deployment", deploymentName);
        address.add("subsystem", "ejb3");
        
        ModelNode result = connectionService.getClient().execute(op);
        if ("success".equals(result.get("outcome").asString())) {
            ModelNode ejbSubsystem = result.get("result");
            
            // Process Stateless Session Beans
            if (ejbSubsystem.hasDefined("stateless-session-bean")) {
                ModelNode statelessBeans = ejbSubsystem.get("stateless-session-bean");
                for (String beanName : statelessBeans.keys()) {
                    ModelNode bean = statelessBeans.get(beanName);
                    EJBComponent ejb = new EJBComponent();
                    ejb.setName(beanName);
                    ejb.setType("Stateless Session Bean");
                    ejb.setDeploymentName(deploymentName);
                    ejb.setStateful(false);
                    
                    // Get JNDI names
                    if (bean.hasDefined("jndi-names") && bean.get("jndi-names").asList().size() > 0) {
                        ejb.setJndiName(bean.get("jndi-names").asList().get(0).asString());
                    }
                    
                    // Set status
                    ejb.setStatus("Deployed");
                    
                    ejbs.add(ejb);
                }
            }
            
            // Process Stateful Session Beans
            if (ejbSubsystem.hasDefined("stateful-session-bean")) {
                ModelNode statefulBeans = ejbSubsystem.get("stateful-session-bean");
                for (String beanName : statefulBeans.keys()) {
                    ModelNode bean = statefulBeans.get(beanName);
                    EJBComponent ejb = new EJBComponent();
                    ejb.setName(beanName);
                    ejb.setType("Stateful Session Bean");
                    ejb.setDeploymentName(deploymentName);
                    ejb.setStateful(true);
                    
                    // Get JNDI names
                    if (bean.hasDefined("jndi-names") && bean.get("jndi-names").asList().size() > 0) {
                        ejb.setJndiName(bean.get("jndi-names").asList().get(0).asString());
                    }
                    
                    // Set status
                    ejb.setStatus("Deployed");
                    
                    ejbs.add(ejb);
                }
            }
            
            // Process Message-Driven Beans
            if (ejbSubsystem.hasDefined("message-driven-bean")) {
                ModelNode mdbBeans = ejbSubsystem.get("message-driven-bean");
                for (String beanName : mdbBeans.keys()) {
                    ModelNode bean = mdbBeans.get(beanName); // Get the bean node
                    // Check if the bean is a message-driven bean
                    EJBComponent ejb = new EJBComponent();
                    ejb.setName(beanName);
                    ejb.setType("Message-Driven Bean");
                    ejb.setDeploymentName(deploymentName);
                    ejb.setStateful(false);
                    
                    // Set status
                    ejb.setStatus("Deployed");
                    
                    ejbs.add(ejb);
                }
            }
            
            // Process Singleton Beans
            if (ejbSubsystem.hasDefined("singleton-bean")) {
                ModelNode singletonBeans = ejbSubsystem.get("singleton-bean");
                for (String beanName : singletonBeans.keys()) {
                    ModelNode bean = singletonBeans.get(beanName);
                    EJBComponent ejb = new EJBComponent();
                    ejb.setName(beanName);
                    ejb.setType("Singleton Bean");
                    ejb.setDeploymentName(deploymentName);
                    ejb.setStateful(false);
                    
                    // Get JNDI names
                    if (bean.hasDefined("jndi-names") && bean.get("jndi-names").asList().size() > 0) {
                        ejb.setJndiName(bean.get("jndi-names").asList().get(0).asString());
                    }
                    
                    // Set status
                    ejb.setStatus("Deployed");
                    
                    ejbs.add(ejb);
                }
            }
        }
        
        return ejbs;
    }
    
    /**
     * Deploy an EJB package to the server.
     * 
     * @param ejbFile The EJB package file (JAR, WAR, EAR)
     * @return true if successful, false otherwise
     * @throws Exception if an error occurs
     */
    public boolean deployEJB(File ejbFile) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Read file bytes
        byte[] bytes = Files.readAllBytes(ejbFile.toPath());
        
        // Create deployment operation
        ModelNode op = new ModelNode();
        op.get("operation").set("deploy");
        op.get("address").setEmptyList();
        op.get("content").add().get("bytes").set(bytes);
        op.get("name").set(ejbFile.getName());
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        return "success".equals(result.get("outcome").asString());
    }
    
    /**
     * Undeploy an EJB package from the server.
     * 
     * @param deploymentName The name of the deployment to undeploy
     * @return true if successful, false otherwise
     * @throws Exception if an error occurs
     */
    public boolean undeployEJB(String deploymentName) throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        // Create undeploy operation
        ModelNode op = new ModelNode();
        op.get("operation").set("undeploy");
        op.get("address").setEmptyList();
        op.get("name").set(deploymentName);
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        return "success".equals(result.get("outcome").asString());
    }
    
    /**
     * Get all deployments from the server.
     * 
     * @return List of deployment names
     * @throws Exception if an error occurs
     */
    public List<String> getDeployments() throws Exception {
        if (!connectionService.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
        
        List<String> deployments = new ArrayList<>();
        
        // Create operation to read all deployments
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set("deployment");
        op.get("address").setEmptyList();
        
        // Execute the operation
        ModelNode result = connectionService.getClient().execute(op);
        if ("success".equals(result.get("outcome").asString())) {
            List<ModelNode> deploymentNodes = result.get("result").asList();
            for (ModelNode deploymentNode : deploymentNodes) {
                deployments.add(deploymentNode.asString());
            }
        } else {
            throw new Exception("Failed to get deployments: " + result.get("failure-description").asString());
        }
        
        return deployments;
    }
}
