package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MultiPlayer {
	
	public ServerSocket serverSocket;
	public Socket clientSocket;
	public Socket remoteServer;
	
	public int remoteGridSize;
	
	public BufferedReader input;
	
	public String host;
	public int port;
	
	public boolean isHost = false;
	public boolean isClient = false;
	public boolean hasClient = false;
	
	public String grid = "";
	
	public Box[][] remoteGrid;
	
	public String[] oldData = new String[10];
	
	public ArrayList<String> chat = new ArrayList<String>();
	
	public ArrayList<Long> timeOut = new ArrayList<Long>();
	
	public final int coolDown = 5000;
	public final int maxChat = 10;
	
	public String sendChat = "";
	
	PrintWriter out;
	BufferedReader in;
	
	public Player player;
	
	
	public MultiPlayer() {
		input = new BufferedReader(new InputStreamReader(System.in));
	}
	
	
	
	public void startHosting(int port) {
		if (isHost || isClient)
			return;
		this.port = port;
		isHost = true;
		try {
			serverSocket = new ServerSocket(this.port);
		}
		catch (IOException e)
		{
			System.out.println("Could not open server on port " + this.port);
			e.printStackTrace();
		}
		System.out.println("Opened server on port " + this.port);
		
		clientSocket = null;
		try {
			serverSocket.setSoTimeout(10);
			clientSocket = serverSocket.accept();
			System.out.println(clientSocket.getInetAddress() + " connected");
			
			hasClient = true;
		}catch (SocketTimeoutException e){
		}catch (IOException e) {
			System.out.println("Connection Failed");
			e.printStackTrace();
		}
		if (hasClient)
		{
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				updateGrid();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void stopHosting()
	{
		if (!isHost||isClient)
			return;
		isHost=false;
		try {
			serverSocket.close();
			if (hasClient && clientSocket != null)
				if (clientSocket.isConnected())
				{
					clientSocket.close();
					in.close();
					out.close();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hasClient = false;
		System.out.println("Closed Server on port " + this.port);
		serverSocket=null;
		clientSocket=null;
		hasClient = false;
	}
	
	public void joinServer(String host, int port)
	{
		if (isClient||isHost)
			return;
		this.host = host;
		this.port = port;
		isClient = true;
		System.out.println("Attempting to connect to "+this.host+":"+this.port);
		
		try {
			remoteServer = new Socket(host,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connected to "+this.host+":"+this.port);
		
		try {
			out = new PrintWriter(remoteServer.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(remoteServer.getInputStream()));
			updateGrid();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void leaveServer() {
		if (!isClient||isHost)
			return;
		isClient = false;
		System.out.println("Disconnected from "+this.host+":"+this.port);
		try {
			remoteServer.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void update(int delta) throws IOException
	{
		
		
		if (isHost)
		{
			if (!hasClient)
			{
				//this.stopHosting();
				clientSocket = null;
				try {
					serverSocket.setSoTimeout(1);
					clientSocket = serverSocket.accept();
					System.out.println(clientSocket.getInetAddress() + " connected");
					hasClient = true;
				}catch (SocketTimeoutException e){
				}catch (IOException e) {
					System.out.println("Connection Failed");
					e.printStackTrace();
				}
				if (hasClient)
				{
					try {
						out = new PrintWriter(clientSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						updateGrid();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else
			{
				
				if (!clientSocket.isConnected())
				{
					hasClient = false;
					GameRunner.entities.remove(player);
					System.out.println(clientSocket.getInetAddress() + " disconnected");
				}
				else
				{
					if (in.ready())
					{
						String line = in.readLine();
						
						parse(line);
					}
					if (delta>=10)
					{
						transmit();
					}
					if (player!=null)
					{
						player.update();
					}
					if (remoteGrid!=null)
					{
						for (Box[] a : remoteGrid)
							for (Box b : a)
								b.update();
					}
				}
			}
		} else if (isClient)
		{
			if (remoteServer.isConnected())
			{
				if (delta>=10)
				{
					transmit();
				}
				if (in.ready())
				{
					String line = in.readLine();
					
					parse(line);
				}
				if (player!=null)
				{
					player.update();
				}
				if (remoteGrid!=null)
				{
					for (Box[] a : remoteGrid)
						for (Box b : a)
							b.update();
				}
			}
			else
			{
				System.out.println("Server Shutdown");
				this.leaveServer();
			}
		}
	}
	
	public void chatParse(String ln)
	{
		if (ln.equals("")){
			return;
		}
		else if (ln.startsWith("/"))
		{
			ln = ln.toLowerCase();
			if (ln.startsWith("/host "))
			{
				if (!isHost&&!isClient)
				{
					int port = Integer.valueOf(ln.split("host ")[1]);
					this.startHosting(port);
				}
			}
			else if (ln.startsWith("/stop"))
			{
				if (isHost&&!isClient)
				{
					this.stopHosting();
				}
			}
			else if (ln.startsWith("/connect "))
			{
				if (!isHost&&!isClient)
				{
					String host = ln.split("connect ")[1].split(":")[0];
					int port = Integer.valueOf(ln.split("connect ")[1].split(":")[1]);
					this.joinServer(host, port);
				}
			}else if (ln.startsWith("/disconnect"))
			{
				if (!isHost&&isClient)
				{
					this.leaveServer();
				}
			}
			else if (ln.equals("/reset"))
			{
				GameRunner.resetGrid();
			}
			else if (ln.equals("/cover"))
			{
				GameRunner.coverGrid();
			}
		}
		else
		{
			sendChat = "cht "+ln;
		}
		chat.add(ln);
		timeOut.add(MainDisplay.getTime());
	}
	
	public void addToChat(String ln)
	{
		chat.add(ln);
		timeOut.add(MainDisplay.getTime());
	}

	private void parse(String line) {
		if (line.startsWith("ploc "))
		{
			if (player == null)
				player = new Player(GameRunner.playerModel);
			GameRunner.entities.remove(player);
			float xChange = 0;
			if (isHost&&!isClient)
			{
				xChange = GameRunner.size*2+10;
			}
			else if (!isHost&&isClient)
			{
				xChange = -GameRunner.size*2-10;
			}
			player.xPos = Float.valueOf(line.split(" ")[1])+xChange;
			player.yPos = Float.valueOf(line.split(" ")[2]);
			player.zPos = Float.valueOf(line.split(" ")[3]);
			
			GameRunner.entities.add(player);
		} else if (line.startsWith("prot "))
		{
			if (player == null)
				player = new Player(GameRunner.playerModel);
			GameRunner.entities.remove(player);
			player.yRot = Float.valueOf(line.split(" ")[1]);
			//chat.add(line.split(" ")[1]);
			timeOut.add(MainDisplay.getTime());
			//player.xRot = (float) (Math.abs(Math.cos(player.yRot))*Float.valueOf(line.split(" ")[2]));
			//player.zRot = (float) (Math.abs(Math.sin(player.yRot))*Float.valueOf(line.split(" ")[2]));
			GameRunner.entities.add(player);
		}
		else if (line.startsWith("grid "))
		{
			//ArrayList<Box[]> newGrid = new ArrayList<Box[]>();
			//ArrayList<Box> row = new ArrayList<Box>();
			Box[][] newGrid = new Box[GameRunner.size][GameRunner.size];
			Box[] row = new Box[GameRunner.size];
			String[] list = line.split(" ");
			int i = 0, j = 0;
			for (String s : list)
			{
				if (s.equals("n"))
				{
					
					newGrid[i] = row;
					row = new Box[GameRunner.size];
					i++;
					j=0;
					
				}
				else if (!s.equals("grid"))
				{
					try  {
						int test;
						if (!s.equals("f")&&!s.equals("e"))
							test = Integer.valueOf(s);
						Box b = new Box(GameRunner.cubeModel, GameRunner.flagModel);
						
						if (isHost&&!isClient)
						{
							b.xPos = 2*i+GameRunner.size*2+10;
							b.zPos = 2*j;
						}
						else if (!isHost&&isClient)
						{
							b.xPos = 2*i-GameRunner.size*2-10;
							b.zPos = 2*j;
						}
						if (s.equals("f"))
						{
							b.marked=true;
						}
						else
						{
							if (!s.equals("e"))
							{
								b.setType(Integer.valueOf(s));
								b.open = true;
							}
						}
						row[j] = b;
						j++;
					}catch (Exception e) {
						System.out.println(s);
					}
				}
				
			}
			remoteGrid = newGrid;
			remoteGridSize = newGrid.length;
		}
		else if (line.startsWith("cht "))
		{
			line = line.split("cht ")[1];
			chat.add(line);
			timeOut.add(MainDisplay.getTime());
		}
	}
	
	public void quit()
	{
		if (isClient&&!isHost)
			this.leaveServer();
		else if (isHost&&!isClient)
			this.stopHosting();
	}
	
	public void transmit()
	{
		String pos = "ploc " + GameRunner.camera.x() + " " + GameRunner.camera.y() + " " +
			GameRunner.camera.z();
		if (!pos.equals(oldData[0]))
		{
			oldData[0] = pos;
			out.println(pos);
		}	
		
		if (!grid.equals(oldData[2]))
		{
			oldData[2] = grid;
			out.println(grid);
		}
		
		if (!sendChat.equals(oldData[3]))
		{
			oldData[3] = sendChat;
			out.println(sendChat);
		}
		
		String rot = "prot " + GameRunner.camera.yaw() + " " + GameRunner.camera.pitch();
		if (!rot.equals(oldData[1]))
		{
			oldData[1] = rot;
			out.println(rot);
		}
	}
	
	public void updateGrid()
	{
		grid = "grid ";
		for (Box[] a : GameRunner.grid)
		{
			for (Box b : a)
			{
				String s;
				if (b.marked)
					s="f";
				else if (!b.open)
					s="e";
				else
					s=String.valueOf(b.type);
				grid = grid.concat(" " +  s);
			}
			grid = grid.concat(" n");
		}
	}
	
	public void renderChat()
	{
		String s = "";
		for (int i=chat.size()-1;i>=(chat.size()<=maxChat? 0:chat.size()-maxChat); i--)
		{
			if (GameRunner.chatOpen)
			{
				GameRunner.font.drawString(1, chat.get(i), 10, 550-(chat.size()-i)*40, Colors.boxColor);
			}
			else
			{
				if (MainDisplay.getTime()-timeOut.get(i)<coolDown)
				{
					GameRunner.font.drawString(1, chat.get(i), 10, 550-(chat.size()-i)*40, Colors.boxColor);
				}
			}
		}
		
		
	}
	
	public void renderRemoteGrid()
	{
		if (remoteGrid != null)
		{
			for (Box[] a : remoteGrid)
			{
				for (Box b : a)
				{
					b.render();
				}
			}
		}
	}
}
