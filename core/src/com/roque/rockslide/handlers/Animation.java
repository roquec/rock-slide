package com.roque.rockslide.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	
	private boolean once;
	private int end;
	private int timesPlayed;
	
	public Animation() {}
	
	public Animation(TextureRegion[] frames) {
		this(frames, 1 / 12f);
	}
	
	public Animation(TextureRegion[] frames, float delay) {
		this(frames, delay, false, 0);
	}
	
	public Animation(TextureRegion[] frames, float delay, boolean once, int end) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		this.once = once;
		this.end = end;
	}
	
	public void setDelay(float f) { delay = f; }
	public void setCurrentFrame(int i) { if(i < frames.length) currentFrame = i; }
	public void setFrames(TextureRegion[] frames) {
		setFrames(frames, 1 / 12f);
	}
	
	public void setFrames(TextureRegion[] frames, float delay) {
		setFrames(frames, delay, false, 0);
	}
	
	public void setFrames(TextureRegion[] frames, float delay, boolean once, int end) {
		this.frames = frames;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
		this.delay = delay;
		this.once = once;
		this.end = end;
	}
	
	public void update(float dt) {		
		if(!(once && hasPlayedOnce())){
			if(delay <= 0) return;
			time += dt;
			while(time >= delay) {
				step();
			}
		}
	}
	
	private void step() {
		time -= delay;
		currentFrame++;
		if(currentFrame == frames.length) {
			if(once)
				currentFrame = end;
			else
				currentFrame = 0;
			timesPlayed++;
		}
	}
	
	public void flipX(){
		for(int i = 0; i < frames.length; i++)
			frames[i].flip(true, false);
	}
	
	public TextureRegion getFrame() { return frames[currentFrame]; }
	public int getTimesPlayed() { return timesPlayed; }
	public boolean hasPlayedOnce() { return timesPlayed > 0; }
	
}
