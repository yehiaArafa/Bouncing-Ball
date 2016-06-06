
package skyBox;

import controller.Camera;
import model.DataLoaderVAO;
import model.ModelInfo;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import view.DisplayManager;


public class skyBoxRenderer {
    
    private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
        
        private static String[] TEXTURE_FILEs = {"right","left","top","bottom","back","front"};
        private static String[] N_TEXTURE_FILEs = {"nright","nleft","ntop","nbottom","nback","nfront"};
        
        private ModelInfo cube;
        private int texture;
        private int nTexture; // night texture
        private skyBoxShader shader;
        private float time = 0;
        
        public skyBoxRenderer(DataLoaderVAO loader ,Matrix4f projectionMatrix){
            cube = loader.loadToVAO(VERTICES, 3);
            texture = loader.loadCupeMap(TEXTURE_FILEs);
            nTexture = loader.loadCupeMap(N_TEXTURE_FILEs);
            shader = new skyBoxShader();
            shader.start();
            shader.connectTextureUnits();
            shader.loadProjectionMatrix(projectionMatrix);
            shader.stop();
        }
        
        public void render(Camera camera, float r , float g , float b){
            shader.start();
            shader.loadViewMatrix(camera);
            shader.loadFogColor(r, g, b);
            GL30.glBindVertexArray(cube.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            //GL13.glActiveTexture(GL13.GL_TEXTURE0);
            //GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
            bindTexture();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
            shader.stop();
        }
        
        /*private void bindTexture(){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
             GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nTexture);
            shader.loadBlendFactor(0.5f);
        }*/
        private void bindTexture(){
		time += DisplayManager.getFrameTime()* 0.9;
                //time +=  1000;
                //System.out.println("time" + DisplayManager.getFrameTime()*100);
                //System.out.println("blind");
		time %= 24000;
		int texture1;
		int texture2;
		float blendFactor;		
		if(time >= 0 && time < 7000){
                    //System.out.println("from 0 to 7000");
			texture1 = nTexture;
			texture2 = nTexture;
			blendFactor = (time - 0)/(5000 - 0);
		}else if(time >= 7000 && time < 10000){
                    //System.out.println("from  7000 to 10000");
			texture1 = nTexture;
			texture2 = texture;
			blendFactor = (time - 7000)/(10000 - 7000);
		}else if(time >= 10000 && time < 21000){
                    //System.out.println("1000 to 21000");
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 10000)/(21000 - 7000);
		}else{
                    //System.out.println("more than 21000");
			texture1 = texture;
			texture2 = nTexture;
			blendFactor = (time - 21000)/(24000 - 21000);
		}
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
             GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
            shader.loadBlendFactor(0.005f);
        
        }
}

