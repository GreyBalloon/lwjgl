package test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	private ObjModel model;
	public float xPos, yPos, zPos, xRot, yRot, zRot;
	
	public Entity(ObjModel model) {
		this.model = model;
	}
	
	public void update(EntityManager manager, int delta) {

	}

	
	public void render() {
		GL11.glPushMatrix();
		
		setRenderLoc();
		renderOther();
		renderModel();
		
		GL11.glPopMatrix();

	}
	
	public void setRenderLoc() {
		GL11.glTranslatef(xPos,yPos,zPos);
		GL11.glRotatef(xRot,1,0,0);
		GL11.glRotatef(yRot,0,1,0);
		GL11.glRotatef(zRot,0,0,1);

	}
	
	public void renderOther() {
		
	}
	
	public void renderModel() {
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

}
