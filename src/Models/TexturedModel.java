package Models;

import RenderEngine.Loader;
import Textures.ModelTexture;

public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public TexturedModel(RawModel rawModel) {

        Loader loader = new Loader();

        this.rawModel = rawModel;
        this.texture = new ModelTexture(loader.LoadTexture(""));
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
