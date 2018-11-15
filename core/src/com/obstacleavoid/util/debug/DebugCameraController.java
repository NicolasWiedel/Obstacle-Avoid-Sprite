package com.obstacleavoid.util.debug;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class DebugCameraController {

    // == Constants ==
    private static final Logger log =new Logger(DebugCameraController.class.getName(), Logger.DEBUG);

    // == Attributes ==
    private Vector2 position = new Vector2();
    private Vector2 startPosition = new Vector2();
    private float zoom = 1.0f;
    private DebugCameraConfig config;

    // == Constructor ==
    public DebugCameraController(){
        config = new DebugCameraConfig();
        log.info("cameraConfig= " + config);
    }

    // == public methods ==
    public void setStartPosition(float x, float y){
        startPosition.set(x, y);
        position.set(x, y);
    }

    public void applyTo(OrthographicCamera camera){
        camera.position.set(position, 0);
        camera.zoom = zoom;
        camera.update();
    }

    public void handleDebugInput(float delta){
        if(Gdx.app.getType() != Application.ApplicationType.Desktop){
            return;
        }

        float moveSpeed = config.getMoveSpeed() * delta;
        float zoomSpeed = config.getZoomSpeed() * delta;

        // move controls
        if(config.isLeftPressed()){
            moveLeft(moveSpeed);
        }else if(config.isRightPressed()){
            moveRight(moveSpeed);
        }else if(config.isUpPressed()){
            moveUp(moveSpeed);
        }else if(config.isDownPressed()){
            moveDown(moveSpeed);
        }

        // Zoom controls
        if(config.isZoomInPressed()){
            zoomIn(zoomSpeed);
        }else if(config.isZoomOutPressed()) {
            zoomOut(zoomSpeed);
        }

        // reset controls
        if(config.isResetPressed()) {
            reset();
        }

        if(config.isLogPressed()){
            logDebug();
        }
    }

    // == private methods ==
    private void setPosition(float x, float y){
        position.set(x, y);
    }

    private void moveCamera(float xSpeed, float ySpeed){
        setPosition(position.x + xSpeed, position.y + ySpeed);
    }

    private void setZoom(float value){
        zoom = MathUtils.clamp(value, config.getMaxZoomIn(), config.getMaxZoomOut());
    }

    private void moveLeft(float speed){
        moveCamera(-speed,0);
    }
    private void moveRight(float speed){
        moveCamera(speed, 0);
    }
    private void moveUp(float speed){
        moveCamera(0, speed);
    }
    private void moveDown(float speed){
        moveCamera(0, -speed);
    }



    private void zoomIn(float zoomSpeed){
        setZoom(zoom + zoomSpeed);
    }

    private void zoomOut(float zoomSpeed){
        setZoom(zoom - zoomSpeed);
    }

    private void reset(){
        position.set(startPosition);
        setZoom(1.0f);
    }

    private void logDebug(){
        log.debug("position = " + position + " zoom = " + zoom);
    }
}
