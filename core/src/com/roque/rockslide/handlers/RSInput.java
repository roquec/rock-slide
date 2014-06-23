package com.roque.rockslide.handlers;

public class RSInput {
	
	public static float x;
	public static float y;
	public static boolean down;
	public static boolean pdown;
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS = 3;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update(){
		pdown = down;
		for(int i= 0; i < NUM_KEYS; i++)
			pkeys[i] = keys[i];
	}
	
	public static boolean isDown() { return down; }
	public static boolean wasDown() { return pdown; }
	public static boolean isPressed() { return down && !pdown; }
	public static boolean isReleased() { return !down && pdown; }
	
	public static void setKey(int i, boolean b) {keys[i] = b;}
	public static boolean isDown(int i) {return keys[i];}
	public static boolean wasDown(int i) {return pkeys[i];}
	public static boolean isPressed(int i) { return keys[i] && !pkeys[i];}
	public static boolean isReleased(int i) { return !keys[i] && pkeys[i]; }
}
