package com.roque.rockslide.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.camera.BoundedCamera;

public abstract class Screen {

	protected ScreenManager sm;
	protected MainGame game;
	
	protected SpriteBatch sb;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;
	
	protected Screen(ScreenManager sm) {
		this.sm = sm;
		game = sm.game();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		hudCam = game.getHudCamera();
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
	public abstract void resize(int width, int height);

}
