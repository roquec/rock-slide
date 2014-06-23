package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.roque.rockslide.handlers.Animation;
import com.roque.rockslide.handlers.B2DVars;

public abstract class Sprite {

	protected Animation animation;
	protected float width;
	protected float height;
	
	public static final boolean LEFT = false;
	public static final boolean RIGHT = true;
	private boolean dir;
	
	public Sprite() {
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt) {
		animation.update(dt);
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(animation.getFrame(), (getPosition().x * B2DVars.PPM - width / 2), (int) (getPosition().y * B2DVars.PPM - height / 2));
		sb.end();
	}
	
	public abstract Vector2 getPosition();
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
	public void setDirection(boolean dir){ 
		if(dir != this.dir){
			animation.flipX();
			this.dir = dir;
		}
	}
	
	public Animation getAnimation(){
		return animation;
	}
}
