package com.roque.rockslide.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Sprite attached to a B2DBox body
 */
public class EntitySprite extends Sprite{
	
	//Center
	private Body body;
	
	public EntitySprite(Body body) {
		super();
		this.body = body;
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public void setBody(Body body){
		this.body = body;
	}
	
}
