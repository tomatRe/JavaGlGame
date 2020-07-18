package Models;

import RenderEngine.Loader;
import Textures.ModelTexture;

public class TextureModel {

    private RawModel rawModel;
    private ModelTexture texture;

    public TextureModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public TextureModel(RawModel rawModel) {

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
