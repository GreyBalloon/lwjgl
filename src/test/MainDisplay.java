package test;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class MainDisplay {

	long lastFPS;
	int fps;
	
	long lastFrame;
	
	float rotation = 0;
	float x = 400, y = 300;
	
	GameRunner runner;
	
	public MainDisplay() {
		try{
			
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setFullscreen(false);
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		init();
		
		/*GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		*/
	}
	
	public void gameLoop() {
		
		lastFPS = getTime();
		
		boolean gameRunning = true;
		
		while (!Display.isCloseRequested() && gameRunning)
		{
			//Render
			
			
			

			
			int delta = getDelta();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			int remainder = delta % 10;
			int step = delta / 10;
			
			for (int i=0;i<step;i++) {
				runner.update(this, 10);
			}
			if (remainder != 0)
			{
				runner.update(this, remainder);
			}
			
			runner.render(this, delta);
			
			Display.update();
			
			//Display.sync(60);
			
			updateFPS();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				gameRunning = false;
			}
		}
		
		Display.destroy();
		System.exit(0);
	}
	
	public void start() {
		gameLoop();
	}
	
	public void enterOrtho() {
		GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 60, 0, -1, 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	public void leaveOrtho() {
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	
	public void init() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		// define the properties for the perspective of the scene
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();		
		GLU.gluPerspective(4.50f, ((float) 800) / ((float) 600), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		runner = new GameRunner();
		
		runner.init(this);
	}
	
	public void update(int delta) {
		rotation += .15f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= .35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += .35f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y += .35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y -= .35f * delta;
		
		if (x < 0) x = 0;
		if (x > 800) x = 800;
		if (y<0) y = 0;
		if (y > 600) y = 600;
		
		
	}
	
	public void renderGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(.5f, .5f, 1f);
		
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
			
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x - 50, y - 50);
				GL11.glVertex2f(x + 50, y - 50);
				GL11.glVertex2f(x + 50, y + 50);
				GL11.glVertex2f(x - 50, y + 50);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void main(String[] args) {
		MainDisplay displayInstance = new MainDisplay();
		displayInstance.start();
	}

}
