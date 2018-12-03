package si.majthehero.game.thing_ZERO;

import si.majthehero.game.thing_ZERO.graphics.Geometry;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.graphics.Texture;

class Tetrahedron {

    private Geometry terrain;
    private Texture terrain_texture;

    Tetrahedron() {
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
        terrain = new Geometry(vertices, indices, tcs);

        terrain_texture = new Texture("assets/textures/tetrahedron.jpg");
    }

    void render() {
        terrain_texture.bind();
        Shader.TERRAIN_SHADER.enable();
        terrain.render();
        Shader.TERRAIN_SHADER.disable();
        terrain_texture.unbind();
    }
}
