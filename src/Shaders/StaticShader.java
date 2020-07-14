package Shaders;

import Entities.Camera;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/VertexShader";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void GetAllUniformLocations() {
        location_transformationMatrix =
                super.GetUniformLocation("transformationMatrix");

        location_projectionMatrix =
                super.GetUniformLocation("projectionMatrix");

        location_viewMatrix =
                super.GetUniformLocation("viewMatrix");
    }

    @Override
    protected void BindAttributes() {
        super.BindAttribute(0,"position");
        super.BindAttribute(1, "textureCoords");
    }

    public void LoadTransformationMatrix(Matrix4f matrix){
        super.LoadMatrix(location_transformationMatrix, matrix);
    }

    public void LoadProjectionMatrix(Matrix4f projection){
        super.LoadMatrix(location_projectionMatrix, projection);
    }

    public void LoadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.CreateViewMatrix(camera);
        super.LoadMatrix(location_viewMatrix, viewMatrix);
    }
}
