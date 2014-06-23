package com.roque.rockslide.screen;

import static com.roque.rockslide.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.camera.BoundedCamera;
import com.roque.rockslide.entity.Ground;
import com.roque.rockslide.entity.HUDSprite;
import com.roque.rockslide.entity.Player;
import com.roque.rockslide.entity.Rock;
import com.roque.rockslide.entity.Text;
import com.roque.rockslide.handlers.B2DVars;
import com.roque.rockslide.handlers.Background;
import com.roque.rockslide.handlers.RSContactListener;
import com.roque.rockslide.handlers.RSInput;
import com.roque.rockslide.handlers.RockManager;

public class Play extends Screen{
	
	public static final float GRAVITY = -5f;
	public static final float CLIMB_SPEED = +0.7f;
	
	//Game states
	private boolean running;
	private boolean HUD;
	private boolean input;
	private boolean animations;
	
	//B2D WORLD
	private World world;
	private Box2DDebugRenderer d2dr;
	private BoundedCamera B2DCamera;
	private RSContactListener cl;
		
	//Entities
	private Body follow;
	private Player player;
	private Ground ground;
	private Body[] walls = new Body[2];
	private RockManager rm;
	
	//Backgrounds
	private Background[] bgs;
	
	//HUD
	private HUDSprite left, right, sound, pause;
	private Array<Text> texts;
	
	private static final int D_DELAY = 20;
	private float time;

	protected Play(ScreenManager sm) {
		super(sm);

		//BOX2D
		world = new World(new Vector2(0, GRAVITY), true);
		cl = new RSContactListener(this); 
		world.setContactListener(cl);
		d2dr = new Box2DDebugRenderer();
		
		//BOX2D camera
		B2DCamera = new BoundedCamera();
		B2DCamera.setToOrtho(false, MainGame.V_WIDTH / PPM, MainGame.V_HEIGHT / PPM);
		B2DCamera.setBounds(0f, MainGame.V_WIDTH / PPM, 0f, null);
		
		rm = new RockManager(this);
		
		//Ground
		createGround();
		
		//Player
		createPlayer();
		follow = player.getBody();
		
		//Walls
		walls[0] = createWall(14);
		walls[1] = createWall(226);
		
		//Backgrounds
		bgs = new Background[3];
		bgs[0] = new Background(new TextureRegion(MainGame.res.getTexture("sky")), cam, 0f);
		bgs[1] = new Background(new TextureRegion(MainGame.res.getTexture("clouds")), cam, 0.4f);
		bgs[2] = new Background(new TextureRegion(MainGame.res.getTexture("walls")), cam, 1f);
		
		cam.setBounds(0f, (float) MainGame.V_WIDTH, 0f, null);
		
		//HUD
		left = new HUDSprite(60, 30, TextureRegion.split(MainGame.res.getTexture("pad"), 47, 47)[0], 1 / 6f, 110, 80);
		TextureRegion[] t = TextureRegion.split(MainGame.res.getTexture("pad"), 47, 47)[0];
		for(int i = 0; i < t.length; i++)
			t[i].flip(true, false);
		right = new HUDSprite(180, 30, t, 1 / 6f, 110, 80);
		
		pause = new HUDSprite(232, 312, TextureRegion.split(MainGame.res.getTexture("pause"), 16, 16)[0], 1 / 6f, 30 , 30);
		sound = new HUDSprite(8, 312, TextureRegion.split(MainGame.res.getTexture("sound"), 16, 16)[MainGame.getSound()], 1 / 6f, 30 , 30);
		
		texts = new Array<Text>();
		
		this.running = true;
		this.input = true;
		this.HUD = true;
		this.animations = true;
	}

