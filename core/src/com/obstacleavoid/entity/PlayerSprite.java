package com.obstacleavoid.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.obstacleavoid.config.GameConfig;

public class PlayerSprite extends GameSpriteBase {

    // == constructor ==
    public PlayerSprite(TextureRegion region) {
        super(region, GameConfig.PLAYER_BOUNDS_RADIUS);
        bounds = new Circle(getX(), getY(), GameConfig.PLAYER_BOUNDS_RADIUS);
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);
    }

}
