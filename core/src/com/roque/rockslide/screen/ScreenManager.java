package com.roque.rockslide.screen;

import com.roque.rockslide.MainGame;

public class ScreenManager {
	
	public static final int TITLE = 786862;
	public static final int PLAY = 243453;
	public static final int PAUSE = 984651;
	public static final int SCORE = 135469;
	
	private MainGame game;
	
	private Play playScreen;
	private Screen menuScreen;
	
	public ScreenManager(MainGame game) {
		this.game = game;
		setScreen(TITLE);
	}
	
	public MainGame game() { return game; }
	
	public void update(float dt) {
		playScreen.update(dt);
		if(menuScreen != null)
			menuScreen.update(dt);
	}
	
	
	public void render() {
		playScreen.render();
		if(menuScreen != null)
			menuScreen.render();
	}
	
	public void resize(int width, int height){
		playScreen.resize(width, height);
		if(menuScreen != null)
			menuScreen.resize(width, height);
	}
	
	public void setScreen(int screen) {
		switch (screen) {
		case TITLE:
			menuScreen = new Title(this);
			playScreen = new Play(this);
			playScreen.setRunning(false);
			playScreen.setAnimations(true);
			playScreen.setInput(false);
			playScreen.setHUD(false);
			break;
		case PAUSE:
			menuScreen = new Pause(this);
			playScreen.pause();
			break;
		case SCORE:
			menuScreen = new Score(this);
			playScreen.setRunning(true);
			playScreen.setAnimations(true);
			playScreen.setInput(false);
			playScreen.setHUD(false);
			break;
		case PLAY:
			menuScreen = null;
			playScreen.resume();
			break;
		}
	}
	
	public void restart(){
		this.playScreen = new Play(this);
		setScreen(PLAY);
	}

	public Play getPlayScreen() {
		return playScreen;
	}
}
