package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.common.EntityFactory;
import com.obstacleavoid.common.GameManager;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.ObstacleSprite;
import com.obstacleavoid.entity.PlayerSprite;
import com.obstacleavoid.entity._old.Background;

public class GameController {

    // == Constants ==
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    // == Attributes
    private PlayerSprite player;
    private Array<ObstacleSprite> obstacles = new Array<ObstacleSprite>();
    private Background background;
    private float obstacleTime;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayedScore;
    private Sound hit;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;
    private final EntityFactory factory;

    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2 ;
    private final float startPlayerY = 1 - GameConfig.PLAYER_SIZE / 2;

    // == Constructor
    public GameController(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        factory = new EntityFactory(assetManager);
        init();
    }

    // init
    private void init(){
        // create player
        player = factory.createPlayer();

        // position player
        player.setPosition(startPlayerX, startPlayerY);

        // create background
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        hit = assetManager.get(AssetDescriptors.HIT_SOUND);
    }

    // == public methods ==
    public void update(float delta){
        if(isGameOver()){
            return;
        }

        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayedScore(delta);

        if(isPlayerCollidingWithObstacle()){
            log.debug("Collision detected!");
            lives--;

            if(isGameOver()){
                log.debug("Game over!!!");
                GameManager.INSTANCE.updateHighScore(score);
            }else{
                restart();
            }
        }
    }

    public PlayerSprite getPlayer() {
        return player;
    }

    public Array<ObstacleSprite> getObstacles() {
        return obstacles;
    }

    public Background getBackground(){ return background; }

    public int getLives() {
        return lives;
    }

    public int getDisplayedScore() {
        return displayedScore;
    }

    public boolean isGameOver(){
        return lives <= 0;
    }

    // == private methods ==
    private void restart(){
        factory.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    private boolean isPlayerCollidingWithObstacle(){
        for(ObstacleSprite obstacle : obstacles){
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)){
                hit.play();
                return true;
            }
        }
        return false;
    }

    private void updatePlayer(){
        float xSpeed = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        player.setX(player.getX() +xSpeed);

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld(){
        float playerX = MathUtils.clamp(player.getX(),
                0,
                GameConfig.WORLD_WIDTH - player.getWidth());

        player.setPosition(playerX, player.getY());
    }

    private void updateObstacles(float delta){
        for(ObstacleSprite obstacle : obstacles){
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta){
        obstacleTime += delta;

        if(obstacleTime >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE ;

            float obstacleX = MathUtils.random(max, min);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            ObstacleSprite obstacle = factory.obtain();

            DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            obstacleTime = 0;
        }
    }

    private void removePassedObstacles(){
        if(obstacles.size > 0){
            ObstacleSprite first = obstacles.first();

            float minObstacleY = -GameConfig.OBSTACLE_SIZE;

            if(first.getY() < minObstacleY){
                obstacles.removeValue(first, true);
                factory.free(first);
            }
        }
    }

    private void updateScore(float delta){
        scoreTimer += delta;

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayedScore(float delta){
        if(displayedScore < score){
            displayedScore = Math.min(
                    score,
                    displayedScore + (int)(60 * delta)
            );
        }
    }
}
