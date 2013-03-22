package test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	public ObjModel model;
	public Vector3f color;
	public float xPos, yPos, zPos, xRot, yRot, zRot;
	
	public Entity(ObjModel model) {
		this.model = model;
	}
	
	public void update(EntityManager manager, int delta) {

	}

	
	public void render() {
		GL11.glPushMatrix();
		
		setRenderLoc(1);
		renderOther();
		renderColor(this.color);
		renderModel(this.model);
		
		GL11.glPopMatrix();

	}
	
	public void setRenderLoc(int i) {
		if (i==1||i==2)
		{
			GL11.glTranslatef(xPos,yPos,zPos);
		}
		if (i==1||i==3)
		{
			GL11.glRotatef(xRot,1,0,0);
			GL11.glRotatef(yRot,0,1,0);
			GL11.glRotatef(zRot,0,0,1);
		}

	}
	
	public void renderOther() {
		
	}
	
	public void renderColor(Vector3f color) {
		GL11.glColor3f(color.x, color.y, color.z);
	}
	
	public void renderModel(ObjModel model) {
		if (model.triangulate)
		{
			GL11.glBegin(GL11.GL_TRIANGLES);
			
			for (Face face : model.faces) {
				Vector3f n1 = model.normals.get((int) face.normal.x - 1);
				GL11.glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
				GL11.glVertex3f(v1.x, v1.y, v1.z);
				
				Vector3f n2 = model.normals.get((int) face.normal.y - 1);
				GL11.glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
				GL11.glVertex3f(v2.x, v2.y, v2.z);
				
				Vector3f n3 = model.normals.get((int) face.normal.z - 1);
				GL11.glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
				GL11.glVertex3f(v3.x, v3.y, v3.z);
			}
			
			
			GL11.glEnd();
		}
		else
		{
			GL11.glBegin(GL11.GL_QUADS);
			
			for (Face face1 : model.faces) {
				QuadFace face = (QuadFace)face1;
				Vector3f n1 = model.normals.get((int) face.normal.w - 1);
				GL11.glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = model.vertices.get((int) face.vertex.w - 1);
				GL11.glVertex3f(v1.x, v1.y, v1.z);
				
				Vector3f n2 = model.normals.get((int) face.normal.x - 1);
				GL11.glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = model.vertices.get((int) face.vertex.x - 1);
				GL11.glVertex3f(v2.x, v2.y, v2.z);
				
				Vector3f n3 = model.normals.get((int) face.normal.y - 1);
				GL11.glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = model.vertices.get((int) face.vertex.y - 1);
				GL11.glVertex3f(v3.x, v3.y, v3.z);
				
				Vector3f n4 = model.normals.get((int) face.normal.z - 1);
				GL11.glNormal3f(n4.x, n4.y, n4.z);
				Vector3f v4 = model.vertices.get((int) face.vertex.z - 1);
				GL11.glVertex3f(v4.x, v4.y, v4.z);
			}
			
			GL11.glEnd();
		}
	}

	public float getSize() {
		return 1;
	}

	public float getPosX() {
		return xPos;
	}

	public float getPosY() {
		return yPos;
	}

	public float getPosZ() {
		return zPos;
	}
	public float getRotX() {
		return xRot;
	}

	public float getRotY() {
		return yRot;
	}

	public float getRotZ() {
		return zRot;
	}
	
	public void update() {
		
	}

}
