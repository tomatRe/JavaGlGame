package RenderEngine;

import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int maxFps = 120;


    public static void CreateDisplay(){
        ContextAttribs attribs = new ContextAttribs(3,2);
        attribs.withForwardCompatible(true);
        attribs.withProfileCore(true);

        try{
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("JavaGlGame");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0,WIDTH,HEIGHT);
    }

    public static void UpdateDisplay(){
        Display.sync(maxFps);
        Display.update();
    }

    public static void CloseDisplay(){
        Display.destroy();
    }
}
