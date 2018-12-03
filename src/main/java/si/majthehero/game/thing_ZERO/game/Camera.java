package si.majthehero.game.thing_ZERO.game;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    float eye_x;
    float eye_y;
    float eye_z;

    float tgt_x;
    float tgt_y;
    float tgt_z;

    float rot_up;
    float rot_left;

    Matrix4f pr_matrix;

    double dT;

    public Camera() {
        eye_x = -3;
        eye_y = -2;
        eye_z = 12;
        rot_up = 0;
        rot_left = 0;
        tgt_x = 7;
        tgt_y = 7;
        tgt_z = 0;
        dT = 0.0;

        pr_matrix = new Matrix4f();
    }

    public void move(float x, float y, float z) {
        eye_x += x * dT;
        eye_y += y * dT;
        eye_z += z * dT;
        tgt_x += x * dT;
        tgt_y += y * dT;
        tgt_z += z * dT;
    }

    public void rotate(float up, float left) {
        Vector3f direction = new Vector3f(
                eye_x - tgt_x,
                eye_y - tgt_y,
                eye_z - tgt_z);
        Matrix3f rot = new Matrix3f();
        rot.rotate((float) (left * dT), new Vector3f(0,0,1))
            .rotate((float) (up * dT), new Vector3f(1,0,0));
        direction.mulTranspose(rot);
        tgt_x = eye_x + direction.x;
        tgt_y = eye_y + direction.y;
        tgt_z = eye_z + direction.z;
    }

    public void update() {
        System.out.println("Camera at: " +
                eye_x + " " + eye_y + " " + eye_z);
        System.out.println("Looking at: " +
                tgt_x + " " + tgt_y + " " + tgt_z);
        pr_matrix = new Matrix4f();

        pr_matrix
            .perspective(
                (float)Math.toRadians(45d),
                16.0f/10.0f, 0.1f, 100.0f)
            .lookAt(eye_x, eye_y, eye_z,
                tgt_x, tgt_y, tgt_z,
                0, 0, 1);
    }

    public Matrix4f getPRMatrix() {
        return pr_matrix;
    }

    public void setDeltaTime(double dT) {
        if (dT < 0.01) dT = 0.01;
        this.dT = dT;

    }
}
