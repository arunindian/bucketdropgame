package com.simplegames.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.simplegames.drop.constants.GameConstants;

import java.util.Iterator;

public class Drop extends ApplicationAdapter {

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private Rectangle bucket;
	private Array<Rectangle> rainDrops;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private long lastRainDropTime;

	Texture img;
	
	@Override
	public void create () {
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		dropSound =  Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic =  Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		bucket = new Rectangle();
		bucket.x = GameConstants.WIDTH /2 - GameConstants.BUCKET_WIDTH/2;
		bucket.y = 20;

		bucket.width = 64;
		bucket.height = 64;

		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);

		rainDrops = new Array<Rectangle>();
		spawnRainDrop();

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0.2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle rainDrop : rainDrops) {
			batch.draw(dropImage,rainDrop.x,rainDrop.y);
		}
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(touchPos);
			bucket.x =  (int)(touchPos.x/2)- GameConstants.BUCKET_WIDTH/2;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > GameConstants.WIDTH - GameConstants.BUCKET_WIDTH) bucket.x = GameConstants.WIDTH - GameConstants.BUCKET_WIDTH;

		if(TimeUtils.nanoTime() - lastRainDropTime > 1000000000) spawnRainDrop();

		Iterator<Rectangle> iter = rainDrops.iterator();

		while(iter.hasNext()) {
			Rectangle rainDrop = iter.next();
			rainDrop.y -= 1;
			if(rainDrop.y + GameConstants.BUCKET_HEIGHT < 0) iter.remove();
			if(rainDrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}

	}

	private void spawnRainDrop() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(GameConstants.WIDTH -GameConstants.BUCKET_WIDTH);
		rainDrop.y = 480;
		rainDrop.width = GameConstants.BUCKET_WIDTH;
		rainDrop.height = GameConstants.BUCKET_HEIGHT;
		rainDrops.add(rainDrop);
		lastRainDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
