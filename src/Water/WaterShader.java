package Water;

import org.lwjgl.util.vector.Matrix4f;
import Shaders.ShaderProgram;
import ToolBox.Maths;
import Entities.Camera;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.glsl";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void BindAttributes() {
		BindAttribute(0, "position");
		BindAttribute(1, "texCoord");
	}

	@Override
	protected void GetAllUniformLocations() {
		location_projectionMatrix = GetUniformLocation("projectionMatrix");
		location_viewMatrix = GetUniformLocation("viewMatrix");
		location_modelMatrix = GetUniformLocation("modelMatrix");
		location_reflectionTexture = GetUniformLocation("reflectionTexture");
		location_refractionTexture = GetUniformLocation("refractionTexture");
	}

	public void connectTextureUnits(){
		super.LoadInt(location_reflectionTexture, 0);
		super.LoadInt(location_refractionTexture, 1);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		LoadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.CreateViewMatrix(camera);
		LoadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		LoadMatrix(location_modelMatrix, modelMatrix);
	}
}
