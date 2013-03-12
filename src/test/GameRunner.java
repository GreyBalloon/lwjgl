package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;



import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameRunner {
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private ArrayList<Entity> addList = new ArrayList<Entity>();
	
	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	private FloatBuffer material;
	
	public static ObjModel playerModel;
	public static ObjModel cubeModel;
	public static ObjModel bombModel;
	public static ObjModel flagModel;
	
	public static int size = 10;
	public static int mines = 15;
	public static int opened = 0;
	
	public static boolean GO = false;
	
	
	private int num = 1;
	
	public static ArrayList<ObjModel> numbers = new ArrayList<ObjModel>();
	
	public static Box grid[][] = new Box[size][size];
	
	private Player player;
	
	public static Camera camera;
	
	private boolean key = false;
	
	public void init(MainDisplay main) {
		//entities = new ArrayList<Entity>();
		try {
			playerModel = ObjLoader.loadObj("res/ball.obj");
			cubeModel = ObjLoader.loadObj("res/cube.obj",false);
			flagModel = ObjLoader.loadObj("res/flag.obj");
			bombModel = ObjLoader.loadObj("res/bomb.obj");
			for (int i=1; i<=8;i++)
			{
				numbers.add(ObjLoader.loadObj("res/" + i + ".obj"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		
		
		setUpCamera();
		
		for (int i = 0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				Box b = new Box(new ObjModel(cubeModel), new ObjModel(flagModel));
				b.xPos = 2*i;
				b.zPos = 2*j;
				grid[i][j] = b;
				//entities.add(b);
			}
		}
		
	}
	
	private static void checkInput() {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 1, 1, 1);
        if (Mouse.isButtonDown(0))
            Mouse.setGrabbed(true);
        else if (Mouse.isButtonDown(1))
            Mouse.setGrabbed(false);
    }
	
	public static void gameOver() {
		System.out.println("GAME OVER");
		GO = true;
		for (Box[] a : grid)
		{
			for (Box b : a)
			{
				if (b.type == -1)
				{
					b.marked = false;
					b.open = true;
					
				}
			}
		}
	}
	
	private static void setUpCamera() {
        camera = new EulerCamera.Builder()
                .setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(80f, 90f, 0f)
                .setPosition(10f, 36f, 19f)
                .setFieldOfView(60)
                .build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
    }
	
	public void update(MainDisplay main, int time) {
		if (!GO)
			checkInput();
		else {
			camera.setPosition(10, 36, 19);
			camera.setRotation(80, 90, 0);
		}
		
		/*if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			System.out.println("Camera X:" + camera.x() + " Y:" + camera.y() + " Z:" + 
					camera.z() + " Pitch:" + camera.pitch() + " Yaw:" + camera.yaw() +
					" Roll:" + camera.roll());
		}*/
		for (Entity e: entities)
		{		
			e.update();
		}
		for (Box[] i : grid)
		{
			for( Box j : i)
			{
				j.update();
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !key && !GO)
		{
			for (Box[] i : grid)
			{
				double dis = 1;
				for( Box b : i)
				{
					dis = Math.sqrt((Math.pow(Math.sqrt(Math.pow(camera.x()-b.xPos,2)+Math.pow(camera.y()-b.yPos,2)),2)+Math.sqrt(Math.pow(camera.x()-b.xPos,2)+Math.pow(camera.z()-b.zPos,2))));
					
					if (dis<1.2 && !b.getOpen())
					{
						if (!b.getFlagged())
							b.flag();
						else
							b.unflag();
						break;
					}
				}
				if (dis<1.2)
				{
					break;
				}
			}
			key = true;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_Q) && !GO)
		{
			
			for (int i = 0; i<size; i++)
			{
				double dis = 1;
				for(int j = 0; j<size; j++)
				{
					Box b = grid[i][j];
					dis = Math.sqrt((Math.pow(Math.sqrt(Math.pow(camera.x()-b.xPos,2)+Math.pow(camera.y()-b.yPos,2)),2)+Math.sqrt(Math.pow(camera.x()-b.xPos,2)+Math.pow(camera.z()-b.zPos,2))));
					
					if (dis<1.2 && !b.getOpen() && !b.getFlagged())
					{
						
						if (!b.init)
						{
							generateGrid(i,j);
							System.out.println("Generated Grid");
						}
						b.open(1,j);
						break;
					}
				}
				if (dis<1.2)
				{
					break;
				}
			}
			
			
			/*//Box b = new Box(new ObjModel(numbers.get(num-1)));
			Box b = new Box(new ObjModel(cubeModel), new ObjModel(flagModel));
			b.setType(num);
			b.xPos = camera.x();
			b.yPos = camera.y();
			b.zPos = camera.z();
			entities.add(b);
			key = true;
			*/
		}
		else if (!Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			key = false;
		}
		
		if (opened==size*size-mines)
		{
			GO=true;
			System.out.println("YOU WIN!");
		}
	}
	
	
	
	public void render(MainDisplay main, int delta) {
		GL11.glLoadIdentity();
		//material.put(1).put(1).put(1);
		//material.flip();
		//GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, material);
		//GL11.glMaterial(GL11.GL_BACK, GL11.GL_DIFFUSE, material);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		camera.applyTranslations();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		for (Entity e : entities) {
			e.render();
		}
		for (Box[] i : grid)
		{
			for( Box j : i)
			{
				j.render();
			}
		}
		
		drawGUI(main);
	}
	
	public void drawGUI(MainDisplay main)
	{
		
	}
	
	public void generateGrid(int fi, int fj) {
		int placed = 0;
		Random rand = new Random();
		while (placed<=mines)
		{
			int i = rand.nextInt(size);
			int j = rand.nextInt(size);
			if (!grid[i][j].init && (i > fi+1 || i <fi-1) && (j > fj+1 || j < fj-1))
			{
				grid[i][j].init=true;
				grid[i][j].setType(-1);
				placed++;
			}
		}
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				if (grid[i][j].type!=-1)
				{
					int count = 0;
					for(int l=-1; l<=1; l++)
					{
						for(int m=-1; m<=1; m++)
						{
							if ((l!=0 || m!=0) && i+l>=0 && j+m>=0 && i+l<size && j+m<size)
							{
								if (grid[i+l][j+m].type==-1)
								{
									count++;
									
								}
							}
						}
					}
					grid[i][j].setType(count);
					grid[i][j].init=true;
				}
				if (grid[i][j].type!=-1)
					System.out.print(grid[i][j].type+ "  ");
				else
					System.out.print(grid[i][j].type+ " ");
			}
			System.out.println();
		}
	}
	
	
}
