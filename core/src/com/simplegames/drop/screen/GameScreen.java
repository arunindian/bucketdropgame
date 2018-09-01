package com.simplegames.drop.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.simplegames.drop.DropGame;
import com.simplegames.drop.constants.GameConstants;

import java.util.Iterator;


/**
 * Created by ardixit on 01/09/18.
 */

public class GameScreen implements Screen {
    private final DropGame game;

    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Array<Rectangle> rainDrops;
    private long lastDropTime;
    private int dropsGathered;

    public GameScreen(final DropGame game) {
        this.game = game;

        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.play();
        rainMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.WIDTH,GameConstants.HEIGHT);

        bucket = new Rectangle();
        bucket.x = GameConstants.WIDTH/2 - GameConstants.BUCKET_WIDTH/2;
        bucket.y = GameConstants.BUCKET_FLOOR_HEIGHT;
        bucket.width = GameConstants.BUCKET_WIDTH;
        bucket.height = GameConstants.BUCKET_HEIGHT;

        rainDrops = new Array<Rectangle>();



    }

    private void spawnRainDrop() {
        Rectangle rainDrop = new Rectangle();
        rainDrop.x = MathUtils.random(0,GameConstants.WIDTH-GameConstants.BUCKET_WIDTH);
        rainDrop.y = GameConstants.HEIGHT;
        rainDrop.width = GameConstants.RAINDROP_WIDTH;
        rainDrop.height = GameConstants.RAINDROP_HEIGHT;
        rainDrops.add(rainDrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getFont().draw(game.getBatch(),"Drops Gathered "+dropsGathered,0,GameConstants.HEIGHT);
        game.getBatch().draw(bucketImage,bucket.x,bucket.y,bucket.width,bucket.height);
        for(Rectangle rainDrop : rainDrops) {
            game.getBatch().draw(dropImage,rainDrop.x,rainDrop.y);
        }
        game.getBatch().end();

        if(Gdx.input.isTouched()) {
            Vector3 touch3 = new Vector3();
            touch3.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch3);
            bucket.x = touch3.x - GameConstants.BUCKET_HEIGHT/2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 200*(Gdx.graphics.getDeltaTime());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 200*(Gdx.graphics.getDeltaTime());
        }

        if(bucket.x < 0) {
            bucket.x = 0;
        } else if(bucket.x > GameConstants.WIDTH-GameConstants.BUCKET_WIDTH) {
            bucket.x = GameConstants.WIDTH - GameConstants.BUCKET_WIDTH;
        }

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRainDrop();
        }

        Iterator<Rectangle> iter = rainDrops.iterator();
        while(iter.hasNext()) {
            Rectangle rainDrop = iter.next();
            rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(rainDrop.y+64 < 0) {
                iter.remove();
            }
            if(rainDrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
