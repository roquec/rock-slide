package com.roque.rockslide.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.roque.rockslide.MainGame;

public class TitleSlide extends EntitySprite{

	public TitleSlide(Body body) {
		super(body);

		Texture tex = MainGame.res.getTexture("title_slide");

		TextureRegion[] sprites = TextureRegion.split(tex, 128, 30)[0];
		animation.setFrames(sprites, 1 / 12f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}

}
