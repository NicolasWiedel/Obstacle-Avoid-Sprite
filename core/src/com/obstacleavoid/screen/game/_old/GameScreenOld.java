package com.obstacleavoid.screen.game._old;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.screen.game.GameController;
import com.obstacleavoid.screen.game.GameRenderer;
import com.obstacleavoid.screen.menu.MenuScreen;

@Deprecated
public class GameScreenOld implements Screen {

    private final ObstacleAvoidGame game;
    private AssetManager assetManager;

    private GameControllerOld controller;
    private GameRendererOld renderer;

    public GameScreenOld(ObstacleAvoidGame game){
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameControllerOld(game);
        renderer = new GameRendererOld(game.getBatch(), assetManager, controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);

        if(controller.isGameOver()){
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        renderer.dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
