package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.roque.rockslide.MainGame;

public class Rock extends EntitySprite{

	Texture tex;
	public boolean hit;
	
	
	public Rock(Body body) {
		
		super(body);

		tex = MainGame.res.getTexture("rock");

		TextureRegion[] sprites = TextureRegion.split(tex, 44, 44)[0];
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
	public void playImpact(){
		TextureRegion[][] sprites = TextureRegion.split(tex, 44, 44);
		if(hit){
			animation.setFrames(sprites[1], 1/ 12f, true, sprites[1].length - 1);
		}else{
			animation.setFrames(sprites[0], 1/ 12f, true, 0);
		}
	}
}
