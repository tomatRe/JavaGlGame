package Shaders;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = LoadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = LoadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
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
