package com.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {
    private String cameFromMenu;
    private List<TextField> keyBindingFields;
    private String[] listOfKeys = {
            "Key-FlyForward",
            "Key-TurnLeft",
            "Key-TurnRight",
            "Key-FlyUp",
            "Key-FlyDown",
            "Key-RotateLeft",
            "Key-RotateRight",
            "Key-RotateUp",
            "Key-RotateDown",
            "Key-SettingsMenu"
    };

    @FXML
    private VBox keyBindingsVBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane homeIcon;

    public SettingsController(String cameFromMenu) {
        this.cameFromMenu = cameFromMenu;
    }

    @FXML
    private void resetDefaultKeyBindings() {
        SettingsHandler.setDefaultKeyBindings();
        getKeyBindingsFromSettings();
    }

    @FXML
    private void exitGame() {
        StartPage.closeStage();
    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }

    @FXML
    private void onKeyPressedKeyBinding(KeyEvent event) {
        String newKey = event.getCode().toString();
        TextField sourceTextField = (TextField) event.getSource();
        sourceTextField.setText(newKey);

        SettingsHandler.updateKeyBindingValue(sourceTextField.getId(), newKey);
    }

    @FXML
    private void onKeyReleasedKeyBinding() {
        anchorPane.requestFocus();
    }

    @FXML
    private void goToStartMenu() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void goBackScene() throws IOException {
        switch (cameFromMenu) {
            case "startPage":
                StartPage.setScene("startMenu");
                break;
            case "flightSimulator":
                FlightSimulatorGame game = new FlightSimulatorGame();
                StartPage.setScene(game.startGame(2222)); // TODO seed
                break;
            case "savedWorldsMenu":
                StartPage.setScene("savedWorldsMenu");
                break;
            case "createWorldMenu":
                StartPage.setScene("createWorldMenu");
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            // if ESC is pressed in settings
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                Node focusNode = anchorPane.getScene().getFocusOwner();
                // if ESC in keybindings textfield
                if (!(focusNode instanceof TextField)) {
                    try {
                        goBackScene();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        switch (cameFromMenu) {
            case "startPage":
                homeIcon.setVisible(false);
                break;
            case "flightSimulator":
                homeIcon.setVisible(true);
                break;
            case "savedWorldsMenu":
                homeIcon.setVisible(true);
                break;
            case "createWorldMenu":
                homeIcon.setVisible(true);
            default:
                break;
        }

        // seedTextField.setText(SettingsHandler.getProperty("seed")); // TODO seed

        keyBindingFields = new ArrayList<>();
        getKeyBindingsFromSettings();
    }

    private void getKeyBindingsFromSettings() {
        int i = 0;
        for (Node node : keyBindingsVBox.getChildren()) {
            if (node instanceof TextField) {
                // set saved values to the textfields
                TextField textField = (TextField) node;
                textField.setText(SettingsHandler.getKeyBindingValue(listOfKeys[i]));
                keyBindingFields.add(textField);
                i++;
            }
        }
    }
}
