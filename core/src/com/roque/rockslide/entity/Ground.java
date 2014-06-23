package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.roque.rockslide.MainGame;

public class Ground extends EntitySprite{

	public Ground(Body body) {
		
		super(body);

		Texture tex = MainGame.res.getTexture("ground");

		TextureRegion[] sprites = TextureRegion.split(tex, 240, 85)[0];
		animation.setFrames(sprites, 1 / 12f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
	
}
