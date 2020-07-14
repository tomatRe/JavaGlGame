package RenderEngine;

import Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel LoadtoVAO(float[] positions,float[] textureCoords, int[] indices){
        int vaoID = CreateVAO();
        BindIndicesBuffer(indices);
        StoreDataInAttributeList(0,3, positions);
        StoreDataInAttributeList(1,2, textureCoords);
        UnbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    private int CreateVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void StoreDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT,
                false, 0 , 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private void BindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = StoreDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }

    private IntBuffer StoreDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int LoadTexture(String fileName){
        Texture texture = null;

        try {
            texture = TextureLoader.getTexture("PNG",new FileInputStream("res/"+fileName+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        textures.add(texture.getTextureID());
        return texture.getTextureID();
    }

    private void UnbindVAO(){
        GL30.glBindVertexArray(0);
    }

    public void CleanUp(){
        for (int vao:vaos)
            GL30.glDeleteVertexArrays(vao);

        for (int vbo:vbos)
            GL15.glDeleteBuffers(vbo);

        for (int texture:textures)
            GL11.glDeleteTextures(texture);
    }
}
