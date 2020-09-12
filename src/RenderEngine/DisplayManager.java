package RenderEngine;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int maxFps = 0;

    private static long lastFrameTime;
    private static float delta;

    //FPS STATS
    private static int fps = 0;
    private static float secondsCounter = 0;

    public static void CreateDisplay(){
        ContextAttribs attribs = new ContextAttribs(3,2).
                withForwardCompatible(true).
                withProfileCore(true);

        try{
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("JavaGlGame");

            Mouse.create();
            Mouse.setGrabbed(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0,WIDTH,HEIGHT);
        lastFrameTime = GetCurrentTime();
    }

    public static void UpdateDisplay(){
        Display.sync(maxFps);
        Display.update();
        long currentFrameTime = GetCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;

        ShowFps();
    }

    public static float GetFrameTimeSeconds(){
        return delta;
    }

    public static void ShowFps(){
        secondsCounter += delta;
        fps++;
        if (secondsCounter >= 1){
            System.out.println(fps + "Fps - "+ delta +"ms");
            secondsCounter = 0;
            fps = 0;
        }
    }

    private static long GetCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

    public static void CloseDisplay(){
        Display.destroy();
        Mouse.destroy();
    }
}
