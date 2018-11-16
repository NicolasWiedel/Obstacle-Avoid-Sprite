package com.obstacleavoid.common;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.entity.PlayerSprite;

public class EntityFactory {

    // == attributes ==
    private final AssetManager assetManager;

    private TextureAtlas gamePlayAtlas;

    // == constructor ==
    public EntityFactory(AssetManager assetManager){
        this.assetManager = assetManager;
        init();
    }

    // == init ==
    private void init(){
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
    }

    // == public methods ==
    public PlayerSprite createPlayer(){
        TextureRegion playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        return new PlayerSprite(playerRegion);
    }
}
