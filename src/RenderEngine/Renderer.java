package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    public void Prepare(){
        GL11.glClearColor(0,0,0,1f);//BACKGROUND COLOR
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void Render(RawModel model){
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0 , model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
