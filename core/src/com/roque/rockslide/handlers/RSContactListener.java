package com.roque.rockslide.handlers;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.roque.rockslide.entity.Rock;
import com.roque.rockslide.screen.Play;

public class RSContactListener implements ContactListener{
	
	private Set<Fixture> rightContacts = new HashSet<Fixture>();
	private Set<Fixture> leftContacts = new HashSet<Fixture>();
	private Set<Fixture> headContacts = new HashSet<Fixture>();
	
	private Play game;
	
	public RSContactListener(Play game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
				
		if(a == null || b == null) return;
		
		//Right side
		if(a.getUserData() != null && a.getUserData().equals("right"))
			rightContacts.add(a);
		
		if(b.getUserData() != null && b.getUserData().equals("right"))
			rightContacts.add(b);
		
		//Left side
		if(a.getUserData() != null && a.getUserData().equals("left"))
			leftContacts.add(a);
		
		if(b.getUserData() != null && b.getUserData().equals("left"))
			leftContacts.add(b);
		
		//Head
		if(a.getUserData() != null && a.getUserData().equals("head")){
			game.getPlayer().kill();
			((Rock) b.getBody().getUserData()).hit = true;
			game.setFollow(b.getBody());
		}
		
		if(b.getUserData() != null && b.getUserData().equals("head")){
			game.getPlayer().kill();
			((Rock) a.getBody().getUserData()).hit = true;
			game.setFollow(a.getBody());
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
				
		if(a == null || b == null) return;
		
		//Right side
		if(a.getUserData() != null && a.getUserData().equals("right"))
			rightContacts.remove(a);
		
		if(b.getUserData() != null && b.getUserData().equals("right"))
			rightContacts.remove(b);
				
		//Left side
		if(a.getUserData() != null && a.getUserData().equals("left"))
			leftContacts.remove(a);
		
		if(b.getUserData() != null && b.getUserData().equals("left"))
			leftContacts.remove(b);
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	
	public boolean canClimbRight(){ return !leftContacts.isEmpty();}
	public boolean canClimbLeft(){ return !rightContacts.isEmpty();}
	public boolean isDead() { return !headContacts.isEmpty(); }
}
