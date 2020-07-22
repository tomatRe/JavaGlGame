package Shaders;

import Entities.Camera;
import Entities.Light;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/VertexShader";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;

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

        location_lightPosition =
                super.GetUniformLocation("lightPosition");

        location_lightColour =
                super.GetUniformLocation("lightColour");
    }

    @Override
    protected void BindAttributes() {
        super.BindAttribute(0,"position");
        super.BindAttribute(1, "textureCoords");
        super.BindAttribute(2, "normals");
    }

    public void LoadLight(Light light){
        super.LoadVector(location_lightPosition, light.getPosition());
        super.LoadVector(location_lightColour, light.getColour());
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
