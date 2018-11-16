package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.ObstacleSprite;
import com.obstacleavoid.entity.PlayerSprite;
import com.obstacleavoid.util.GdxUtils;
import com.obstacleavoid.util.ViewportUtils;
import com.obstacleavoid.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    // == Attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    private DebugCameraController debugCameraController;

    private final GameController controller;
    private final AssetManager assetManager;
    private final SpriteBatch batch;

    // == Constructor ==
    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController controller){
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    // == init ==
    private void init(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        // create Debug camera Controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

    }

    // == public methods ==
    public void render(float delta){
        batch.totalRenderCalls = 0;

        // not wrapping inside alive, for using it after game stops
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if(Gdx.input.isTouched() && !controller.isGameOver()){
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));

            System.out.println("screenTouch = " + screenTouch);
            System.out.println("worldTouch = " + worldTouch);

            PlayerSprite player = controller.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);
        }

        // clear screen
        GdxUtils.clearScreen();

        renderGamePlay();

        // render UI / HUD
        renderUI();

        // render debug graphics
        renderDebug();

        System.out.println("totalRenserCalls = " + batch.totalRenderCalls);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    // == private methods ==

    private void renderGamePlay(){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // draw background
//        Background background = controller.getBackground();
//        batch.draw(backgroundRegion,
//                background.getX(), background.getY(),
//                background.getWidth(), background.getHeight());

        // draw player
        PlayerSprite player = controller.getPlayer();
        player.draw(batch);
//        batch.draw(player,
//                player.getX(), player.getY(),
//                player.getWidth(), player.getHeight()
//        );

        // draw obstacles
        for(ObstacleSprite obstacle : controller.getObstacles()){
            obstacle.draw(batch);
//            batch.draw(obstacleRegion,
//                    obstacle.getX(), obstacle.getY(),
//                    obstacle.getWidth(), obstacle.getHeight()
//            );
        }


        batch.end();
    }

    private void renderUI(){
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES " + controller.getLives();

        layout.setText(font, livesText);

        font.draw(batch, livesText,
                20,
                GameConfig.HUD_HEIGHT - layout.height);

        String scoreText = "SCORE = " + controller.getDisplayedScore();
        layout.setText(font, scoreText);

        font.draw(batch, scoreText,
                GameConfig.HUD_WIDTH - layout.width - 20,
                GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug(){
        viewport.apply();

        Color oldColor = renderer.getColor().cpy();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        renderer.setColor(oldColor);

        ViewportUtils.drawGrid(viewport,renderer);
    }

    private void drawDebug(){
        renderer.setColor(Color.RED);
        PlayerSprite player = controller.getPlayer();
        player.drawDebug(renderer);
//
        Array<ObstacleSprite> obstacles = controller.getObstacles();
//
        for(ObstacleSprite obstacle : obstacles){
            obstacle.drawDebug(renderer);
        }
    }
}
