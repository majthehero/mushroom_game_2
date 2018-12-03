package si.majthehero.game.thing_ZERO.game;

import si.majthehero.game.thing_ZERO.graphics.Geometry;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.graphics.Texture;

public class Prototype {

    float pos_x;
    float pos_y;
    private Geometry geometry;
    private Texture texture;

    public Prototype(float x, float y) {
        pos_x = x;
        pos_y = y;
        createGeometry();
        texture = new Texture("assets/textures/grass.jpg");
    }

    private void createGeometry() {

        float[] vertices;// = new float[3*3*3];
        byte[] indices; // = new byte[3*3*2];
        float[] tcs = new float[3*3*2];

        vertices = new float[] {
                -1.0f, -1.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  1.0f,  0.0f,

                 0.0f, -1.0f,  0.0f,
                 0.0f,  0.0f,  1.0f,
                 0.0f,  1.0f,  0.0f,

                 1.0f, -1.0f,  0.0f,
                 1.0f,  0.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
        };
        indices = new byte[] {
                0, 1, 3,
                1, 4, 3,
                1, 2, 4,
                2, 5, 4,
                3, 4, 6,
                4, 7, 6,
                4, 5, 7,
                5, 8, 7,
        };
        tcs = new float[] {
                0.0f, 0.0f,
                0.5f, 0.0f,
                1.0f, 0.0f,

                0.0f, 0.5f,
                0.5f, 0.5f,
                1.0f, 0.5f,

                0.0f, 1.0f,
                0.5f, 1.0f,
                1.0f, 1.0f,
        };

        this.geometry = new Geometry(vertices, indices, tcs);
    }

    public void render() {
        Shader.TERRAIN_SHADER.enable();
        texture.bind();
        geometry.render();
        texture.unbind();
        Shader.TERRAIN_SHADER.disable();
    }

}
