package test;

import org.lwjgl.util.vector.Vector3f;

public class Color {
	public Vector3f colorVector;
	public float r,g,b;
	
	public Color(float r, float g, float b)
	{
		colorVector = new Vector3f(r,g,b);
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	public Color(Color color)
	{
		colorVector = new Vector3f(color.colorVector);
		r = color.r;
		g = color.g;
		b = color.b;
	}
	
	public Color getDarkened(float factor)
	{
		factor = 1-factor;
		Color newColor = new Color(this.r*factor, this.g*factor, this.b*factor);
		
		return newColor;
	}
	
	public Color getBritened(float factor)
	{
		factor = 1+factor;
		Color newColor = new Color(this.r*factor, this.g*factor, this.b*factor);
		
		return newColor;
	}
}
