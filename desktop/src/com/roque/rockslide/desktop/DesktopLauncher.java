package com.roque.rockslide.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roque.rockslide.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = MainGame.TITLE;
		config.height = MainGame.V_HEIGHT * MainGame.SCALE;
		config.width = MainGame.V_WIDTH * MainGame.SCALE;
		config.addIcon("images/icons/icon128.png", Files.FileType.Internal);
		config.addIcon("images/icons/icon32.png", Files.FileType.Internal);
		config.addIcon("images/icons/icon16.png", Files.FileType.Internal);
		
		new LwjglApplication(new MainGame(), config);
	}
}