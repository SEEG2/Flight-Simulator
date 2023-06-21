package com.flight_sim;

import javafx.animation.TranslateTransition;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static java.lang.Math.floor;

public class CameraHandler {
    private static final double TRANSLATION_AMOUNT = 10.0;
    private static final double ROTATION_AMOUNT = 2.0;

    private Camera camera;
    private Rotate cameraRotation;
    private double cameraTranslateX;
    private double cameraTranslateY;
    private double cameraTranslateZ;

    public void setupCamera(Scene scene) {
        camera = new PerspectiveCamera(true);

        cameraRotation = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().add(cameraRotation);

        // create scene
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        // set up the camera
        camera.setNearClip(20);
        camera.setFarClip(2000);
        cameraTranslateX = floor((double) TerrainGeneration.BOXES_PER_ROW * TerrainGeneration.BOX_SIZE / 2);
        cameraTranslateY = -250;
        cameraTranslateZ = 50;

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateY(cameraTranslateY);
        camera.setTranslateZ(cameraTranslateZ);
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                cameraTranslateX -= TRANSLATION_AMOUNT;
                break;
            case D:
                cameraTranslateX += TRANSLATION_AMOUNT;
                break;
            case W:
                cameraTranslateY -= TRANSLATION_AMOUNT;
                break;
            case S:
                cameraTranslateY += TRANSLATION_AMOUNT;
                break;
            case LEFT:
                cameraRotation.setAngle(cameraRotation.getAngle() - ROTATION_AMOUNT);
                break;
            case RIGHT:
                cameraRotation.setAngle(cameraRotation.getAngle() + ROTATION_AMOUNT);
                break;
            default:
                break;
        }

        camera.setTranslateX(cameraTranslateX);
        camera.setTranslateY(cameraTranslateY);
    }

    public void handleAnimationTick() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), camera);
        // move forward temporary TODO
        cameraTranslateZ = camera.getTranslateZ() + 5;
        translateTransition.play();

        camera.setTranslateZ(cameraTranslateZ);
    }
}
