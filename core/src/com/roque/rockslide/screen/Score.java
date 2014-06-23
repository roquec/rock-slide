package com.roque.rockslide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.entity.HUDSprite;

public class Score extends Screen{

	private ShapeRenderer sr;
	private Color color;
	private HUDSprite playAgain;
	
	private BitmapFont font;
	
	private boolean hud;
	
	protected Score(ScreenManager sm) {
		super(sm);

		sr = new ShapeRenderer();
		color = new Color(0, 0, 0, 0);
		
		playAgain = new HUDSprite(120, 25, TextureRegion.split(MainGame.res.getTexture("play"), 90, 30)[0], 1 / 6f);
		
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
	}

	@Override
	public void handleInput() {
		if(playAgain.isClicked())
			sm.restart();
	}

	@Override
	public void update(float dt) {
		if(color.a >= 0.7)
			hud = true;
		else
			color.set(color.r, color.g, color.b, (float) (color.a + 0.005));
		
		if(hud){
			handleInput();
			
			playAgain.update(dt);
		}
	}

	@Override
	public void render() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		sr.setProjectionMatrix(hudCam.combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(color);
		sr.rect(0, 0, MainGame.V_WIDTH, MainGame.V_HEIGHT);
		sr.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		
		//Points
		if(hud){
			sb.setProjectionMatrix(hudCam.combined);
			
			sb.begin();
			font.setScale(1.2f);
			
			write("GAME\nOVER", 260, new Color(1, 0, 0, 1));
			
			
			font.setScale(0.8f);
			write("Score:", 150, new Color(1, 1, 1, 1));
			write(sm.getPlayScreen().getPlayer().getPoints() + " meters", 130, new Color(0, 1, 0, 1));
			sb.end();
			
			playAgain.render(sb);
		}
	}

	private void write(String text, int y, Color color) {
		font.setColor(color);
		font.drawMultiLine(sb, text, 120 - font.getMultiLineBounds(text).width / 2, y, font.getMultiLineBounds(text).width, BitmapFont.HAlignment.CENTER);
	}

	@Override
	public void dispose() {}

	@Override
	public void resize(int width, int height) {}

}
