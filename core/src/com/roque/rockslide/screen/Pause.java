package com.roque.rockslide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.entity.HUDSprite;

public class Pause extends Screen{

	private HUDSprite resume;
	private BitmapFont font;
	
	protected Pause(ScreenManager sm) {
		super(sm);

		resume = new HUDSprite(120, 25, TextureRegion.split(MainGame.res.getTexture("play"), 90, 30)[0], 1 / 6f);
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
	}

	@Override
	public void handleInput() {
		if(resume.isClicked())
			sm.setScreen(ScreenManager.PLAY);
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		resume.update(dt);
	}

	@Override
	public void render() {
		sb.setProjectionMatrix(hudCam.combined);
		
		sb.begin();
		font.setScale(1.2f);
		
		write("GAME\nPAUSED", 260, new Color(0, 1, 0, 1));
		
		sb.end();
		
		resume.render(sb);
	}

	private void write(String text, int y, Color color) {
		font.setColor(color);
		font.drawMultiLine(sb, text, 120 - font.getMultiLineBounds(text).width / 2, y, font.getMultiLineBounds(text).width, BitmapFont.HAlignment.CENTER);
	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
