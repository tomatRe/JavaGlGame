package Shaders;

import Entities.Camera;
import Entities.Light;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class StaticShader extends ShaderProgram{

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/Shaders/VertexShader";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_refelectivity;
    private int location_useFakeLightning;
    private int location_skyColour;

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

        location_shineDamper =
                super.GetUniformLocation("shineDamper");

        location_refelectivity =
                super.GetUniformLocation("refelectivity");

        location_useFakeLightning =
                super.GetUniformLocation("useFakeLightning");

        location_skyColour =
                super.GetUniformLocation("skyColour");

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

    public void LoadSkyColour(float r, float g, float b){
        super.LoadVector(location_skyColour, new Vector3f(r,g,b));

    }

    public void LoadFakeLightningVariable(boolean useFake){
        super.LoadBoolean(location_useFakeLightning, useFake);
    }

    public void LoadLights(List<Light> lights){
        for (int i = 0; i < MAX_LIGHTS; i++){
            if (i<lights.size()){
                super.LoadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.LoadVector(location_lightColour[i], lights.get(i).getColour());
                super.LoadVector(location_attenuation[i], lights.get(i).getAttenuation());
            }
            else{
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
