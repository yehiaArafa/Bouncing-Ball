
package model;

import controller.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


/* This class for loading 3d models into memory by loading data about the model in a VAO */

public class DataLoaderVAO {
        
    private ArrayList <Integer>VAO_IDS=new ArrayList<Integer>();
    private ArrayList <Integer>VBO_IDS=new ArrayList<Integer>();
    private ArrayList <Integer>Texture_IDS=new ArrayList<Integer>();
    
//==================================================================
/* 
    Makes new VAO(memory)- load positions of model and texure coordinates into 
    this VAO and return that new model 
*/     
    public ModelInfo loadToVAO(float[]data,float[]texture,float[]normals,int[]indexData)
    {
        int VAO_ID=this.createVAO_ID();
        VAO_IDS.add(VAO_ID);
        this.make_index_buffer_into_VBO(indexData);
        this.store_data_in_VBO(0,3, data);
        this.store_data_in_VBO(1,2, texture);
        this.store_data_in_VBO(2,3, normals);
         this.unbind_VAO();
        return new ModelInfo(VAO_ID,indexData.length);//  /3 because no of vertex
    }
    
    public ModelInfo loadToVAO(float[] positions , int dimensions){
        int VAO_ID = this.createVAO_ID();
        this.store_data_in_VBO(0, dimensions, positions);
        this.unbind_VAO();
        return new ModelInfo(VAO_ID,positions.length/dimensions);
    }
    
    public int loadCupeMap(String[] textureFiles){
        
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
        
        for(int i = 0 ; i<textureFiles.length ;i++){
        TextureData data = decodeTextureFile("res/" + textureFiles[i] +".png");
        GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X +i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        
         Texture_IDS.add(texID);
         return texID;
    
    }
    
    private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

//==================================================================
 /*
      create new empty VAO and return its ID
 */
    private int createVAO_ID()
    {
        int VAO_ID=GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO_ID);
        return VAO_ID;
    }

//==================================================================
/*
  store the data into one of the attribute lists of the VAO  
*/   
   private void store_data_in_VBO(int attribListNum,int sizeCoordinate,float[]data)
    {
        //make VBO
        int VBO_ID=GL15.glGenBuffers();
        VBO_IDS.add(VBO_ID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
        FloatBuffer buffer=this.store_data_in_FloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);//7TTHA f vbo
        GL20.glVertexAttribPointer(attribListNum,sizeCoordinate,GL11.GL_FLOAT,false, 0, 0);//7tetl VBO fe VAO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }
 
//==================================================================
/*
  change form float array to float buffer    
*/ 
   private FloatBuffer store_data_in_FloatBuffer(float[]data)
    {
        FloatBuffer buffer=BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;     
    }
 //==================================================================
 /*
    take the indices and bind it to vbo
 */  
   private void make_index_buffer_into_VBO(int[]indexData)
   {
        int VBO_ID=GL15.glGenBuffers();
        VBO_IDS.add(VBO_ID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO_ID);
        IntBuffer buffer=this.store_data_in_IntBuffer(indexData);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }
  
//==================================================================
/*
   change form float array to int buffer
*/
   private IntBuffer store_data_in_IntBuffer(int[]indexData)
    {
        IntBuffer buffer=BufferUtils.createIntBuffer(indexData.length);
        buffer.put(indexData);
        buffer.flip();
        return buffer;     
    }
 //==================================================================
 
   private void unbind_VAO()
    {
        GL30.glBindVertexArray(0);
    }
  
//==================================================================
/*
   load up a texure into memory and return ID of that texure 
*/
   public int loadTexture(String file,String filetype) throws IOException
   {
       Texture texture=null;
       texture=TextureLoader.getTexture(filetype,new FileInputStream("res/"+file+"."+filetype));
       GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
       GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
       int textureID=texture.getTextureID();
       Texture_IDS.add(textureID);
       return textureID;
   }
    
 //==================================================================
 /*
   cleans up vao-vbo-texures ids from memory
 */   
    public void cleanUP()
    {
        for(int vao:VAO_IDS)
        {
            GL30.glDeleteVertexArrays(vao);       
        }
        for(int vbo:VBO_IDS)
        {
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:Texture_IDS)
        {
            GL11.glDeleteTextures(texture);   
        }    
    }
 
    
    
}