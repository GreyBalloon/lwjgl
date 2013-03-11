package test;


import org.lwjgl.util.vector.Vector4f;

public class QuadFace extends Face {
	public Vector4f vertex = new Vector4f();
	public Vector4f normal = new Vector4f();
	
	public QuadFace(Vector4f vert, Vector4f norm) {
		super();
		this.vertex = vert;
		this.normal = norm;
	}
}
