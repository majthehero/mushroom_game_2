package si.majthehero.game.thing_ZERO.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain {

    private List<TerrainBlock> terrainBlocks;

    Terrain() {
        terrainBlocks = new ArrayList<TerrainBlock>();
        for (int i=-1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
                terrainBlocks.add(generateBlock(i,j));
            }
        }
    }

    // render

    public void render() {
        for (TerrainBlock tb : terrainBlocks) {
            tb.render();
        }
    }

    // generating

    private TerrainBlock generateBlock(int block_x, int block_y) {
        class _border {
            float[] h_vals;
            String dir;
            _border(float[] h_vals, String dir) {
                this.h_vals = h_vals;
                this.dir = dir;
            }
        }

        // get neighbours that exist
        List<_border> borders = new ArrayList<>();
        for (TerrainBlock tb : terrainBlocks) {
            int diff_x = Math.abs(tb.pos_x - (block_x * (TerrainBlock.resolution - 3))
                    - TerrainBlock.resolution/2);
            int diff_y = Math.abs(tb.pos_y - (block_y * (TerrainBlock.resolution - 2))
                    - TerrainBlock.resolution/2);
            System.out.println("Difference to block is: " + diff_x + " " + diff_y);
            if ((13 < diff_x && diff_x < 17) ||
                (13 < diff_y && diff_y < 17)) {
                System.out.println("Getting border.");
                String direction = "";
                int dx = tb.pos_x - block_x;
                int dy = tb.pos_y - block_y;

                if (dx == 1) direction += "F";
                else if (dx == -1) direction += "B";

                if (dy == 1) direction += "R";
                else if (dy == -1) direction += "L";

                _border b = new _border(tb.getHeightmapBorder(direction), direction);

            }
        }

        // prepare heightmap for diamond-square algorithm
        float [][] heightmap = new float[TerrainBlock.resolution][TerrainBlock.resolution];
        for (_border b : borders) {
            switch (b.dir) {
                case "F":
                    heightmap[0] = b.h_vals;
                    break;
                case "B":
                    heightmap[TerrainBlock.resolution-1] = b.h_vals;
                    break;
                case "L":
                    for (int i=0; i<TerrainBlock.resolution; i++) {
                        heightmap[i][0] = b.h_vals[i];
                    }
                    break;
                case "R":
                    for (int i=0; i<TerrainBlock.resolution; i++) {
                        heightmap[i][TerrainBlock.resolution] = b.h_vals[i];
                    }
                    break;
            }
        }

        // create a new block from height map
        TerrainBlock newBlock = new TerrainBlock(heightmap,
                block_x * (TerrainBlock.resolution-3),
                block_y * (TerrainBlock.resolution-2));

        return newBlock;
    }

}
