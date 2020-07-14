package Entities;

import Models.TextureModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

    private TextureModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Entity(TextureModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void IncreasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void IncreaseRotation(float dx,float dy,float dz){
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;
    }

    public void IncreaseScale(float ds){
        this.scale += ds;
    }

    public TextureModel getModel() {
        return model;
    }

    public void setModel(TextureModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
