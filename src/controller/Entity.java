package controller;

import model.ModelAndTexuredInfo;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private ModelAndTexuredInfo model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
        public Boolean outOfRange=false; 

	public Entity(ModelAndTexuredInfo model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		 
         
            if(this.position.getX()>=28f || this.position.getX()<=-28f ){
                outOfRange=true;
                
                /*
                this.position.x += dx;
                if(this.position.getX()==33f)
                    this.position.x--;
                if(this.position.getX()==-33f)
                    this.position.x++;
                */
            }
             
            if(!this.outOfRange){
                    this.position.x += dx;
            }
            else{
                //this.position.x += dx;
//                if(this.position.getX()==33f) 
//                {
//                    this.position.x--;
//                }
//                    
//                if(this.position.getX()==-33f)
//                {
//                    this.position.x++;
//                }
            }      
                this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public ModelAndTexuredInfo getModel() {
		return model;
	}

	public void setModel(ModelAndTexuredInfo model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
