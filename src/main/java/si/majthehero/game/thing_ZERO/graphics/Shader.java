package si.majthehero.game.thing_ZERO.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import si.majthehero.game.thing_ZERO.utils.ShaderUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    static final int VERTEX_ATTRIB = 0;
    static final int TCOORD_ATTRIB = 1;
    static final int NORMAL_ATTRIB = 2;

    private boolean enabled = false;

    public static Shader TERRAIN_SHADER;

    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        TERRAIN_SHADER = new Shader("assets/shaders/terrain.vert",
                "assets/shaders/terrain.frag");
    }


    public int getUniform(String name) {
        if (locationCache.containsKey(name))
            return locationCache.get(name);

        int result = glGetUniformLocation(ID, name);

        if (result == -1)
            System.out.println("ERROR: Could not find uniform variable: " + name);
        else
            locationCache.put(name, result);

        return result;
    }

    public void setUniform1i(String name, int value) {
//        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
//        if (!enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform3f(String name, Vector3f value) {
//        if (!enabled) enable();
        glUniform3f(getUniform(name), value.x, value.y, value.z);
    }

    public void setUniformMat4f(String name, Matrix4f value) {
//        if (!enabled) enable();
        FloatBuffer mat = BufferUtils.createFloatBuffer(4*4);
        mat = value.get(mat);
        glUniformMatrix4fv(getUniform(name), false, mat);
    }

    public void enable() {
        glUseProgram(ID);
    }

    public void disable() {
        glUseProgram(0);
    }

}
