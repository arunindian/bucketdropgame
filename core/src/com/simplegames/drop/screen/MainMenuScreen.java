package com.simplegames.drop.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.simplegames.drop.DropGame;
import com.simplegames.drop.constants.GameConstants;

/**
 * Created by ardixit on 01/09/18.
 */

public class MainMenuScreen implements Screen {
    private final DropGame game;
    private OrthographicCamera camera;

    public MainMenuScreen(DropGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.WIDTH,GameConstants.HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(),"Welcome to Drop!",100,150);
        game.getFont().draw(game.getBatch(),"Tap anywhere to begin!",100,100);
        game.getBatch().end();

        if(Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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
