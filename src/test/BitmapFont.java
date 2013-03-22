package test;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class BitmapFont {
	private int charactersAcross = 16;
	
	private int characterWidth = 32;
	private int characterHeight = 16;
	
	private float characterWidthInTexture;
	private float characterHeightInTexture;
	
	private Texture texture;
	
	private int characterStep;
	
	public BitmapFont (Texture texture, int characterWidth, int characterHight) {
		this.texture = texture;
		this.characterWidth = characterWidth;
		this.characterHeight = characterHight;
		
		characterWidthInTexture = texture.getWidth() / (texture.getImageWidth() / characterWidth);
		characterHeightInTexture = texture.getHeight() / (texture.getImageHeight() / characterHeight);
		
		charactersAcross = texture.getImageWidth() / characterWidth;
		
		characterStep = characterWidth - 10;
	}
	
	public void drawString(int font, String text, int x, int y, Vector3f color) {
		// bind the font text so we can render quads with the characters

		// on
		
		texture.bind();

		// turn blending on so characters are displayed above the

		// scene
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		// cycle through each character drawing a quad to the screen

		// mapped to the right part of the texture

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(color.x, color.y, color.z);
		for (int i=0;i<text.length();i++) {
			// get the index of the character baesd on the font starting

			// with the space character

			int c =text.charAt(i) - ' ';
			// work out the u,v texture mapping coordinates based on the

			// index of the character and the amount of texture per

			// character

			float u = ((c % charactersAcross) * characterWidthInTexture);
			float v = 1-((7-(c / charactersAcross)) * characterHeightInTexture);
			//System.out.print(" " + ((c / charactersAcross)));
			
			
			v -= font * 0.5f;
			//System.out.print(" :" + (y+characterHeight)+ " " + (x+(i*characterStep)+characterWidth) + " " + v + ": ");
			
			
			// setup the quad 

			GL11.glTexCoord2f(u, v);
			GL11.glVertex2f(x+(i*characterStep), y+characterHeight);
			GL11.glTexCoord2f(u, v - characterHeightInTexture);
			GL11.glVertex2f(x+(i*characterStep), y);
			GL11.glTexCoord2f(u + characterWidthInTexture, v - characterHeightInTexture);
			GL11.glVertex2f(x+(i*characterStep)+characterWidth, y);
			GL11.glTexCoord2f(u + characterWidthInTexture, v);
			GL11.glVertex2f(x+(i*characterStep)+characterWidth, y+characterHeight);
			
			
		}
		
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		// reset the blending

		GL11.glDisable(GL11.GL_BLEND);
	}
}
