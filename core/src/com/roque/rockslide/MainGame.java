package com.roque.rockslide;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roque.rockslide.camera.BoundedCamera;
import com.roque.rockslide.handlers.RSInput;
import com.roque.rockslide.handlers.RSInputProcessor;
import com.roque.rockslide.handlers.ResourceManager;
import com.roque.rockslide.screen.ScreenManager;

public class MainGame extends ApplicationAdapter {
	
	public static final String TITLE = "Rock Slide";
	public static final int V_WIDTH = 240;
	public static final int V_HEIGHT = 320;
	public static final int SCALE = 2;
	
	private static int SOUND;
	
	public static boolean DEBUG = false;
	
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch spriteBatch;
	private BoundedCamera camera; //Follow player
	private OrthographicCamera hudCamera; //HUD, buttons...
	private ScreenManager screenManager;
	
	private Viewport viewport; //To resize
	
	public static ResourceManager res;
	
	//Getters
	public SpriteBatch getSpriteBatch() {return spriteBatch;}
	public BoundedCamera getCamera() {return camera;}
	public OrthographicCamera getHudCamera() {return hudCamera;}
	public ScreenManager getScreenManager() {return screenManager;}

	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(new RSInputProcessor(this));
		
		initializeResources();
		
		//Music
		res.getMusic("song").setLooping(true);
		res.getMusic("song").setVolume(0.05f);
		soundON();
		
		//Initialize cameras
		camera = new BoundedCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		viewport = new FitViewport(V_WIDTH, V_HEIGHT, hudCamera);
		
		spriteBatch = new SpriteBatch();
		screenManager = new ScreenManager(this);
	}

	@Override
	public void render () {
		
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		
		screenManager.update(STEP);
		screenManager.render();
		RSInput.update();
		
	}
	
	@Override
	public void dispose() {
		res.removeAll();
	}
	
	@Override
	public void resize (int width, int height) {
		viewport.update(width, height);
		screenManager.resize(width, height);
	}
	
	@Override
	public void pause () {}
	
	@Override
	public void resume () {}
	
	private void initializeResources() {
		res = new ResourceManager();
		
		//IMAGES
		res.loadTexture("images/player.png", "player");
		res.loadTexture("images/rock.png", "rock");
		res.loadTexture("images/play.png", "play");
		res.loadTexture("images/walls.png", "walls");
		res.loadTexture("images/ground.png", "ground");
		res.loadTexture("images/sky.png", "sky");
		res.loadTexture("images/clouds.png", "clouds");
		res.loadTexture("images/title_rock.png", "title_rock");
		res.loadTexture("images/title_slide.png", "title_slide");
		res.loadTexture("images/pad.png", "pad");
		res.loadTexture("images/pause.png", "pause");
		res.loadTexture("images/sound.png", "sound");
		
		//Music
		res.loadMusic("music/RS_song.mp3", "song");
		
		//Sound Effects
		res.loadSound("sounds/rock_smash.mp3", "rock_smash");
	}
	
	public Vector2 unproject(int x, int y){
		return viewport.unproject(new Vector2(x,y));
	}
	
	public static void soundON(){
		res.getMusic("song").play();
		SOUND = 1;
	}
	
	public static void soundOFF(){
		res.getMusic("song").pause();
		SOUND = 0;
	}
	
	public static int getSound(){
		return SOUND;
	}
}
