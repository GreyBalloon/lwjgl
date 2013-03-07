package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;



import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameRunner {
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private ArrayList<Entity> addList = new ArrayList<Entity>();
	
	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	private FloatBuffer material;
	
	private ObjModel playerModel;
	
	private Player player;
	
	public static Camera camera;
	
	public void init(MainDisplay main) {
		entities = new ArrayList<Entity>();
		try {
			playerModel = ObjLoader.loadObj("res/ball.obj");
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		setUpCamera();
		
		
	}
	
	private static void checkInput() {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 1, 1, 1);
        if (Mouse.isButtonDown(0))
            Mouse.setGrabbed(true);
        else if (Mouse.isButtonDown(1))
            Mouse.setGrabbed(false);
    }
	
	private static void setUpCamera() {
        camera = new EulerCamera.Builder()
                .setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(-1.12f, 0.16f, 0f)
                .setPosition(-1.38f, 1.36f, 7.95f)
                .setFieldOfView(60)
                .build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
    }
	
	public void update(MainDisplay main, int time) {
		checkInput();
	}
	
	
	
	public void render(MainDisplay main, int delta) {
		GL11.glLoadIdentity();
		//material.put(1).put(1).put(1);
		//material.flip();
		//GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material);
		//GL11.glMaterial(GL11.GL_BACK, GL11.GL_DIFFUSE, material);

		camera.applyTranslations();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);
			
			entity.render();
		}
		entities.add(new Player(playerModel));
		drawGUI(main);
	}
	
	public void drawGUI(MainDisplay main)
	{
		
	}
	
	
}