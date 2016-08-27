package com.srgood.dbot.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Created by dmanl on 8/21/2016.
 */
public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void init() {
        System.out.println("do what here?");
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
