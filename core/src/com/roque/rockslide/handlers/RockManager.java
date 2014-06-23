package com.roque.rockslide.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.roque.rockslide.entity.Rock;
import com.roque.rockslide.screen.Play;

public class RockManager {
	
	public static final float INITIAL_DIFFICULTY = 2; //Easy
	
	public int[] xPositions = new int[8]; 
	
	
	private Array<Rock> rocks;
	private Play game;
	
	private float minDelay;
	private float maxDelay;
	private float time;
	
	private float delay;
	
	public RockManager(Play game){
		this(game, INITIAL_DIFFICULTY);
	}
	
	public RockManager(Play game, float dificulty) {
		
		this.game = game;
		this.time = 0;
		setDificulty(dificulty);
		
		for(int i = 0; i < xPositions.length; i++)
			xPositions[i] = 17 + 12 + 26 * i;
		
		rocks = new Array<Rock>();
	}
	
	
	public void update(float dt){
		time += dt;
		if(time >= delay) {
			spawn(MathUtils.random(0, 7));
		}
	}

	public void spawn(int column){
		time -= delay;
		delay = MathUtils.random(minDelay, maxDelay);
		if(canSpawn(column)){
			rocks.add(game.createRock(xPositions[column]));
		}
	}
	
	
	public Play getGame() {
		return game;
	}

	public void setGame(Play game) {
		this.game = game;
	}

	public float getMinDelay() {
		return minDelay;
	}

	public void setMinDelay(float minDelay) {
		this.minDelay = minDelay;
	}

	public float getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(float maxDelay) {
		this.maxDelay = maxDelay;
	}
	
	public Array<Rock> getRocks() {
		return rocks;
	}

	public boolean canSpawn(int column){
		for(Rock r : rocks){
			if((r.getPosition().y > game.getRockSpawnY() - 100 / B2DVars.PPM) && r.getPosition().x == xPositions[column] / B2DVars.PPM)
				return false;
		}
		return true;
	}
	
	public void setDificulty(float dificulty){
		this.minDelay = dificulty;
		this.maxDelay = minDelay + 0.5f;
	}

	public void increaseDificulty() {
		if(minDelay > 0)
			setDificulty(minDelay - 0.1f);
		
		game.drawText("Difficulty\n" + MathUtils.round(((INITIAL_DIFFICULTY - minDelay) / INITIAL_DIFFICULTY) * 100) + "%!", Color.RED);
	}

}
