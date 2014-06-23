package com.roque.rockslide.screen;

import static com.roque.rockslide.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.roque.rockslide.MainGame;
import com.roque.rockslide.entity.HUDSprite;
import com.roque.rockslide.entity.TitleRock;
import com.roque.rockslide.entity.TitleSlide;
import com.roque.rockslide.handlers.B2DVars;

public class Title extends Screen{

	public static final float GRAVITY = -9.81f;
	
	private World world;
	private Box2DDebugRenderer d2dr;
	private OrthographicCamera B2DCamera;
		 
	private TitleRock tRock;
	private TitleSlide tSlide;
	private HUDSprite playButton;
	
	protected Title(ScreenManager sm) {
		super(sm);
		world = new World(new Vector2(0, GRAVITY), true);
		d2dr = new Box2DDebugRenderer();
		
		B2DCamera = new OrthographicCamera();
		B2DCamera.setToOrtho(false, MainGame.V_WIDTH / PPM, MainGame.V_HEIGHT / PPM);
	
		createTitle();
		
		playButton = new HUDSprite(120, 25, TextureRegion.split(MainGame.res.getTexture("play"), 90, 30)[0], 1 / 6f);
	}

	private void createTitle() {
		//Platform
		BodyDef dDef = new BodyDef();
		dDef.type = BodyType.StaticBody;
		dDef.position.set(120 / PPM, 160 / PPM);
		Body body = world.createBody(dDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(120 / PPM, 1 / PPM);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLATFORM1;
		fDef.filter.maskBits = B2DVars.BIT_TITLE;
		body.createFixture(fDef);
		
		dDef = new BodyDef();
		dDef.type = BodyType.StaticBody;
		dDef.position.set(120 / PPM, 190 / PPM);
		body = world.createBody(dDef);
		shape = new PolygonShape();
		shape.setAsBox(120 / PPM, 1 / PPM);
		fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLATFORM2;
		fDef.filter.maskBits = B2DVars.BIT_TITLE;
		body.createFixture(fDef);
		
		//Title1 (ROCK)
		dDef = new BodyDef();
		dDef.type = BodyType.DynamicBody;
		dDef.position.set(101 / PPM, 600 / PPM);
		body = world.createBody(dDef);
		shape = new PolygonShape();
		shape.setAsBox(51 / PPM, 15 / PPM);
		fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_TITLE;
		fDef.filter.maskBits = B2DVars.BIT_PLATFORM2;
		body.createFixture(fDef);
		
		tRock = new TitleRock(body);
		body.setUserData(tRock);
		
		//Title1 (SLIDE)
		dDef = new BodyDef();
		dDef.type = BodyType.DynamicBody;
		dDef.position.set(126 / PPM, 400 / PPM);
		body = world.createBody(dDef);
		shape = new PolygonShape();
		shape.setAsBox(64 / PPM, 15 / PPM);
		fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_TITLE;
		fDef.filter.maskBits = B2DVars.BIT_PLATFORM1;
		body.createFixture(fDef);
		
		tSlide = new TitleSlide(body);
		body.setUserData(tSlide);
		
		shape.dispose();
	}

	@Override
	public void handleInput() {
		if(playButton.isClicked())
			sm.setScreen(ScreenManager.PLAY);
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		playButton.update(dt);
		
		world.step(dt, 1, 1);
	}

	@Override
	public void render() {
		
		sb.setProjectionMatrix(hudCam.combined);
		
		tRock.render(sb);
		tSlide.render(sb);
		
		playButton.render(sb);
		
		if(MainGame.DEBUG){
			d2dr.render(world, B2DCamera.combined);
		}
	}

	@Override
	public void dispose() {}

	@Override
	public void resize(int width, int height) {}
	

}
