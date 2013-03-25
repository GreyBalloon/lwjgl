package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;



import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameRunner {
	
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private ArrayList<Entity> addList = new ArrayList<Entity>();
	
	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	private FloatBuffer material;
	
	public static MainDisplay mainDisplay;
	
	public static ObjModel playerModel;
	public static ObjModel cubeModel;
	public static ObjModel bombModel;
	public static ObjModel flagModel;
	
	public static ObjModel gameOverModel;
	public static ObjModel youWinModel;
	
	public static int size = 10;
	public static int mines = 15;
	public static int opened = 0;
	
	public static Number number;
	
	public static int flags = 0;
	
	public static boolean GO = false;
	
	public static boolean chatOpen = false;
	
	public static String curChat = "";
	
	
	private int num = 1;
	
	public static ArrayList<ObjModel> numbers = new ArrayList<ObjModel>();
	
	public static Box grid[][] = new Box[size][size];
	
	private Player player;
	
	public static Camera camera;
	
	public boolean keydown2 = false;
	public boolean keydown3 = false;
	
	private boolean key = false;
	
	public static MultiPlayer multiplayer;
	
	
	public static BitmapFont font;
	

	private void defineLight() {
		FloatBuffer buffer;
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(1).put(1).put(1).put(1); 
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer);
		
		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(1).put(1).put(1).put(1);
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer);
		
		// setup the ambient light 

		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0.8f).put(0.8f).put(0.8f).put(0.8f);
		buffer.flip();
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
		
		// set up the position of the light

		buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(10).put(10).put(5).put(0);
		buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, buffer);
		
		GL11.glEnable(GL11.GL_LIGHT0);
		
		material = BufferUtils.createFloatBuffer(4);
	}
	
	public void init(MainDisplay main) {
		//entities = new ArrayList<Entity>();
		this.mainDisplay = main;
		//defineLight();
		try {
			playerModel = ObjLoader.loadObj("res/cube.obj", false);
			cubeModel = ObjLoader.loadObj("res/cube.obj",false);
			flagModel = ObjLoader.loadObj("res/flag.obj");
			bombModel = ObjLoader.loadObj("res/bomb.obj");
			for (int i=0; i<=9;i++)
			{
				numbers.add(ObjLoader.loadObj("res/" + i + ".obj"));
			}
			gameOverModel = ObjLoader.loadObj("res/GAMEOVER.obj");
			youWinModel = ObjLoader.loadObj("res/YOUWIN.obj");
			
			Texture fontTexture = main.loader.getTexture("res/font.png");
			font = new BitmapFont(fontTexture, 32 ,32);
			System.out.println(GL11.glGetError());
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		
		
		setUpCamera();
		
		resetGrid();
		
		
		multiplayer = new MultiPlayer();
		
	}
	
	public static void resetGrid()
	{
		GO=false;
		opened = 0;
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
		
		number = new Number(mines, Colors.numberColor, 3);
		number.zPos = -1-2*size;
		number.yPos = 5;
		number.xPos = size;
		
		flags = 0;
		
		entities = new ArrayList<Entity>();
		
	}
	
	public static void coverGrid() {
		opened = 0;
		flags = 0;
		for (Box[] i : grid)
		{
			for( Box j : i)
			{
				j.open = false;
				j.marked = false;
			}
		}
		GO=false;
	}
	
	private static void checkInput() {
        if (!chatOpen)
        {
        	camera.processKeyboard(16, 1, 1, 1);
        	if (Mouse.isGrabbed())
        		camera.processMouse(1, 80, -80);
        }
        
        if (Mouse.isButtonDown(0)&&!chatOpen)
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
		Text text = new Text(gameOverModel, Colors.gameOverColor);
		text.zPos = -1-2*size;
		text.yPos = 7;
		text.xPos = size-1;
		entities.add(text);
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
			
			checkInput();
		
		/*if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			System.out.println("Camera X:" + camera.x() + " Y:" + camera.y() + " Z:" + 
					camera.z() + " Pitch:" + camera.pitch() + " Yaw:" + camera.yaw() +
					" Roll:" + camera.roll());
		}*/
		if (mines-flags>=0)
			number.number = mines-flags;
		number.update();
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !key && !GO && !chatOpen)
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
						updateMP();
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
		else if (Keyboard.isKeyDown(Keyboard.KEY_Q) && !GO && !chatOpen)
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
						updateMP();
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
		
		try {
			multiplayer.update(time);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			if (!chatOpen && !keydown3)
			{
				main.gameRunning = false;
				multiplayer.quit();
				keydown3 = true;
			}
			else if (!keydown3)
			{
				chatOpen = false;
				keydown3 = true;
				Mouse.setGrabbed(true);
			}
		}
		
		/*if (!multiplayer.isHost && !multiplayer.isClient && Keyboard.isKeyDown(Keyboard.KEY_H) && !chatOpen)
		{
			multiplayer.startHosting(4445);
		}
		if (!multiplayer.isHost && !multiplayer.isClient && Keyboard.isKeyDown(Keyboard.KEY_J) && !chatOpen)
		{
			multiplayer.joinServer("localhost", 4445);
		}*/
		
		if (Keyboard.isKeyDown(Keyboard.KEY_T) && !chatOpen)
		{
			chatOpen = true;
			curChat = "";
			keydown2 = true;
			Mouse.setGrabbed(false);
			Keyboard.destroy();
			try {
				Keyboard.create();
			} catch (LWJGLException e1) {
				e1.printStackTrace();
			}
			while (Keyboard.next()){}
		}
		
		if (chatOpen && Keyboard.next())
		{
			char c = Keyboard.getEventCharacter();
			if ((c=='T' || c=='t' && !keydown2) || (c!='t' && c!='T'))
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_BACK && Keyboard.isKeyDown(Keyboard.KEY_BACK))
				{
					if (curChat.length()>0)
					{
						curChat = curChat.substring(0, curChat.length()-1);
					}
				}
				else if (Integer.valueOf(c) >= ' ' && Integer.valueOf(c) <= ' '+128 && curChat.length()<34)
				{
					curChat = curChat + c;
					System.out.println(Integer.valueOf(c));
				}
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && chatOpen)
		{
			multiplayer.chatParse(curChat);
			chatOpen = false;
			Mouse.setGrabbed(true);
		}
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_T))
		{
			keydown2 = false;
		}
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			keydown3 = false;
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
		multiplayer.renderRemoteGrid();
		number.render();
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
		main.enterOrtho();
		
		int num;
		if (mines-flags>=0)
			num = mines-flags;
		else
			num = 0;
		font.drawString(1, "MINES LEFT: " + num, 10, 10, Colors.flagColor);
		if (GO)
		{
			font.drawString(1, "GAME OVER", 10, 50, Colors.bombColor);
		}
		
		if (chatOpen)
		{
			font.drawString(1, ">"+curChat, 10, 550, Colors.boxColor);
		}
		
		multiplayer.renderChat();
		
		main.leaveOrtho();
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
	
	public static void updateMP() {
		multiplayer.updateGrid();
	}
	
	
}
