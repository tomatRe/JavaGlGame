package Skybox;

import RenderEngine.DisplayManager;
import org.lwjgl.util.vector.Matrix4f;
import Entities.Camera;
import Shaders.ShaderProgram;
import ToolBox.Maths;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/Shaders/skyboxVertexShader";
	private static final String FRAGMENT_FILE = "src/Shaders/skyboxFragmentShader";

	private final float rotationSpeed = 1;
	private float rotation = 0;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColour;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void LoadProjectionMatrix(Matrix4f matrix){
		super.LoadMatrix(location_projectionMatrix, matrix);
	}

	public void LoadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.CreateViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += rotationSpeed * DisplayManager.GetFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		super.LoadMatrix(location_viewMatrix, matrix);
	}

	public void LoadFogColour(Vector3f fogColour){
		super.LoadVector(location_fogColour, fogColour);
	}
	
	@Override
	protected void GetAllUniformLocations() {
		location_projectionMatrix = super.GetUniformLocation("projectionMatrix");
		location_viewMatrix = super.GetUniformLocation("viewMatrix");
		location_fogColour = super.GetUniformLocation("fogColour");
	}

	@Override
	protected void BindAttributes() {
		super.BindAttribute(0, "position");
	}

}
