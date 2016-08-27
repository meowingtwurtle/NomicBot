package com.srgood.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import net.dv8tion.jda.JDA;

import java.lang.invoke.LambdaConversionException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane x1;

    @FXML
    private Button PowerButton;

    @FXML
    private TextArea console;

    @FXML
    private Button UpdateButton;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'sample.fxml'.";


        PowerButton.setOnAction(event -> BotMain.jda.shutdown());
        UpdateButton.setOnAction(event -> console.setText(BotMain.out.toString()));




    }




}
