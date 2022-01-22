package com.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.controllers.MainWindowController;
import javafx.application.Platform;


/**
 * CheckInternet
 */
public class CheckInternet extends Thread {
    private static final MainWindowController mainWindow = MainWindowController.getInstance();
    public void run(){
        super.run();
        this.setName("Thread connectio status");
        while(true){
            try {
                try {
                    URL url = new URL("http://www.google.com");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // System.out.println(this.getName() + ": true");
                    Platform.runLater(()->{
                        mainWindow.setConnectionLabel(true);
                    });
                } catch (MalformedURLException e) { 
                   //  System.out.println(this.getName() + ": false");
                    Platform.runLater(()->{
                        mainWindow.setConnectionLabel(false);
                    });
                } catch (IOException e) {
                    // System.out.println(this.getName() + ": false");
                    Platform.runLater(()->{
                        mainWindow.setConnectionLabel(false);
                    });
                }
            Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }  
    }
}
