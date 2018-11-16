package com.obstacleavoid.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.GameConfig;


public class ObstacleSprite extends GameSpriteBase implements Pool.Poolable {

    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

    public ObstacleSprite(TextureRegion region) {

        super(region, GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public void update(){
        setY(getY() - ySpeed);
    }

    public boolean isPlayerColliding(PlayerSprite player) {
        Circle playerBound = player.getBounds();
        boolean overlaps = Intersector.overlaps(playerBound, getBounds());

//        if(overlaps){
//            hit = true;
//        }
//       Better way
        hit = overlaps;

        return overlaps;
    }

    public boolean isNotHit(){
        return !hit;
    }


    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }


    @Override
    public void reset() {
        hit = false;

    }
}
