package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roque.rockslide.handlers.B2DVars;
import com.roque.rockslide.handlers.RSInput;

/**
 * Sprite with a fixed position
 */
public class HUDSprite extends Sprite{
	
	//Center
	private float x;
	private float y;
	
	private boolean pressed;
	private boolean down;
	private int bwidth;
	private int bheight;
	
	public HUDSprite(int x, int y, TextureRegion[] sprites, float delay) {
		this(x,y,sprites, delay, sprites[0].getRegionWidth(), sprites[0].getRegionHeight());
	}
	
	public HUDSprite(int x, int y, TextureRegion[] sprites, float delay, int bwidth, int bheight) {
		super();
		this.x = x;
		this.y = y;
		
		animation.setFrames(sprites, delay);
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
		this.bwidth = bwidth;
		this.bheight = bheight;
	}
	
	public Vector2 getPosition() { return new Vector2(x / B2DVars.PPM, y / B2DVars.PPM); }
	
	public void update(float dt) {
		
		super.update(dt);
		
		if( RSInput.x > x - bwidth / 2 && RSInput.x < x + bwidth / 2 &&
			RSInput.y > y - bheight / 2 && RSInput.y < y + bheight / 2) {
			pressed = RSInput.isPressed();
			down = RSInput.isDown();
		}else{
			pressed = false;
			down = false;
		}
	}
	
	public void setSprites(TextureRegion[] sprites, float delay){
		animation.setFrames(sprites, delay);
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
	public boolean isClicked() { return pressed; }
	public boolean isDown() { return down;}
	
	
}
