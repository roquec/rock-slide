package com.roque.rockslide.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roque.rockslide.screen.Play;

public class Text extends BitmapFont{
	
	private boolean fadeIn = false;
	private int wait = 30;
	private boolean fadeOut = false;
	
	private float delay;
	private float time;
	private int times;
	private String text;
	private Play game;
	
	private int x;
	private int y;
	
	public Text(String text, int x, int y, Play game , float r, float g, float b){
		super(Gdx.files.internal("fonts/font.fnt"));
		setScale(0.8f);
		this.text = text;
		this.x =  x - (int)getMultiLineBounds(text).width / 2;
		this.y = y;
		this.delay = 1/12f;
		this.time = 0;
		this.times = 0;
		this.game = game;
		this.setColor(r, g, b, 0);
		fadeIn = true;
	}
	
	public void update(float dt){
		
		time += dt;
		
		if(time >= delay)
			step();
	}
	
	private void step() {
		time = 0;
		
		
		if(fadeIn)
			fadeIn();
		else if(fadeOut)
			fadeOut();
		else{
			times++;
			if(times >= wait)
				fadeOut = true;
		}
		
	}
	
	private void fadeOut(){
		Color c = this.getColor();
		this.setColor(c.r, c.g, c.b, c.a - 0.05f);
		if(this.getColor().a <= 0.05)
			game.disposeText(this);
		this.y = y + 1;
	}
	
	private void fadeIn(){
		Color c = this.getColor();
		this.setColor(c.r, c.g, c.b, c.a + 0.1f);
		this.y = y + 1;
		
		if(getColor().a >= 0.9){
			fadeIn = false;
		}
			
	}
	
	public void render(SpriteBatch sb){
		this.drawMultiLine(sb, text, x, y, getMultiLineBounds(text).width, BitmapFont.HAlignment.CENTER);
	}

	public String getText() {
		return text;
	}
	
	

}
