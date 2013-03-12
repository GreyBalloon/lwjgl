package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MultiPlayer {
	
	public ServerSocket serverSocket;
	public Socket clientSocket;
	public Socket remoteServer;
	
	public String host;
	public int port;
	
	public boolean isHost = false;
	public boolean isClient = false;
	public boolean hasClient = false;
	
	PrintWriter out;
	BufferedReader in;
	
	
	public MultiPlayer() {
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
			System.out.println("Could not open server on port 4444");
			e.printStackTrace();
		}
		
		clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
			hasClient = true;
		}catch (IOException e) {
			System.out.println("Connection Faild");
			e.printStackTrace();
		}
		if (hasClient)
		{
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
			if (clientSocket.isConnected())
				clientSocket.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverSocket=null;
		clientSocket=null;
		out.close();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void leaveServer() {
		if (!isClient||isHost)
			return;
		isClient = false;
		try {
			remoteServer.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void update() throws IOException
	{
		if (isHost)
		{
			if (!hasClient)
			{
				this.stopHosting();
			}
			else
			{
				if (in.ready())
				{
					parse(in.readLine());
				}
			}
		}
	}

	private void parse(String line) {
		// TODO Auto-generated method stub
		
	}
}
