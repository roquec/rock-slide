package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.screen.Play;

public class Player extends EntitySprite{
	
	private boolean dead = false;
	public int points = 0;
	Play game;

	public Player(Body body, Play game) {
		
		super(body);
		
		this.game = game;
		
		Texture tex = MainGame.res.getTexture("player");

		TextureRegion[] sprites = TextureRegion.split(tex, 18, 30)[0];
		animation.setFrames(sprites, 1 / 10f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void kill(){
		this.dead = true;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(float points) {
		int p = this.points;
		
		this.points = (int)MathUtils.round(points);
		
		if((this.points / 10) - (p / 10) == 1)
			game.drawText(this.points + " meters!", Color.GREEN);
		
	}

	public void update(float dt) {
		super.update(dt);
		
		float h = getPosition().y * 10 - 9;
		
		if(h > points)
			setPoints(h);
	}
}
