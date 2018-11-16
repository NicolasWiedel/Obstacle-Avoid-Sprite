package com.obstacleavoid.common;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.entity.ObstacleSprite;
import com.obstacleavoid.entity.PlayerSprite;

public class EntityFactory {

    // == attributes ==
    private final AssetManager assetManager;

    private TextureAtlas gamePlayAtlas;
    private TextureRegion obstacleRegion;
    private Pool<ObstacleSprite>obstacleSpritePool;

    // == constructor ==
    public EntityFactory(AssetManager assetManager){
        this.assetManager = assetManager;
        init();
    }

    // == init ==
    private void init(){

        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);

        obstacleSpritePool = new Pool<ObstacleSprite>(40) {
            @Override
            protected ObstacleSprite newObject() {
                return new ObstacleSprite(obstacleRegion);
            }
        };
    }

    // == public methods ==
    public PlayerSprite createPlayer(){
        TextureRegion playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        return new PlayerSprite(playerRegion);
    }

    public ObstacleSprite obtain(){
        ObstacleSprite obstacle = obstacleSpritePool.obtain();
        obstacle.setRegion(obstacleRegion);
        return obstacle;
    }

    public void free(ObstacleSprite obstacle){
        obstacleSpritePool.free(obstacle);
    }

    public void freeAll(Array<ObstacleSprite> obstacles){
        obstacleSpritePool.freeAll(obstacles);
    }
}
