package com.jbossmanager.controller;

import com.jbossmanager.model.EJBComponent;
import com.jbossmanager.model.JMSQueue;
import com.jbossmanager.model.ServerConnection;
import com.jbossmanager.service.ConnectionService;
import com.jbossmanager.service.EJBService;
import com.jbossmanager.service.JMSService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for the main view.
 */
public class MainController {
    
    // FXML injected fields
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button connectButton;
    @FXML private Label statusLabel;
    @FXML private TreeView<Object> resourcesTree;
    @FXML private Label detailsTitleLabel;
    @FXML private GridPane detailsGrid;
    @FXML private VBox operationsBox;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button restartButton;
    @FXML private Button deployButton;
    @FXML private Button undeployButton;
    @FXML private Label statusBarLabel;
    @FXML private Button refreshButton;
    
    // Services
    private ConnectionService connectionService;
    private JMSService jmsService;
    private EJBService ejbService;
    
    // State
    private Object selectedResource;
    
    /**
     * Initialize the controller.
     */
    @FXML
    public void initialize() {
        // Initialize services
        connectionService = new ConnectionService();
        jmsService = new JMSService(connectionService);
        ejbService = new EJBService(connectionService);
        
        // Set default values
        hostField.setText("localhost");
        portField.setText("9990");
        
        // Initialize tree
        TreeItem<Object> rootItem = new TreeItem<>("Server Resources");
        rootItem.setExpanded(true);
        resourcesTree.setRoot(rootItem);
        resourcesTree.setShowRoot(false);
        
        // Disable buttons initially
        refreshButton.setDisable(true);
        disableOperationButtons(true);
        
        // Update UI when connection status changes
        ServerConnection serverConnection = connectionService.getServerConnection();
        serverConnection.connectedProperty().addListener((obs, oldVal, newVal) -> {
            updateConnectionStatus(newVal);
        });
        
        serverConnection.connectionStatusProperty().addListener((obs, oldVal, newVal) -> {
            statusLabel.setText(newVal);
        });
    }
    
    /**
     * Handle connect button click.
     */
    @FXML
    private void handleConnect() {
        if (connectionService.isConnected()) {
            // Disconnect
            connectionService.disconnect();
            connectButton.setText("Connect");
            clearResources();
            disableOperationButtons(true);
            refreshButton.setDisable(true);
            updateStatusBar("Disconnected from server");
        } else {
            // Connect
            try {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                String username = usernameField.getText();
                String password = passwordField.getText();
                
                updateStatusBar("Connecting to server...");
                
                // Connect in background thread
                new Thread(() -> {
                    try {
                        boolean connected = connectionService.connect(host, port, username, password);
                        
                        Platform.runLater(() -> {
                            if (connected) {
                                connectButton.setText("Disconnect");
                                refreshButton.setDisable(false);
                                loadResources();
                                updateStatusBar("Connected to server");
                            } else {
                                updateStatusBar("Failed to connect: " + 
                                    connectionService.getServerConnection().getConnectionStatus());
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            updateStatusBar("Error connecting: " + e.getMessage());
                        });
                    }
                }).start();
            } catch (NumberFormatException e) {
                updateStatusBar("Invalid port number");
            }
        }
    }
    
    /**
     * Handle refresh button click.
     */
    @FXML
    private void handleRefresh() {
        if (connectionService.isConnected()) {
            updateStatusBar("Refreshing resources...");
            loadResources();
        }
    }
    
    /**
     * Handle resource selection in the tree view.
     */
    @FXML
    private void handleResourceSelection(MouseEvent event) {
        TreeItem<Object> selectedItem = resourcesTree.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Object value = selectedItem.getValue();
            selectedResource = value;
            
            if (value instanceof JMSQueue) {
                displayJMSQueueDetails((JMSQueue) value);
                enableJMSOperations(true);
                enableEJBOperations(false);
            } else if (value instanceof EJBComponent) {
                displayEJBDetails((EJBComponent) value);
                enableJMSOperations(false);
                enableEJBOperations(true);
            } else {
                clearDetails();
                disableOperationButtons(true);
            }
        } else {
            clearDetails();
            disableOperationButtons(true);
        }
    }
    
