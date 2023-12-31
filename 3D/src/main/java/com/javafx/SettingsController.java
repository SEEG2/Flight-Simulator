package com.javafx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingsController {
    private String cameFromMenu;
    private int world_id;
    private List<TextField> keyBindingFields;
    private List<TextField> keyBindingFieldsFree;
    private String[] listOfKeys = {
            "Key-FlyForward",
            "Key-TurnLeft",
            "Key-TurnRight",
            "Key-Decelerate",
            "Key-RotateUp",
            "Key-RotateDown",
            "Key-SettingsMenu"
    };

    private String[] listOfKeysFree = {
            "KeyF-Forward",
            "KeyF-Left",
            "KeyF-Right",
            "KeyF-Backward",
            "KeyF-Down",
            "KeyF-Up",
            "KeyF-RotateLeft",
            "KeyF-RotateRight",
            "KeyF-RotateUp",
            "KeyF-RotateDown",
            "KeyF-SettingsMenu"
    };

    @FXML
    private VBox keyBindingsVBoxFree;

    @FXML
    private VBox keyBindingsVBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane homeIcon;

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField worldNameTextField;

    @FXML
    private Button saveWorldName;

    @FXML
    private Button saveAndExitButton;

    @FXML
    private Button saveGameButton;

    @FXML
    private RadioButton freeFlyModeToggle;

    public SettingsController(String cameFromMenu, int world_id) {
        this.cameFromMenu = cameFromMenu;
        this.world_id = world_id;
    }

    @FXML
    private void resetDefaultKeyBindings() {
        SettingsHandler.setDefaultKeyBindings();
        getKeyBindingsFromSettings();
    }

    @FXML
    private void resetDefaultKeyBindingsFree() {
        SettingsHandler.setDefaultKeyBindingsFree();
        getKeyBindingsFromSettingsFree();
    }

    @FXML
    private void saveAndGoToStartMenu() {

    }

    @FXML
    private void saveGame() {

    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }

    @FXML
    private void saveWorldName() {
        if (worldNameTextField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Name");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid name for this world");
            alert.showAndWait();
        } else {
            SettingsHandler.saveGameDataWorldName(world_id, worldNameTextField.getText());
        }
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
    private void setFreeFlyMode() {
        freeFlyModeToggle.setSelected(freeFlyModeToggle.isSelected());
        SettingsHandler.setIsInFreeFlyMode(world_id, freeFlyModeToggle.isSelected());
    }

    @FXML
    private void goBackScene() throws IOException {
        switch (cameFromMenu) {
            case "startPage":
                StartPage.setScene("startMenu");
                break;
            case "flightSimulator":
                GameData gameData = SettingsHandler.readGameData(world_id);
                FlightSimulatorGame game = new FlightSimulatorGame(gameData);
                StartPage.setScene(game.startGame());
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

    public void initialize() {
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
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
                freeFlyModeToggle.setDisable(true);
                break;
            case "flightSimulator":
                homeIcon.setVisible(true);
                break;
            case "savedWorldsMenu":
                homeIcon.setVisible(true);
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
                freeFlyModeToggle.setDisable(true);
                break;
            case "createWorldMenu":
                homeIcon.setVisible(true);
                worldNameTextField.setDisable(true);
                saveWorldName.setDisable(true);
                saveAndExitButton.setDisable(true);
                saveGameButton.setDisable(true);
                freeFlyModeToggle.setDisable(true);
            default:
                break;
        }

        GameData data = SettingsHandler.readGameData(world_id);

        freeFlyModeToggle.setSelected(data.getIsInFreeFlyMode());
        seedTextField.setText(String.valueOf(data.getSeed()));
        worldNameTextField.setText(data.getWorldName());

        keyBindingFields = new ArrayList<>();
        keyBindingFieldsFree = new ArrayList<>();
        getKeyBindingsFromSettings();
        getKeyBindingsFromSettingsFree();
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

    private void getKeyBindingsFromSettingsFree() {
        int z = 0;
        for (Node node : keyBindingsVBoxFree.getChildren()) {
            if (node instanceof TextField) {
                // set saved values to the textfields
                TextField textField = (TextField) node;
                textField.setText(SettingsHandler.getKeyBindingValue(listOfKeysFree[z]));
                keyBindingFieldsFree.add(textField);
                z++;
            }
        }
    }
}
