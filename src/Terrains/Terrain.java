package Terrains;

import Models.RawModel;
import RenderEngine.Loader;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import ToolBox.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 4;
    private static final float MAX_PIXEL_COLOUR = 256*256*256;

    private float x;
    private float z;

    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = GenerateTerrain(loader, heightMap);
    }

    private RawModel GenerateTerrain(Loader loader, String heightMap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("HEIGHT MAP LOADER CRASHED");
        }

        int VERTEX_COUNT = image.getHeight();
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){

                float height = getHeight(j,i,image);
                heights[j][i] = height;

                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;

                Vector3f normal = CalculateNormal(j,i,image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.LoadtoVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int y, BufferedImage image){
        if (x<0 || x>=image.getHeight() || y<0 || y>=image.getHeight())
            return 0;

        float height = image.getRGB(x,y);

        height += MAX_PIXEL_COLOUR/2f;
        height /= MAX_PIXEL_COLOUR/2f;
        height *= MAX_HEIGHT;

        return  height;
    }

    public float getHeight(float worldX, float worldZ){
        float terrainX = worldX - x;
        float terrainZ = worldZ - z;
        float gridSquareSize = SIZE / (heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length -1 || gridZ >= heights.length-1 ||
                gridX < 0 || gridZ < 0){

            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        //All of this is for the interpolation between vertexes on a given triangle on the terrain
        if (xCoord <= (1-zCoord)) {
            answer = Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }

    private Vector3f CalculateNormal(int x, int y, BufferedImage image){
        float heightL = getHeight(x-1,y,image);
        float heightR = getHeight(x+1,y,image);
        float heightD = getHeight(x,y-1,image);
        float heightU = getHeight(x,y+1,image);

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();

        return normal;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
