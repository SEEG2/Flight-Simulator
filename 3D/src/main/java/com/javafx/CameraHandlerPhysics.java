package com.javafx;

import java.io.IOException;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

public class CameraHandlerPhysics extends CameraHandler {
    private static final double ROTATION_AMOUNT = 2.0;

    private double rotX = 180;
    private double rotY = 0;
    private double rotZ = 0;
    private Physics physics = new Physics(0);
    private Camera camera;
    private Rotate cameraRotationY;
    private Rotate cameraRotationX;
    private Rotate cameraRotationZ;
    private double coordinateX;
    private double altitude;
    private double coordinateZ;
    private byte curve = 0;

    // key bindings
    private String keyFlyForward = SettingsHandler.getKeyBindingValue("Key-FlyForward");
    private String keyDecelerate = SettingsHandler.getKeyBindingValue("Key-Decelerate");
    private String keyTurnLeft = SettingsHandler.getKeyBindingValue("Key-TurnLeft");
    private String keyTurnRight = SettingsHandler.getKeyBindingValue("Key-TurnRight");
    private String keyRotateDown = SettingsHandler.getKeyBindingValue("Key-RotateDown");
    private String keyRotateUp = SettingsHandler.getKeyBindingValue("Key-RotateUp");
    private String keySettingsMenu = SettingsHandler.getKeyBindingValue("Key-SettingsMenu");

    private int world_id;
    private Scene scene;

    public CameraHandlerPhysics(int world_id, Scene scene) {
        this.world_id = world_id;
        this.scene = scene;
    }

    public Camera setupCamera(double position_x, double position_y, double position_z, double rotation_x,
            double rotation_y, double rotation_z) {
        camera = new PerspectiveCamera(true);

        cameraRotationX = new Rotate(0, Rotate.X_AXIS);
        cameraRotationY = new Rotate(0, Rotate.Y_AXIS);
        cameraRotationZ = new Rotate(0, Rotate.Z_AXIS);
        camera.getTransforms().addAll(cameraRotationX, cameraRotationY, cameraRotationZ);

        // render distance
        camera.setNearClip(10);
        camera.setFarClip(2000);

        // rotations
        rotX = rotation_x;
        rotY = rotation_y;
        rotZ = rotation_z;

        // positions
        coordinateX = position_x;
        altitude = position_y;
        coordinateZ = position_z;

        camera.setTranslateX(coordinateX);
        camera.setTranslateY(altitude);
        camera.setTranslateZ(coordinateZ);

        return camera;
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.valueOf(this.keyFlyForward))) {
            physics.accelerate();
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnLeft))) {
            curve = -1;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyTurnRight))) {
            curve = 1;
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyDecelerate))) {
            physics.decelerate();
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateDown))) {
            physics.turnUp(-ROTATION_AMOUNT);
        } else if (event.getCode().equals(KeyCode.valueOf(this.keyRotateUp))) {
            physics.turnUp(ROTATION_AMOUNT);
        } else if (event.getCode().equals(KeyCode.valueOf(this.keySettingsMenu))) {
            try {
                SettingsHandler.saveScreenshot(world_id, scene);

                StartPage.setSettingsScene("settingsMenu", "flightSimulator", world_id);

                SettingsHandler.saveGameData(world_id, camera.getTranslateX(), camera.getTranslateY(),
                        camera.getTranslateZ(), physics.getAngleDown(), physics.getAngle(), rotZ, false);
                System.out.println("Game saved!");

            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO Load physics data after save

        } else {
        }

    }

    public void handleAnimationTick(long timeBetweenTickInNano) {
        if (curve != 0) {
            cameraRotationZ.setAngle(physics.flyCurve(altitude, curve));
            curve = 0;
        } else {
            cameraRotationZ.setAngle(0);
            physics.sleep(altitude);
        }
        camera.setTranslateX(camera.getTranslateX() + physics.getDeltaX());
        camera.setTranslateZ(camera.getTranslateZ() + physics.getDeltaZ());
        camera.setTranslateY(camera.getTranslateY() + physics.getDeltaY());
        cameraRotationY.setAngle(physics.getAngle());
        cameraRotationX.setAngle(physics.getAngleDown());
    }

    public double getCoordinateX() {
        return this.coordinateX;
    }

    public double getCoordinateZ() {
        return this.coordinateZ;
    }

    public double getAltitude() {
        return this.altitude;
    }
}