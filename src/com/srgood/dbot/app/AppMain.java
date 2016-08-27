package com.srgood.dbot.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by dmanl on 8/21/2016.
 */
public class AppMain extends Application {
    private boolean firstTime;
    private TrayIcon trayIcon;

    public static void main(String[] args) {
        System.err.print(SystemTray.isSupported());
        launch(args);
    }



    public void start(Stage stage) {
        stage.setTitle("Reasons Console");



        FlowPane root = new FlowPane();

        Scene scene = new Scene(root,300,200);

        Label label = new Label("Test Label");

        stage.setScene(scene);

        root.getChildren().addAll(label);


        stage.show();


    }


}
