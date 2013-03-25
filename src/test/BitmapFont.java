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
	
	public void drawString(int font, String text, int x, int y, Color flagcolor) {
		texture.bind();


		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);


		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(flagcolor.r, flagcolor.g, flagcolor.b);
		for (int i=0;i<text.length();i++) {

			int c =text.charAt(i) - ' ';


			float u = ((c % charactersAcross) * characterWidthInTexture);
			float v = 1-((7-(c / charactersAcross)) * characterHeightInTexture);
			//System.out.print(" " + ((c / charactersAcross)));
			
			
			v -= font * 0.5f;
			//System.out.print(" :" + (y+characterHeight)+ " " + (x+(i*characterStep)+characterWidth) + " " + v + ": ");
			
			
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

		GL11.glDisable(GL11.GL_BLEND);
	}
}
