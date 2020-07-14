package ToolBox;

import Entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

    public static Matrix4f createTransofrmationMatrix
            (Vector3f transform, Vector3f rotation, float scale){

        float rx = rotation.x,ry = rotation.y,rz = rotation.z;

        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(transform, matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix,matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(1,1,0), matrix,matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(1,0,1), matrix,matrix);

        Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);

        return matrix;
    }

    public static Matrix4f CreateViewMatrix(Camera camera){
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate
                ((float) Math.toRadians(camera.getPitch()),
                        new Vector3f(1,0,0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()),
                new Vector3f(0,1,0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix,viewMatrix);

        return viewMatrix;
    }
}
