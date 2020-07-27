package Shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = LoadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = LoadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        BindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        GetAllUniformLocations();
    }

    protected abstract void GetAllUniformLocations();

    protected int GetUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void LoadInt(int location, int value){
        GL20.glUniform1i(location, value);
    }

    protected void LoadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void LoadVector(int location, Vector3f value){
        GL20.glUniform3f(location,value.x,value.y,value.z);
    }

    protected void LoadBoolean(int location, boolean value){
        float toLoad = 0;
        if (value)
            toLoad = 1;
        GL20.glUniform1f(location, toLoad);
    }

    protected void LoadMatrix(int location, Matrix value){
        value.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location,false, matrixBuffer);
    }

    public void Start(){
        GL20.glUseProgram(programID);
    }

    public void Stop(){
        GL20.glUseProgram(0);
    }

    public void CleanUp(){
        Stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected void BindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected abstract void BindAttributes();

    private static int LoadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }

            reader.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Shader Compilation Error!!!");
            System.exit(-1);
        }

        return shaderID;
    }
}
