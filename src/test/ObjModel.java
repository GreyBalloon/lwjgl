package test;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class ObjModel {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Face> faces = new ArrayList<Face>();
	
	public boolean triangulate = false;
	
	public ObjModel() {
		
	}
	
	public ObjModel(ObjModel m)
	{
		this.vertices = new ArrayList<Vector3f>(m.vertices);
		this.normals = new ArrayList<Vector3f>(m.normals);
		this.faces = new ArrayList<Face>(m.faces);
		this.triangulate = m.triangulate;
	}
	
	
}
