package Shaders;

import Entities.Camera;
import Entities.Light;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainShader extends ShaderProgram{

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/Shaders/TerrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/Shaders/TerrainFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_attenuation[];
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

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];

        for (int i = 0; i < MAX_LIGHTS; i++){
            location_lightPosition[i] = super.GetUniformLocation("lightPosition["+i+"]");
            location_lightColour[i] = super.GetUniformLocation("lightColour["+i+"]");
            location_attenuation[i] = super.GetUniformLocation("attenuation["+i+"]");
        }
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

    public void LoadLights(List<Light> lights){
        for (int i = 0; i < MAX_LIGHTS; i++){
            if (i<lights.size()){
                super.LoadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.LoadVector(location_lightColour[i], lights.get(i).getColour());
                super.LoadVector(location_attenuation[i], lights.get(i).getAttenuation());
            }else{
                super.LoadVector(location_lightPosition[i], new Vector3f(0,0,0));
                super.LoadVector(location_lightColour[i], new Vector3f(0,0,0));
                super.LoadVector(location_attenuation[i], new Vector3f(1,0,0));
            }

        }
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
