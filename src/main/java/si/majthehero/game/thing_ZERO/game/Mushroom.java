package si.majthehero.game.thing_ZERO.game;

import org.joml.Matrix4f;
import si.majthehero.game.thing_ZERO.graphics.Geometry;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.graphics.Texture;

public class Mushroom {

    float pos_x, pos_y;
    static Geometry geometry;
    static Texture texture;

    {
        float[] vertices = new float[]{ // testing tetrahedron
                -1.0f, +1.0f, +1.0f, // A
                -1.0f, -1.0f, +1.0f, // B
                -1.0f, +0.0f, -1.0f, // C
                +1.0f, +0.0f, +0.0f, // D
        };
        byte[] indices = new byte[]{
                3, 0, 1, // front face
                3, 1, 2, // right face
                3, 2, 0, // back face
                0, 2, 1, // bottom face
        };
        float[] tcs = new float[]{ // hm...
                0.0f, 0.0f, // A
                1.0f, 0.0f, // B
                1.0f, 1.0f, // C
                0.5f, 0.5f // D - bootom face is expected to be wrong
        };
        geometry = new Geometry(vertices, indices, tcs);
        texture = new Texture("assets/textures/tetrahedron.jpg");
    }

    public Mushroom() {
        pos_x = (float) (Math.random() * TerrainBlock.resolution/2);
        pos_y = (float) (Math.random() * TerrainBlock.resolution/2);
    }

    public void render(Matrix4f mvMaster) {
        mvMaster.mul(new Matrix4f()
                .translate(pos_x, pos_y, 0));

        Shader.TERRAIN_SHADER.enable();
        Shader.TERRAIN_SHADER.setUniformMat4f("mv_matrix", mvMaster);
        texture.bind();
        geometry.render();
        texture.unbind();
        Shader.TERRAIN_SHADER.disable();
    }

}
