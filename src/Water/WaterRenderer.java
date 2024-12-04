package Water;

import java.util.List;
import Models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import RenderEngine.Loader;
import ToolBox.Maths;
import Entities.Camera;

public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.Start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.Stop();
		SetUpVAO(loader);
	}

	public void Render(List<WaterTile> water, Camera camera) {
		PrepareRender(camera);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransofrmationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
					new Vector3f(0f, 0f, 0f),
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		Unbind();
	}
	
	private void PrepareRender(Camera camera){
		shader.Start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}
	
	private void Unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.Stop();
	}

	private void SetUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.LoadtoVAO(vertices, 2);
	}

}
