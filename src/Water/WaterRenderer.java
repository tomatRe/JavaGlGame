package Water;

import java.util.List;
import Models.RawModel;

import Models.TexturedModel;
import Textures.ModelTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import RenderEngine.Loader;
import ToolBox.Maths;
import Entities.Camera;

public class WaterRenderer {

	private TexturedModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;

	public WaterRenderer(Loader loader, Matrix4f projectionMatrix, WaterShader shader, WaterFrameBuffers fbos) {
		this.shader = shader;
		this.fbos = fbos;

		shader.Start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.Stop();
		SetUpVAO(loader);
	}

	public void Render(List<WaterTile> water, Camera camera) {
		WaterTile tile = water.get(0);

		Matrix4f modelMatrix = Maths.createTransofrmationMatrix(
				new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
				new Vector3f(0f, 0f, 0f),
				WaterTile.TILE_SIZE);

		PrepareRender(camera);
		shader.loadModelMatrix(modelMatrix);
		GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		Unbind();
	}
	
	private void PrepareRender(Camera camera){
		shader.Start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(quad.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2); // Normals (not used yet)

		GL11.glDisable(GL11.GL_CULL_FACE); // Para test
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Reflection
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());

		// Refraction
		//GL13.glActiveTexture(GL13.GL_TEXTURE1);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
	}
	
	private void Unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);

		GL11.glEnable(GL11.GL_CULL_FACE); // Para test
		GL11.glDisable(GL11.GL_BLEND);

		shader.Stop();
	}

	private void SetUpVAO(Loader loader) {
		float[] vertices = {
				-1f, -1f, 0f, // Vértice 1
				 1f, -1f, 0f, // Vértice 2
				 1f,  1f, 0f, // Vértice 3
				-1f,  1f, 0f  // Vértice 4
		};

		float[] textureCoords = {
				0.0f, 0.0f,  // Vértice 1
				1.0f, 0.0f,  // Vértice 2
				1.0f, 1.0f,  // Vértice 3
				0.0f, 1.0f   // Vértice 4
		};
		float[] normals = {
				0.0f, 0.0f, 1.0f,  // Normal para cada vértice
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f
		};

		int[] indices = {
				0, 1, 2,  // Primer triángulo
				0, 2, 3   // Segundo triángulo
		};

		// Cargar tanto los vértices como las coordenadas de textura en el VAO
		RawModel rawModel = loader.LoadtoVAO(vertices, textureCoords, normals, indices);
		quad = new TexturedModel(rawModel, new ModelTexture(fbos.getReflectionTexture()));
	}
}
