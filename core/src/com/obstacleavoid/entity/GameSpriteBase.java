package com.obstacleavoid.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public abstract class GameSpriteBase extends Sprite {

    // == attributes ==
    protected Circle bounds;

    // == constructor ==
    public GameSpriteBase(TextureRegion region, float boundsRadius){
        super(region);
        bounds = new Circle(getX(), getY(), boundsRadius);
    }

    // == public methods ==
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateBounds();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateBounds();
    }

    public void updateBounds(){
        if(bounds == null){
            return;
        }
        float halfWidth = getWidth() / 2;
        float halfHeight = getHeight() / 2;
        bounds.setPosition(getX() + halfWidth, getY() + halfHeight);
    }

    // == public methods ==
    public void drawDebug(ShapeRenderer renderer){
        renderer.x(bounds.x, bounds.y, 0.1f);
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }

    public Circle getBounds() {
        return bounds;
    }
}
