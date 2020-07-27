package Shaders;

import Entities.Camera;
import Entities.Light;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/TerrainVertexShader";
    private static final String FRAGMENT_FILE = "src/Shaders/TerrainFragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_refelectivity;
    private int location_skyColour;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendmap;

    public TerrainShader() {
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

        location_shineDamper =
                super.GetUniformLocation("shineDamper");

        location_refelectivity =
                super.GetUniformLocation("refelectivity");

        location_skyColour =
                super.GetUniformLocation("skyColour");

        location_backgroundTexture =
                super.GetUniformLocation("backgroundTexture");

        location_rTexture =
                super.GetUniformLocation("rTexture");

        location_gTexture =
                super.GetUniformLocation("gTexture");

        location_bTexture =
                super.GetUniformLocation("bTexture");

        location_blendmap =
                super.GetUniformLocation("blendMap");
    }

    @Override
    protected void BindAttributes() {
        super.BindAttribute(0,"position");
        super.BindAttribute(1, "textureCoords");
        super.BindAttribute(2, "normals");
    }

    public void ConnectTextureUnits(){
        super.LoadInt(location_backgroundTexture, 0);
        super.LoadInt(location_rTexture, 1);
        super.LoadInt(location_gTexture, 2);
        super.LoadInt(location_bTexture, 3);
        super.LoadInt(location_blendmap, 4);
    }

    public void LoadSkyColour(float r, float g, float b){
        super.LoadVector(location_skyColour, new Vector3f(r,g,b));
    }

    public void LoadLight(Light light){
        super.LoadVector(location_lightPosition, light.getPosition());
        super.LoadVector(location_lightColour, light.getColour());
    }

    public void LoadShineVariables(float damper, float reflectivity){
        super.LoadFloat(location_refelectivity, reflectivity);
        super.LoadFloat(location_shineDamper, damper);
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
