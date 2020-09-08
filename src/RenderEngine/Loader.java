package RenderEngine;

import Models.RawModel;
import Textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel LoadtoVAO(float[] positions,float[] textureCoords,float[] normals, int[] indices){
        int vaoID = CreateVAO();
        BindIndicesBuffer(indices);
        StoreDataInAttributeList(0,3, positions);
        StoreDataInAttributeList(1,2, textureCoords);
        StoreDataInAttributeList(2,3, normals);
        UnbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel LoadtoVAO(float[] positions, int dimensions){
        int vaoID = CreateVAO();
        this.StoreDataInAttributeList(0,dimensions,positions);
        UnbindVAO();
        return new RawModel(vaoID, positions.length/dimensions);
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
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT,
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

        if(fileName.equals(""))
            fileName = "deftext";

        try {
            texture = TextureLoader.getTexture("PNG",new FileInputStream("res/"+fileName+".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textures.add(texture.getTextureID());
        return texture.getTextureID();
    }

    public int LoadCubemap(String[] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i<textureFiles.length;i++){
            TextureData data = DecodeTextureFile("res/" + textureFiles[i]+".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,0, GL11.GL_RGBA,
                    data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        textures.add(texID);
        return texID;
    }

    private TextureData DecodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
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
