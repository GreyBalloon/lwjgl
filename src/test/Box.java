package test;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Box extends Entity{
	private Vector3f color;
	public boolean open = false;
	public boolean marked = false;
	private ObjModel inside;
	private ObjModel flag;
	public boolean init = false;
	
	public int type;
	
	public Box(ObjModel model, ObjModel flag) {
		super(model);
		this.flag = flag;
		Random ran = new Random();
		//this.color = new Vector3f(ran.nextFloat(), ran.nextFloat(), ran.nextFloat());
	}
	
	public void render() {
		GL11.glPushMatrix();
		
		setRenderLoc(2);
		renderColor(Colors.boxColor);
		if (!(this.open && this.type==0))
		{
			renderModel(this.model);
		}
		
		
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		setRenderLoc(1);	
		
		
		if (this.open && this.type!=0) {
			if (type==-1)
			{
				renderColor(Colors.bombColor);
				GL11.glScalef(.5f, .5f, .5f);
			}
			else
			{
				renderColor(Colors.numberColors[type]);
				GL11.glScalef(2, 2, 2);
			}
			renderModel(this.inside);
		}
		else if (this.open && this.type==0)
		{
			
		}
		else if (this.marked) {
			renderColor(Colors.flagColor);
			renderModel(this.flag);
		}
		else {
			GL11.glScalef(.5f, .5f, .5f);
			renderColor(Colors.innerBoxColor);
			renderModel(this.model);
		}
		GL11.glPopMatrix();

	}
	
	public void setType(int type)
	{
		this.type = type;
		this.init = true;
		if (type>0)
		{
			this.inside = GameRunner.numbers.get(type);
		}
		else if (type == -1)
		{
			this.inside = GameRunner.bombModel;
		}
	}
	
	public void open(int i, int j)
	{
		if (!this.marked)
		{
			this.open = true;
			if (this.type == -1)
			{

				System.out.println(this.type);
				GameRunner.gameOver();
			}
			else
			{
			GameRunner.opened++;
			}
			if (this.type == 0)
			{
				for(int l=-1; l<=1; l++)
				{
					for(int m=-1; m<=1; m++)
					{
						if ((l!=0 || m!=0) && i+l>=0 && j+m>=0 && i+l<GameRunner.size && j+m<GameRunner.size)
						{
							if(!GameRunner.grid[i+l][j+m].open)
								GameRunner.grid[i+l][j+m].open(i+l,j+m);
						}
					}
				}
				
			}
		}
	}
	
	public boolean getOpen() {
		return this.open;
	}
	
	public boolean getFlagged() {
		return this.marked;
	}

	

	public void update() {
		this.yRot += 1;
		if (type==-1 && !this.marked && this.open)
			this.xRot +=.3;
		else
			this.xRot = 0;
	}
	
	public void flag()
	{
		if (!this.open)
		{
			this.marked = true;
			GameRunner.updateMP();
			GameRunner.flags++;
		}
	}
	public void unflag()
	{
		if (!this.open)
		{
			this.marked = false;
			GameRunner.updateMP();
			GameRunner.flags--;
		}
	}
}
