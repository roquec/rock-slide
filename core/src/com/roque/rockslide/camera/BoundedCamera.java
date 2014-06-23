package com.roque.rockslide.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;


public class BoundedCamera extends OrthographicCamera{
	
	private Float xmin;
	private Float xmax;
	private Float ymin;
	private Float ymax;
	
	public BoundedCamera() {
		this(null, null, null, null);
	}
	
	public BoundedCamera(Float xmin, Float xmax, Float ymin, Float ymax) {
		super();
		setBounds(xmin, xmax, ymin, ymax);
	}
	
	public void setBounds(Float xmin, Float xmax, Float ymin, Float ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	public void setPosition(float x, float y) {
		setPosition(x, y, 0);
	}
	
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
		fixBounds();
	}
	
	private void fixBounds() {
		if(xmin != null && position.x < xmin + viewportWidth / 2) {
			position.x = xmin + viewportWidth / 2;
		}
		if(xmax != null && position.x > xmax - viewportWidth / 2) {
			position.x = xmax - viewportWidth / 2;
		}
		if(ymin != null && position.y < ymin + viewportHeight / 2) {
			position.y = ymin + viewportHeight / 2;
		}
		if(ymax != null && position.y > ymax - viewportHeight / 2) {
			position.y = ymax - viewportHeight / 2;
		}
	}

}