	@Override
	public void handleInput() {
		
		if(pause.isClicked())
				sm.setScreen(ScreenManager.PAUSE);
		
		if(sound.isClicked()){
			if(MainGame.getSound() == 0) MainGame.soundON();
			else MainGame.soundOFF();
			sound.setSprites(TextureRegion.split(MainGame.res.getTexture("sound"), 16, 16)[MainGame.getSound()],  1 / 6f);
		}
		
		if(player != null && !player.isDead()){
			if(RSInput.isDown(RSInput.LEFT) || left.isDown()){
				player.setDirection(Player.LEFT);
				
				if(cl.canClimbLeft())
					player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, CLIMB_SPEED);
				else
					player.getBody().setLinearVelocity(-1f, player.getBody().getLinearVelocity().y);
			}
			else if(RSInput.isDown(RSInput.RIGHT) || right.isDown()){
				
				player.setDirection(Player.RIGHT);
				
				if(cl.canClimbRight())
					player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, CLIMB_SPEED);
				else
					player.getBody().setLinearVelocity(1f, player.getBody().getLinearVelocity().y);
			}
			else
				player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
		}
	}

	@Override
	public void update(float dt) {
		
		
		
		if(running){
			
			rm.update(dt);
			
			//B2D world update
			world.step(dt, 1, 1);
			
			//GAME OVER
			if(player.isDead()){
				
				//sm.restart();
				if(player.getBody() != null){
					world.destroyBody(player.getBody());
					player.setBody(null);
					sm.setScreen(ScreenManager.SCORE);
				}
			}
			
			//ROCKS update
			
			for(Rock r : rm.getRocks()){
				if(r.getBody().getLinearVelocity().y == 0 && r.getBody().getType() != BodyType.StaticBody){
					r.getBody().setType(BodyType.StaticBody);
					if(MainGame.getSound() == 1)
						MainGame.res.getSound("rock_smash").play();
					r.playImpact();
				}
			}
			
			//Move the walls to follow the player
			if(!player.isDead())
				for(int i = 0; i<walls.length; i++)
					walls[i].setTransform(walls[i].getPosition().x, player.getPosition().y, walls[i].getAngle());
			
			updateDificulty(dt);
		}
		
		//ANIMATIONS
		if(animations){
			if(!player.isDead())
				player.update(dt);
			
			for(Rock r : rm.getRocks())
				r.update(dt);
			
			for(Text t : texts)
				t.update(dt);
		}
		
		
		//HUD
		if(HUD){
			left.update(dt);
			right.update(dt);
			pause.update(dt);
			sound.update(dt);
		}
		
		if(input)
			handleInput();
	}


	private void updateDificulty(float dt) {
		time += dt;
		if(time > D_DELAY){
			time = 0;
			rm.increaseDificulty();
		}
	}

	@Override
	public void render() {
		
		//Clear bits to keep the black bars
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Draw backgrounds
		sb.setProjectionMatrix(hudCam.combined); //Use HUDcam
		for(int i = 0; i<bgs.length; i++)
			bgs[i].render(sb);
		
		//Camera following the player
		cam.setPosition(MainGame.V_WIDTH / 2, (MainGame.V_HEIGHT / 2) + (follow.getPosition().y  * PPM) - 86);
		cam.update();
		
		//Draw entities
		sb.setProjectionMatrix(cam.combined); //Use cam
		ground.render(sb);
		for(Rock rock : rm.getRocks())
			rock.render(sb);
		if(!player.isDead())
			player.render(sb);
		
		//Draw HUD
		if(HUD){
			sb.setProjectionMatrix(hudCam.combined);
			left.render(sb);
			right.render(sb);
			pause.render(sb);
			sound.render(sb);
		}
		
		//Draw text
        sb.begin();
        for(Text t : texts)
        	t.render(sb);
		sb.end();
		
		
		//BOX2D camera
		if(MainGame.DEBUG){
			B2DCamera.setPosition(MainGame.V_WIDTH / 2 / PPM, (MainGame.V_HEIGHT / 2 / PPM) + follow.getPosition().y - (86 / PPM));
			B2DCamera.update();
			d2dr.render(world, B2DCamera.combined);
		}
	}

	@Override
	public void dispose() {}

	@Override
	public void resize(int width, int height) {}

	private void createPlayer(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(120 / PPM, 95 / PPM);
		bodyDef.type = BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		
		PolygonShape  shape = new PolygonShape();
		shape.setAsBox(5 / PPM, 11 / PPM);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_WALL | B2DVars.BIT_ROCK;
		body.createFixture(fDef).setUserData("player");
		
		//Head sensor
		shape.setAsBox(3 / PPM, 1 / PPM, new Vector2(0, 11 / PPM), 0);
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_ROCK;
		fDef.isSensor = true;
		body.createFixture(fDef).setUserData("head");
		
		MassData md = body.getMassData();
		md.mass = 0.001f;
		body.setMassData(md);
				
		player = new Player(body, this);
		body.setUserData(player);
		
		shape.dispose();
	}
	
	private void createGround(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(120 / PPM, 40 / PPM);
		bodyDef.type = BodyType.StaticBody;
		Body body = world.createBody(bodyDef);
		
		PolygonShape  shape = new PolygonShape();
		shape.setAsBox(105 / PPM, 40 / PPM);
		
		FixtureDef fDef = new FixtureDef();
		fDef.friction = 0;
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_GROUND;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_ROCK;
		body.createFixture(fDef).setUserData("ground");
		
		ground = new Ground(body);
		body.setUserData(ground);
		
		shape.dispose();
	}
	
	private Body createWall(int x){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / PPM, 160 / PPM);
		bodyDef.type = BodyType.StaticBody;
		Body body = world.createBody(bodyDef);
		
		PolygonShape  shape = new PolygonShape();
		shape.setAsBox(1 / PPM, 160 / PPM);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.friction = 0;
		fDef.filter.categoryBits = B2DVars.BIT_WALL;
		fDef.filter.maskBits = B2DVars.BIT_ROCK | B2DVars.BIT_PLAYER;
		body.createFixture(fDef).setUserData("wall");
		
		shape.dispose();
		
		return body;
	}
	
	public Rock createRock(int x){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / PPM, cam.position.y * 3/ PPM);
		bodyDef.type = BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		
		PolygonShape  shape = new PolygonShape();
		shape.setAsBox(12 / PPM, 12 / PPM);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.friction = 0;
		fDef.filter.categoryBits = B2DVars.BIT_ROCK;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_ROCK | B2DVars.BIT_GROUND | B2DVars.BIT_WALL;
		
		body.createFixture(fDef).setUserData("rock");
		
		//Edges (wraping for smooth movement)
		ChainShape cs = new ChainShape();
		Vector2[] v = new Vector2[2];
		v[0] = new Vector2(-13 / PPM, 13 / PPM);
		v[1] = new Vector2(13 / PPM, 13 / PPM);
		cs.createChain(v);
		fDef.shape = cs;
		fDef.friction = 0;
		fDef.filter.categoryBits = B2DVars.BIT_ROCK;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER;
		
		body.createFixture(fDef).setUserData("top");
		
		//Sides sensor
		cs = new ChainShape();
		v = new Vector2[2];
		v[0] = new Vector2(13 / PPM, 10 / PPM);
		v[1] = new Vector2(13 / PPM, -11 / PPM);
		cs.createChain(v);
		fDef.shape = cs;
		fDef.filter.categoryBits = B2DVars.BIT_ROCK;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fDef).setUserData("right");
		
		cs = new ChainShape();
		v = new Vector2[2];
		v[0] = new Vector2(-13 / PPM, 10 / PPM);
		v[1] = new Vector2(-13 / PPM, -11 / PPM);
		cs.createChain(v);
		fDef.shape = cs;
		fDef.filter.categoryBits = B2DVars.BIT_ROCK;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fDef).setUserData("left");
				
		MassData md = body.getMassData();
		md.mass = 1000000f;
		body.setMassData(md);
		body.setGravityScale(0.2f);
		Rock rock = new Rock(body);
		body.setUserData(rock);
		
		shape.dispose();
		cs.dispose();
		
		return rock;
	}
	
	public void pause(){
		setRunning(false);
		setInput(false);
		setAnimations(false);
		setHUD(false);
	}

	
	public void resume(){
		setRunning(true);
		setHUD(true);
		setAnimations(true);
		setInput(true);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setFollow(Body body){
		this.follow = body;
	}
	
	public float getRockSpawnY(){
		return cam.position.y * 3/ PPM;
	}
	
	public void drawText(String text, Color c){
		int y = 280 - texts.size * 20;
		for(Text t :  texts)
			y -= t.getMultiLineBounds(t.getText()).height - 15;
		texts.add(new Text(text, 120, y, this, c.r, c.g, c.b));
	}
	
	public void disposeText(Text t){
		texts.removeValue(t, true);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isHUD() {
		return HUD;
	}

	public void setHUD(boolean hUD) {
		HUD = hUD;
	}

	public boolean isInput() {
		return input;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	public boolean isAnimations() {
		return animations;
	}

	public void setAnimations(boolean animations) {
		this.animations = animations;
	}
}
