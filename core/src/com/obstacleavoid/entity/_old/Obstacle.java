package com.obstacleavoid.entity._old;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.GameConfig;

@Deprecated
public class Obstacle extends GameObjectBase implements Pool.Poolable {

    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

    public Obstacle() {

        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public void update(){
        setY(getY() - ySpeed);
    }

    public boolean isPlayerColliding(Player player) {
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
