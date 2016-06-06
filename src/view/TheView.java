/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Camera;
import controller.Enemy;
import controller.Entity;
import controller.Light;
import controller.MasterRenderer;
import controller.Player;
import controller.Terrain;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import model.DataLoaderVAO;
import model.ModelTexture;
import model.OBJLoader;
import model.ModelInfo;
import model.ModelAndTexuredInfo;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.easyogg.OggClip;
import org.newdawn.slick.openal.Audio;

/**
 *
 * @author yehia
 */
public class TheView {
public static int Score;
public boolean isPaused = false;
private static int y=0;
private float lastx = -1;
private float lastz = -1;
private float lasty = -1;
private boolean negFlag = false;
private boolean isGameOver = false;
private int gameovercount = 0;
private ArrayList<Integer> xvals = new ArrayList();
private ArrayList<Integer> zvals = new ArrayList();
private OggClip inGameMusic;

    public void play_in_game() throws IOException {
        inGameMusic = new OggClip(new FileInputStream("Overworld.ogg"));
        inGameMusic.loop();
    }
    
    public void play_game_over() throws IOException {
        inGameMusic.stop();
        inGameMusic = new OggClip(new FileInputStream("Gameover.ogg"));
        inGameMusic.loop();
    }

    
    public void getIntelligentPosition() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            negFlag = !negFlag;
            int X_pos = rand.nextInt(25);
            int Z_pos = rand.nextInt(50000)+1000;
            if(negFlag) {
                xvals.add(-1*X_pos);
                zvals.add(-1*Z_pos);
            } else {
                xvals.add(X_pos);
                zvals.add(-1*Z_pos);
            }
        }
        //Collections.sort(zvals);
    }
    
    public void collided() throws IOException {
        for (int i = 0; i < 1000; i++) {
            if(lastz <= zvals.get(i)+22 && lastz>=zvals.get(i)-22 && lastx <= xvals.get(i)+2.5 && lastx>=xvals.get(i)-2.5 &&
                    lasty == 2.5f) {
                isGameOver = true;
                System.out.println(isGameOver);
            }
        }
    }

    public void view() throws IOException, LWJGLException, InterruptedException {
        play_in_game();
        Random rand = new Random();
        DataLoaderVAO loader = new DataLoaderVAO();

        ModelInfo model = OBJLoader.loadObjModel("laastsphere", loader);

        ModelAndTexuredInfo staticModel = new ModelAndTexuredInfo(model, new ModelTexture(loader.loadTexture("ball", "jpg")));

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();

        Player myPlayer = new Player(staticModel, new Vector3f(10, 10, 10), 0, 180, 0, 1.5f);

        Light light = new Light(new Vector3f(350, 2000, 200), new Vector3f(1, 1, 1));

        List<Terrain> myList=new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        
        for(int i=0;i>-100;i--){
        Terrain terrain = new Terrain(0.5f/16, i, loader, new ModelTexture(loader.loadTexture("Road", "jpg")));
         myList.add(terrain);
        }
        
        Camera camera = new Camera(myPlayer);
        MasterRenderer renderer = new MasterRenderer(loader);
        
       

        
        ModelInfo model2 = OBJLoader.loadObjModel("CrateModel",loader);
        ModelAndTexuredInfo staticModel2 = new ModelAndTexuredInfo(model2, new ModelTexture(loader.loadTexture("wood","jpg")));
        //guis.add(gui);
        
       
        
        getIntelligentPosition();
        for (int i = 0; i < 1000; i++) {
            Enemy enemy = new Enemy(staticModel2,new Vector3f(xvals.get(i),0,zvals.get(i)),0,180,0,0.1f);
            enemies.add(enemy);
        }      
        
        while (!Display.isCloseRequested()) {
           
            if(y%2==0)
            {
                Score++;
            }
            y++;
            Menu.writeFont(450,500,"Score : "+Integer.toString(Score));

            myPlayer.checkInputs();
            myPlayer.checkInputXbox();
 
            for(Terrain terrain:myList){
                renderer.processTerrain(terrain); 
            }
            
            renderer.processEntity(myPlayer);
            this.lastx = myPlayer.getPosition().x;
            this.lastz = myPlayer.getPosition().z;
            this.lasty = myPlayer.getPosition().y;
            for (int i = 0; i < 1000; i++) {
                Enemy enemy = enemies.get(i);
                renderer.processEntity(enemy);
            }
            
            
            light.move();
            collided();
            
            
            if(!myPlayer.isPaused && !isGameOver && !myPlayer.outOfRange) {
                camera.move();
                myPlayer.move();    
            } else {
                if(myPlayer.outOfRange) {
                    myPlayer.getPosition().y -= 2;
                }
                if(gameovercount==0) {
                    play_game_over();
                    Menu.writeFont(450,500,"GAME OVER");
                    gameovercount++;
                }
            }
                       
            renderer.render(light, camera);
            
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUP();
        DisplayManager.closeDisplay();

    }
}