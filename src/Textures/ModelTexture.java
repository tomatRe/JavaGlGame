package Textures;

public class ModelTexture {

    private int textureID;
    private float shineDumper = 1;
    private float reflectivity = 0;

    private boolean hasTransparecy = false;

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public boolean hasTransparecy() {
        return hasTransparecy;
    }

    public void setHasTransparecy(boolean hasTransparecy) {
        this.hasTransparecy = hasTransparecy;
    }

    public int getID() {
        return textureID;
    }

    public float getShineDumper() {
        return shineDumper;
    }

    public void setShineDumper(float shineDumper) {
        this.shineDumper = shineDumper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
