package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ObjLoader{
	public static ObjModel loadObj (String f, boolean triangulate) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		ObjModel m = new ObjModel();
		
		String line;
		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.vertices.add(new Vector3f(x,y,z));
			}
			else if (line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.normals.add(new Vector3f(x,y,z));
			}
			else if (line.startsWith("f "))
			{
				if(triangulate)
				{
					Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]),
							Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]));
					Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]),
							Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));
					Face face = new Face(vertexIndices, normalIndices);
					m.faces.add(face);
					m.triangulate = true;
				}
				else
				{
					Vector4f vertexIndices = new Vector4f(Float.valueOf(line.split(" ")[1].split("/")[0]),
							Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]),
							Float.valueOf(line.split(" ")[4].split("/")[0]));
					Vector4f normalIndices = new Vector4f(Float.valueOf(line.split(" ")[1].split("/")[2]),
							Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]),
							Float.valueOf(line.split(" ")[4].split("/")[2]));
					QuadFace face = new QuadFace(vertexIndices, normalIndices);
					m.faces.add(face);
					m.triangulate = false;
				}
			}
		}
		reader.close();
		
		return m;
	}
	
	public static ObjModel loadObj(String f) throws FileNotFoundException, IOException
	{
		return loadObj(f, true);
	}
}
