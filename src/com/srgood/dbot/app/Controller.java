package com.srgood.dbot.app;

import com.srgood.dbot.BotMain;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

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
    private Button loadXMLButton;

    @FXML
    private TextArea xmlEditArea;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'sample.fxml'.";

        PowerButton.setOnAction(event -> com.srgood.dbot.BotMain.jda.shutdown());

        loadXMLButton.setOnAction(event -> {
            try {
                xmlEditArea.setText(BotMain.generateCleanXML());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void updateConsole() {
        console.setText(com.srgood.dbot.BotMain.out.toString());
    }



}