    /**
     * Handle start button click.
     */
    @FXML
    private void handleStart() {
        if (selectedResource instanceof JMSQueue) {
            JMSQueue queue = (JMSQueue) selectedResource;
            updateStatusBar("Starting queue " + queue.getName() + "...");
            
            new Thread(() -> {
                try {
                    boolean success = jmsService.startQueue(queue.getName());
                    
                    Platform.runLater(() -> {
                        if (success) {
                            updateStatusBar("Queue " + queue.getName() + " started successfully");
                            refreshJMSQueue(queue);
                        } else {
                            updateStatusBar("Failed to start queue " + queue.getName());
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatusBar("Error starting queue: " + e.getMessage());
                    });
                }
            }).start();
        }
    }
    
    /**
     * Handle stop button click.
     */
    @FXML
    private void handleStop() {
        if (selectedResource instanceof JMSQueue) {
            JMSQueue queue = (JMSQueue) selectedResource;
            updateStatusBar("Stopping queue " + queue.getName() + "...");
            
            new Thread(() -> {
                try {
                    boolean success = jmsService.stopQueue(queue.getName());
                    
                    Platform.runLater(() -> {
                        if (success) {
                            updateStatusBar("Queue " + queue.getName() + " stopped successfully");
                            refreshJMSQueue(queue);
                        } else {
                            updateStatusBar("Failed to stop queue " + queue.getName());
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatusBar("Error stopping queue: " + e.getMessage());
                    });
                }
            }).start();
        }
    }
    
    /**
     * Handle restart button click.
     */
    @FXML
    private void handleRestart() {
        if (selectedResource instanceof JMSQueue) {
            JMSQueue queue = (JMSQueue) selectedResource;
            updateStatusBar("Restarting queue " + queue.getName() + "...");
            
            new Thread(() -> {
                try {
                    boolean success = jmsService.restartQueue(queue.getName());
                    
                    Platform.runLater(() -> {
                        if (success) {
                            updateStatusBar("Queue " + queue.getName() + " restarted successfully");
                            refreshJMSQueue(queue);
                        } else {
                            updateStatusBar("Failed to restart queue " + queue.getName());
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatusBar("Error restarting queue: " + e.getMessage());
                    });
                }
            }).start();
        }
    }
    
    /**
     * Handle deploy button click.
     */
    @FXML
    private void handleDeploy() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select EJB Package");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("EJB Packages", "*.jar", "*.war", "*.ear"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            updateStatusBar("Deploying " + file.getName() + "...");
            
            new Thread(() -> {
                try {
                    boolean success = ejbService.deployEJB(file);
                    
                    Platform.runLater(() -> {
                        if (success) {
                            updateStatusBar(file.getName() + " deployed successfully");
                            loadResources();
                        } else {
                            updateStatusBar("Failed to deploy " + file.getName());
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatusBar("Error deploying: " + e.getMessage());
                    });
                }
            }).start();
        }
    }
    
    /**
     * Handle undeploy button click.
     */
    @FXML
    private void handleUndeploy() {
        if (selectedResource instanceof EJBComponent) {
            EJBComponent ejb = (EJBComponent) selectedResource;
            String deploymentName = ejb.getDeploymentName();
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Undeploy");
            alert.setHeaderText("Undeploy " + deploymentName);
            alert.setContentText("Are you sure you want to undeploy " + deploymentName + "?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                updateStatusBar("Undeploying " + deploymentName + "...");
                
                new Thread(() -> {
                    try {
                        boolean success = ejbService.undeployEJB(deploymentName);
                        
                        Platform.runLater(() -> {
                            if (success) {
                                updateStatusBar(deploymentName + " undeployed successfully");
                                loadResources();
                            } else {
                                updateStatusBar("Failed to undeploy " + deploymentName);
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            updateStatusBar("Error undeploying: " + e.getMessage());
                        });
                    }
                }).start();
            }
        }
    }
    
    /**
     * Load resources from the server.
     */
    private void loadResources() {
        if (!connectionService.isConnected()) {
            return;
        }
        
        updateStatusBar("Loading resources...");
        
        new Thread(() -> {
            try {
                // Get JMS queues
                List<JMSQueue> queues = jmsService.getQueues();
                
                // Get EJBs
                List<EJBComponent> ejbs = ejbService.getEJBs();
                
                Platform.runLater(() -> {
                    // Clear existing resources
                    clearResources();
                    
                    // Create root items
                    TreeItem<Object> rootItem = resourcesTree.getRoot();
                    TreeItem<Object> jmsQueuesItem = new TreeItem<>("JMS Queues");
                    TreeItem<Object> ejbsItem = new TreeItem<>("EJBs");
                    
                    // Add JMS queues
                    for (JMSQueue queue : queues) {
                        TreeItem<Object> queueItem = new TreeItem<>(queue);
                        jmsQueuesItem.getChildren().add(queueItem);
                    }
                    
                    // Add EJBs (grouped by deployment)
                    ejbs.stream()
                        .map(EJBComponent::getDeploymentName)
                        .distinct()
                        .forEach(deploymentName -> {
                            TreeItem<Object> deploymentItem = new TreeItem<>(deploymentName);
                            
                            ejbs.stream()
                                .filter(ejb -> ejb.getDeploymentName().equals(deploymentName))
                                .forEach(ejb -> {
                                    TreeItem<Object> ejbItem = new TreeItem<>(ejb);
                                    deploymentItem.getChildren().add(ejbItem);
                                });
                            
                            ejbsItem.getChildren().add(deploymentItem);
                        });
                    
                    // Add to root
                    rootItem.getChildren().add(jmsQueuesItem);
                    rootItem.getChildren().add(ejbsItem);
                    
                    // Expand root items
                    jmsQueuesItem.setExpanded(true);
                    ejbsItem.setExpanded(true);
                    
                    updateStatusBar("Resources loaded successfully");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    updateStatusBar("Error loading resources: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * Clear resources from the tree view.
     */
    private void clearResources() {
        TreeItem<Object> rootItem = resourcesTree.getRoot();
        rootItem.getChildren().clear();
        clearDetails();
    }
    
    /**
     * Display JMS queue details.
     */
    private void displayJMSQueueDetails(JMSQueue queue) {
        clearDetails();
        
        detailsTitleLabel.setText(queue.getName());
        
        // Add details to grid
        int row = 0;
        
        detailsGrid.add(new Label("Name:"), 0, row);
        detailsGrid.add(new Label(queue.getName()), 1, row++);
        
        detailsGrid.add(new Label("JNDI Name:"), 0, row);
        detailsGrid.add(new Label(queue.getJndiName()), 1, row++);
        
        detailsGrid.add(new Label("Status:"), 0, row);
        detailsGrid.add(new Label(queue.getStatus()), 1, row++);
        
        detailsGrid.add(new Label("Message Count:"), 0, row);
        detailsGrid.add(new Label(String.valueOf(queue.getMessageCount())), 1, row++);
        
        detailsGrid.add(new Label("Consumer Count:"), 0, row);
        detailsGrid.add(new Label(String.valueOf(queue.getConsumerCount())), 1, row++);
        
        detailsGrid.add(new Label("Durable:"), 0, row);
        detailsGrid.add(new Label(queue.isDurable() ? "Yes" : "No"), 1, row++);
    }
    
    /**
     * Display EJB details.
     */
    private void displayEJBDetails(EJBComponent ejb) {
        clearDetails();
        
        detailsTitleLabel.setText(ejb.getName());
        
        // Add details to grid
        int row = 0;
        
        detailsGrid.add(new Label("Name:"), 0, row);
        detailsGrid.add(new Label(ejb.getName()), 1, row++);
        
        detailsGrid.add(new Label("Type:"), 0, row);
        detailsGrid.add(new Label(ejb.getType()), 1, row++);
        
        detailsGrid.add(new Label("Deployment:"), 0, row);
        detailsGrid.add(new Label(ejb.getDeploymentName()), 1, row++);
        
        if (ejb.getJndiName() != null && !ejb.getJndiName().isEmpty()) {
            detailsGrid.add(new Label("JNDI Name:"), 0, row);
            detailsGrid.add(new Label(ejb.getJndiName()), 1, row++);
        }
        
        detailsGrid.add(new Label("Status:"), 0, row);
        detailsGrid.add(new Label(ejb.getStatus()), 1, row++);
        
        detailsGrid.add(new Label("Stateful:"), 0, row);
        detailsGrid.add(new Label(ejb.isStateful() ? "Yes" : "No"), 1, row++);
    }
    
    /**
     * Clear details panel.
     */
    private void clearDetails() {
        detailsTitleLabel.setText("Details");
        detailsGrid.getChildren().clear();
    }
    
    /**
     * Enable or disable JMS operation buttons.
     */
    private void enableJMSOperations(boolean enable) {
        startButton.setDisable(!enable);
        stopButton.setDisable(!enable);
        restartButton.setDisable(!enable);
        deployButton.setDisable(true);
        undeployButton.setDisable(true);
    }
    
    /**
     * Enable or disable EJB operation buttons.
     */
    private void enableEJBOperations(boolean enable) {
        startButton.setDisable(true);
        stopButton.setDisable(true);
        restartButton.setDisable(true);
        deployButton.setDisable(!enable);
        undeployButton.setDisable(!enable);
    }
    
    /**
     * Disable all operation buttons.
     */
    private void disableOperationButtons(boolean disable) {
        startButton.setDisable(disable);
        stopButton.setDisable(disable);
        restartButton.setDisable(disable);
        deployButton.setDisable(disable);
        undeployButton.setDisable(disable);
    }
    
    /**
     * Update connection status in the UI.
     */
    private void updateConnectionStatus(boolean connected) {
        if (connected) {
            statusLabel.setText("Connected");
            statusLabel.getStyleClass().remove("status-disconnected");
            statusLabel.getStyleClass().add("status-connected");
        } else {
            statusLabel.setText("Disconnected");
            statusLabel.getStyleClass().remove("status-connected");
            statusLabel.getStyleClass().add("status-disconnected");
        }
    }
    
    /**
     * Update status bar message.
     */
    private void updateStatusBar(String message) {
        statusBarLabel.setText(message);
    }
    
    /**
     * Refresh a JMS queue's status.
     */
    private void refreshJMSQueue(JMSQueue queue) {
        new Thread(() -> {
            try {
                jmsService.updateQueueStatus(queue);
                
                Platform.runLater(() -> {
                    if (selectedResource == queue) {
                        displayJMSQueueDetails(queue);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    updateStatusBar("Error refreshing queue: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * Get the stage from any control.
     */
    private Stage getStage() {
        return (Stage) hostField.getScene().getWindow();
    }
}
