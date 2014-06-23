package com.roque.rockslide.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.roque.rockslide.MainGame;

public class RSInputProcessor extends InputAdapter {
	
	MainGame game;

	public RSInputProcessor(MainGame game) {
		this.game = game;
	}
	
	public boolean mouseMoved(int x, int y) {
		Vector2 v = game.unproject(x, y);
		RSInput.x = v.x;
		RSInput.y = v.y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		Vector2 v = game.unproject(x, y);
		RSInput.x = v.x;
		RSInput.y = v.y;
		RSInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 v = game.unproject(x, y);
		RSInput.x = v.x;
		RSInput.y = v.y;
		RSInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		Vector2 v = game.unproject(x, y);
		RSInput.x = v.x;
		RSInput.y = v.y;
		RSInput.down = false;
		return true;
	}
	
	public boolean keyDown(int k){
		
		switch (k) {
		case Keys.A:
			RSInput.setKey(RSInput.LEFT, true);
			break;
		case Keys.D:
			RSInput.setKey(RSInput.RIGHT, true);
			break;
		}
		
		return true;
	}
	
	public boolean keyUp(int k){
		
		switch (k) {
		case Keys.A:
			RSInput.setKey(RSInput.LEFT, false);
			break;
		case Keys.D:
			RSInput.setKey(RSInput.RIGHT, false);
			break;
		}
		
		return true;
	}
	
}
