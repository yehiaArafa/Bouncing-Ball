
package controller;

import model.ModelAndTexuredInfo;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;

/**
 *
 * @author yehia
 */
public class Player extends Entity{

    private static final float runSpeed=0.1f;
    private static final float turnSpeed=0.5f;
    private static final float LeftRightSpeed=1f;
    private static final float gravity=0.00042f;
    private static final float jumpPower=0.15f; 
    private static final float terrainHieght=2.5f;
    private float currentRunSpeed=0;
    private float currentTurnSpeed=0;
    private float movingLeftRightSpeed=0; 
    private float upwardsSpeed=0;
    Boolean inAir=false;
    public boolean isPaused = false;
    public boolean isGameOver = false;
    public Controller controller;
    private boolean isXboxConnected = false;
    private int XboxIndex = -1;
    


    public Player(ModelAndTexuredInfo model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }
    
    
    public void move(){
        currentTurnSpeed=turnSpeed;
        
        if(movingLeftRightSpeed!=0){
         //   this.currentRunSpeed=0;
            super.increaseRotation(0, 0, this.currentTurnSpeed*DisplayManager.getFrameTime());
        }
        else{
          
           super.increaseRotation(this.currentTurnSpeed*DisplayManager.getFrameTime(),0,0);
        }
        
         this.currentRunSpeed=runSpeed;
        super.increasePosition(movingLeftRightSpeed,0,0);
       
        float distance= this.currentRunSpeed*DisplayManager.getFrameTime();
        float dx= (float)(distance*Math.sin(Math.toRadians(super.getRotY())));
        float dz=(float)(distance*Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed-=gravity*DisplayManager.getFrameTime();
        super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTime(),0);
        
        if(super.getPosition().y<=terrainHieght+1 && !inAir){
            upwardsSpeed=0;
            super.getPosition().y=terrainHieght;
        }
        
        if(super.getPosition().y<=terrainHieght+1 && inAir){
            upwardsSpeed=0.09f;
            upwardsSpeed-=gravity*DisplayManager.getFrameTime();
             super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTime(),0);
            inAir=false;  
        }
        
   
    }
    
    public void checkInputXbox() throws LWJGLException {
        for (int i = 0; i < Controllers.getControllerCount(); i++) {
            if(Controllers.getController(i).getName().equalsIgnoreCase("Controller (XBOX 360 For Windows)")) {
                isXboxConnected = true;
                XboxIndex = i;
                break;
            }
        }
        if(isXboxConnected) {
            controller = Controllers.getController(XboxIndex);
            controller.poll();
        }
        if(isXboxConnected) {
            
        if(controller.isButtonPressed(0)) {
            if(!inAir){
                upwardsSpeed=jumpPower;
                inAir=true;
            }
        } else if(controller.isButtonPressed(7)) {
            isPaused = !isPaused;
        } 
        if(controller.getPovX()>0) {
            movingLeftRightSpeed=LeftRightSpeed;    
        } else if(controller.getPovX()<0) {
            movingLeftRightSpeed=-LeftRightSpeed;    
        }       
        }
        
        
    }
    
    
    
    public void checkInputs(){        
                
       if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
          movingLeftRightSpeed=LeftRightSpeed;    
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
             movingLeftRightSpeed=-LeftRightSpeed;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
            isPaused = !isPaused;
        }
        else{ 
            movingLeftRightSpeed=0;
        }
          
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            if(!inAir){
                upwardsSpeed=jumpPower;
                inAir=true;
            }
        }
    }
    
    
    
    
}
