package si.majthehero.game.thing_ZERO.game;

import org.joml.Matrix4f;
import si.majthehero.game.thing_ZERO.graphics.Geometry;
import si.majthehero.game.thing_ZERO.graphics.Shader;
import si.majthehero.game.thing_ZERO.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class TerrainBlock {

    List<Mushroom> mushrooms;

    int pos_x, pos_y;
    final int seed;

    Geometry geometry;
    static Texture texture;

    public static int resolution = 4*4+1;
    private float SCALING_FACTOR = 0.5f;
    private float[][] heightmap;

    {
        texture = new Texture("assets/textures/grass.jpg");
    }

    public TerrainBlock(float[][] heightmap, int pos_x, int pos_y) {
        this.pos_x = pos_x - resolution/2;
        this.pos_y = pos_y - resolution/2;
        this.heightmap = mirror(heightmap);
        diamond_square(0, resolution-1, 0, resolution-1, 0);
        seed = (pos_x + resolution * pos_y) & (pos_y + resolution * pos_x);
        System.out.println("Seed for TerrainBlock@" + pos_x + ":" + pos_y + " is " + seed);

        createGeometry();

        int mushroom_num = (int) (Math.random() * 20);
        mushrooms = new ArrayList<>(mushroom_num);
        for (int i=0; i<mushroom_num; i++) {
            mushrooms.add(new Mushroom());
        }
    }

    private float[][] mirror(float[][] hmap) {
        float[][] retval = new float[hmap.length][hmap[0].length];
        for (int i = 0; i < hmap.length; i++) {
            for (int j = 0; j < hmap[0].length; j++) {
                retval[hmap.length-i-1][hmap.length-j-1] = hmap[i][j];
            }
        }
        return retval;
    }

    // rendering ======================================================================
    public void render() {
        Matrix4f mvMatrix = new Matrix4f()
                .translate(pos_x, pos_y, 0);

        Shader.TERRAIN_SHADER.enable();
        Shader.TERRAIN_SHADER.setUniformMat4f("mv_matrix", mvMatrix);
        texture.bind();
        geometry.render();
        texture.unbind();
        Shader.TERRAIN_SHADER.disable();

        for (Mushroom m : mushrooms) {
            m.render(mvMatrix);
        }
    }

    // generating terrain =============================================================

    /**
     * Gets average of four points in the height map, ignoring those out of bounds.
     * @param xUp and others: index pairs for all four points
     * @return average of those points that are in the height map
     */
    private float diamond_get_avg(int xUp, int yUp,
                                  int xRight, int yRight,
                                  int xDown, int yDown,
                                  int xLeft, int yLeft) {
        float vUp = 0;
        float vRight = 0;
        float vDown = 0;
        float vLeft = 0;
        boolean bUp = true;
        boolean bRight = true;
        boolean bDown  = true;
        boolean bLeft = true;

        try { vUp = heightmap[xUp][yUp];
        } catch (IndexOutOfBoundsException e) { bUp = false; }

        try { vRight = heightmap[xRight][yRight];
        } catch (IndexOutOfBoundsException e) { bRight = false; }

        try { vDown = heightmap[xDown][yDown];
        } catch (IndexOutOfBoundsException e) { bDown = false; }

        try { vLeft = heightmap[xLeft][yLeft];
        } catch (IndexOutOfBoundsException e) { bLeft = false; }

        int num = 0;
        float sum = 0;
        if (bUp) {
            sum += vUp;
            num++;
        }
        if (bRight) {
            sum += vRight;
            num++;
        }
        if (bDown) {
            sum += vDown;
            num++;
        }
        if (bLeft) {
            sum += vLeft;
            num++;
        }
        return sum/(float)num;
    }

    /**
     * Performs the diamond-square algorithm to generate a new terrain block.
     * @param x_min ,
     * @param x_max ,
     * @param y_min and
     * @param y_max : these 4 params define the region of the heightmap to work on
     * @param curr_iter depth of the recurence - starts with 0
     */
    private void diamond_square(int x_min, int x_max,
                                int y_min, int y_max,
                                int curr_iter) {
        // square step
        int x_center = (x_max - x_min)/2 + x_min;
        int y_center = (y_max - y_min)/2 + y_min;
        float rand_mod = (float) (Math.random() * Math.pow(SCALING_FACTOR, curr_iter));
        if (heightmap[x_center][y_center] == 0)
            heightmap[x_center][y_center] =
                    (heightmap[x_min][y_min] + heightmap[x_max][y_min] +
                            heightmap[x_max][y_max] + heightmap[x_min][y_max]) / 4 +
                            rand_mod;
        // diamond step
        int diff = (x_max - x_min)/2;
        // left
        if (heightmap[x_min][y_center] == 0)
            heightmap[x_min][y_center]= diamond_get_avg(
                    x_min, y_center + diff,
                    x_min + diff, y_center,
                    x_min, y_center - diff,
                    x_min - diff, y_center) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // up
        if (heightmap[x_center][y_min] == 0)
            heightmap[x_center][y_min]= diamond_get_avg(
                    x_center, y_min + diff,
                    x_center + diff, y_min,
                    x_center, y_min - diff,
                    x_center - diff, y_min) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // right
        if (heightmap[x_max][y_center] == 0)
            heightmap[x_max][y_center] = diamond_get_avg(
                    x_max, y_center + diff,
                    x_max + diff, y_center,
                    x_max, y_center - diff,
                    x_max - diff, y_center) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // down
        if (heightmap[x_center][y_max] == 0)
            heightmap[x_center][y_max] = diamond_get_avg(
                    x_center, y_max + diff,
                    x_center + diff, y_max,
                    x_center, y_max - diff,
                    x_center - diff, y_max) + (float)(Math.random() * Math.pow(0.5, curr_iter));

        // recure into sub-squares
        // left up
        if (Math.pow(2, curr_iter) < resolution) {
            diamond_square(x_min, x_center, y_min, y_center, curr_iter + 1);
            // right up
            diamond_square(x_center, x_max, y_min, y_center, curr_iter + 1);
            // right down
            diamond_square(x_center, x_max, y_center, y_max, curr_iter + 1);
            // left down
            diamond_square(x_min, x_center, y_center, y_max, curr_iter + 1);
        }
    }

    /**
     * Converts heightmap into geometry.
     */
    private void createGeometry() {

        float[] vertices = new float[resolution * resolution * 3];
        byte[] indices = new byte[resolution * resolution * 6];
        float[] tcs = new float[resolution * resolution * 2];

        int vertCount = 0;
        for (int i=0; i<resolution; i++) {
            for (int j=0; j<resolution; j++) {
                vertices[vertCount] = i;
                vertices[vertCount + 1] = j;
                vertices[vertCount + 2] = heightmap[i][j];
                vertCount += 3;
            }
        }

        int tri_index = 0;
        for (int i=0; i<resolution - 3; i++) {
            for (int j=0; j<resolution - 2; j++) {
                int offset = i*resolution + j;
                indices[tri_index+0] = (byte)(offset);
                indices[tri_index+1] = (byte)(offset+1);
                indices[tri_index+2] = (byte)(offset+resolution);

                indices[tri_index+3] = (byte)(offset+1);
                indices[tri_index+4] = (byte)(offset+resolution+1);
                indices[tri_index+5] = (byte)(offset+resolution);
                tri_index += 6;
            }
        }

        int tcCount = 0;
        for (int i=0; i<resolution; i++) {
            for (int j=0; j<resolution; j++) {
                tcs[tcCount] = (float)i / (float)resolution;
                tcs[tcCount + 1] = (float)j / (float)resolution;
                tcCount += 2;
            }
        }

        // create geometry
        geometry = new Geometry(vertices, indices, tcs);
    }

    // getting data out ===============================================================

    /**
     * Returns the heightmap border.
     * Used in Terrain.generateTerrain to pass this block's borders to those generating next to it.
     * @param direction Which border to return
     * @return float[] border of heightmap
     */
    float[] getHeightmapBorder(String direction) {
        /**
         * Get float array of height along a chosen border.
         * For direction that don't share a border but only a corner,
         * returns null.
         * :param direction: string in ["F","B","R","L"] return values, others null
         * :return: float array of height values along a border
         */
        float[] retval = new float[resolution];
        switch (direction) {
            case "F":
                retval = heightmap[0];
                break;
            case "B":
                retval = heightmap[resolution-1];
                break;
            case "L":
                for (int i=0; i<resolution; i++) {
                    retval[i] = heightmap[i][0];
                }
                break;
            case "R":
                for (int i=0; i<resolution; i++) {
                    retval[i] = heightmap[i][resolution-1];
                }
                break;
            default:
                retval = null;
        }
        return retval;
    }

}
