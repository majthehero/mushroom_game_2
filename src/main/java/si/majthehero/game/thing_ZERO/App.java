package si.majthehero.game.thing_ZERO;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import si.majthehero.game.thing_ZERO.game.Camera;
import si.majthehero.game.thing_ZERO.game.Game;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.input.Input;
import si.majthehero.game.thing_ZERO.game.TerrainBlock;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class App implements Runnable {

    int width = 1680;
    int height = 1050;

    private Thread thread;
    private boolean running = false;

    private long window;

    private Game game;

    private void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        if (!glfwInit()) {
            System.out.println("ERROR: Could not init GLFW.");
            System.exit(1);
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, "ZERO", NULL, NULL);

        if (window == NULL) {
            System.out.println("ERROR: Could not create window.");
            System.exit(1);
        }

        glfwSetKeyCallback(window, new Input());

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();

        System.out.println("GL: " + glGetString(GL_VERSION));

        glClearColor(0.5f, 0.6f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        glActiveTexture(GL_TEXTURE1);

        Shader.loadAll();

        Shader.TERRAIN_SHADER.enable();
//        Shader.TERRAIN_SHADER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.TERRAIN_SHADER.setUniform1i("tex", 1);
        Shader.TERRAIN_SHADER.disable();

        game = new Game();

    }

    @Override
    public void run() {
        init();
        long time_previous = System.nanoTime();
        while (running) {
            long time_loop_start = System.nanoTime();
            long deltaT = time_loop_start - time_previous;
            double dT = (double) deltaT /  1000000000.0d;
            time_previous = time_loop_start;

            update(dT);
            render();

            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }

    private void update(double dT) {
        game.update(dT);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        game.render();

        int err = glGetError();
        if (err != GL_NO_ERROR)
            System.out.println(err);

        glfwSwapBuffers(window);
    }

    public static void main (String[] args) {
        new App().start();
    }
}
