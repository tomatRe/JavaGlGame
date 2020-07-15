package RenderEngine;

import Models.RawModel;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {

    public static RawModel LoadObjModel(String fileName, Loader loader){

        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        try {
            FileReader fr = new FileReader(new File("res/"+fileName+".obj"));
            BufferedReader reader = new BufferedReader(fr);
            String line;

            int lineIndex = 0;

            while (true){
                line = reader.readLine();

                if (line != null && !line.equals("")){
                    String[] currentLine = line.split(" ");

                    if (line.startsWith("v ") &&
                            !currentLine[1].equals("") && !currentLine[2].equals("") && !currentLine[3].equals("")){
                        Vector3f vertex = new Vector3f(
                                Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]),
                                Float.parseFloat(currentLine[3])
                        );
                        vertices.add(vertex);
                    }else if(line.startsWith("vt ") &&
                            !currentLine[1].equals("") && !currentLine[2].equals("")){
                        Vector2f texture = new Vector2f(
                                Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2])
                        );
                        textures.add(texture);
                    }
                    else if(line.startsWith("vn ")
                    && !currentLine[1].equals("") && !currentLine[2].equals("") && !currentLine[3].equals("")){
                        Vector3f normal = new Vector3f(
                                Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]),
                                Float.parseFloat(currentLine[3])
                        );
                        normals.add(normal);
                    }
                    else if (line.startsWith("f ")){
                        texturesArray = new float[vertices.size() * 2];
                        normalsArray = new float[vertices.size()*3];
                        break;
                    }
                }
            }

            while (line != null){
                if (!line.startsWith("f ")){
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);

                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("The file "+fileName+".obj Could not be loaded!!");
            e.printStackTrace();
            System.exit(-1);
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        for (Vector3f vertex: vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i<indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }

        return loader.LoadtoVAO(verticesArray,texturesArray, indicesArray);
    }

    private static void processVertex
            (String[] vertexData, List<Integer> indices,
             List<Vector2f> textures, List<Vector3f> normals,
             float[] textureArray, float[] normalsArray){

        int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = 1-currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }
}
