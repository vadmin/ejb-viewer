package com.jbossmanager;

/**
 * Launcher class used as the main class in the executable JAR.
 * This is needed because JavaFX modules are loaded differently when packaged.
 */
public class MainLauncher {
    
    public static void main(String[] args) {
        Main.main(args);
    }
}
