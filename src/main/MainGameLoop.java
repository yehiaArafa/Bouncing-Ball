package main;

import java.io.IOException;
import org.lwjgl.LWJGLException;
import view.Menu;
import view.TheView;


public class MainGameLoop {
 
    public static void main(String[] args) throws IOException, LWJGLException, InterruptedException {

        Menu mymenu = new Menu();
        mymenu.menu();
        
        //TheView v = new TheView();
        //v.view();

    }

}
