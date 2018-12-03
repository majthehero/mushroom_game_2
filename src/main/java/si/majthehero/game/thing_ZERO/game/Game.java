package si.majthehero.game.thing_ZERO.game;

import org.joml.Matrix4f;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.input.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class Game {

    public Camera camera;
    public Terrain terrain;

    public Game() {
        camera = new Camera();
        terrain = new Terrain();
    }

    // render ===========================================================

    private void prepareCamera() {
        Matrix4f pr_matrix = camera.getPRMatrix();

        Shader.TERRAIN_SHADER.enable();
        Shader.TERRAIN_SHADER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.TERRAIN_SHADER.disable();
    }

    public void render() {
        Matrix4f prMatrix = camera.getPRMatrix();
        prepareCamera();
        terrain.render();
    }

    public void update(double dT) {
        glfwPollEvents();
        camera.setDeltaTime(dT);
        if (Input.keys[GLFW_KEY_W]) {
            camera.move(1,0,0);
        }
        if (Input.keys[GLFW_KEY_S]) {
            camera.move(-1,0,0);
        }
        if (Input.keys[GLFW_KEY_A]) {
            camera.move(0,1,0);
        }
        if (Input.keys[GLFW_KEY_D]) {
            camera.move(0,-1,0);
        }

        if (Input.keys[GLFW_KEY_Q]) {
            camera.rotate(0, 0.1f);
        }
        if (Input.keys[GLFW_KEY_E]) {
            camera.rotate(0, -0.1f);
        }
        if (Input.keys[GLFW_KEY_R]) {
            camera.rotate(0.1f, 0);
        }
        if (Input.keys[GLFW_KEY_F]) {
            camera.rotate(0, -0.1f);
        }

        camera.update();
    }

}
