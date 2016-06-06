
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.Resource;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glRectf;
import org.newdawn.easyogg.OggClip;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author loai Aboelsooud
 */


public class Menu {

    private static enum State {

        INTRO, GAME;
    }
    private State state = State.INTRO;
    private boolean isXboxConnected = false;
    private int XboxIndex = -1;
    public static TrueTypeFont font;
    public boolean antiAlias = true;
    private static Texture texture;
    public Controller controller;
    
    public static void loadFont() {
        Font awtFont = new Font("Times New Roman", Font.BOLD, 50);
        font = new TrueTypeFont(awtFont, false);

    }

    public static void writeFont(int BL, int BU, String x) {
        loadFont();
        Color.white.bind();
        font.drawString(BL, BU, x, Color.decode("#e0682a"));
    }
    
    private OggClip inGameMusic;

    public void play_in_game() throws IOException {
        inGameMusic = new OggClip(new FileInputStream("Course.ogg"));
        inGameMusic.loop();
    }
    
    public void stop_in_game() {
        inGameMusic.stop();
    }

    public static void loadImage() {

        try {
            // load texture from PNG file
        texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/lastisaasphere.png"));

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawImage() {
        loadImage();
        Color.white.bind();
        texture.bind(); // or GL11.glBind(texture.getTextureID());

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(400, 200);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(1200 , 220);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(100 + texture.getTextureWidth(), 80 + texture.getTextureHeight());
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(350, 100 + texture.getTextureHeight());
        GL11.glEnd();
    }

    public void menu() throws LWJGLException, IOException, InterruptedException {

        switch (state) {
            case GAME:
                TheView v = new TheView();
                stop_in_game();
                v.view();
                break;
            case INTRO:
                DisplayManager.createDisplay();
                Display.setVSyncEnabled(true);
                loadFont();
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                play_in_game();
                while (!Display.isCloseRequested()) {
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                    drawImage();
                    writeFont(300, 500, "Space bar or start button to start");
                    checkInput();
                    Display.update();
                    Display.sync(60);
                }
                
                Display.destroy();

                break;
        }
    }

    public void checkInput() throws IOException, LWJGLException, InterruptedException {
        Controllers.create();
        Controllers.poll(); 
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
        switch (state) {
            case INTRO:
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    state = state.GAME;
                    menu();
                }
                if(isXboxConnected) {
                   if (controller.isButtonPressed(7)) {
                        state = state.GAME;
                        menu();
                    } 
                }
                break;

       
        }
    }

}