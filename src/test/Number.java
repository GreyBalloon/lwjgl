package test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Number extends Entity{
	public int number;
	private int min;
	
	public Number(int num, Vector3f color, int min)
	{
		super(new ObjModel());
		this.number = num;
		this.color = color;
		this.min = min;
	}
	
	public void render()
	{
		
		
		String s = String.valueOf(number);
		while(s.length()<min)
		{
			s="0".concat(s);
		}
		float offset = s.length()*3/2;
		float tempX = xPos;
		float tempY = yPos;
		for (int i=0; i<s.length(); i++)
		{
			this.yPos = tempY;
			this.xPos = tempX - offset + 3*i;
			int num = Integer.valueOf(s.substring(i, i+1));
			if (num == 9|| num == 0)
				this.yPos = tempY + .4f;
			GL11.glPushMatrix();
			setRenderLoc(1);
			renderOther();
			GL11.glScalef(3, 3, 3);
			renderColor(this.color);
			renderModel(GameRunner.numbers.get(num));
			GL11.glPopMatrix();
		}
		this.xPos = tempX;
		this.yPos = tempY;
		
		
	}
}
