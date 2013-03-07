package test;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Box extends Entity{
	Vector3f color;
	
	public Box(ObjModel model) {
		super(model);
		Random ran = new Random();
		color = new Vector3f(ran.nextFloat(), ran.nextFloat(), ran.nextFloat());
	}
	
	
	public void renderOther() {
		GL11.glColor3f(color.x, color.y, color.z);
	}

}
