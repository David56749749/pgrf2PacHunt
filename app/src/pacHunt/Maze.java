package pacHunt;

import static org.lwjgl.opengl.GL11.*;

public class Maze {
    // 2D pole představující plán bludiště (1 = zeď, 0 = volná cesta)
    private int[][] maze = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,1,1,0,0,1,0,0,1},
            {1,0,1,0,0,0,0,0,0,1},
            {1,0,1,0,1,1,1,1,0,1},
            {1,0,1,0,1,0,0,0,0,1},
            {1,0,0,0,1,0,1,1,0,1},
            {1,0,1,0,0,0,1,0,0,1},
            {1,0,1,1,1,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1}
    };

    private float tileSize = 1.0f;

    // Vykreslí bludiště pomocí rendereru
    public void draw(Renderer renderer) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 1) {
                    // Nastavíme různou barvu na základě indexů
                    if ((row + col) % 2 == 0) {
                        glColor3f(0.8f, 0.3f, 0.3f);  // například odstín červené
                    } else {
                        glColor3f(0.3f, 0.8f, 0.3f);  // například odstín zelené
                    }

                    // Výpočet pozice zdi
                    float x = (col - maze[row].length / 2) * tileSize;
                    float z = (row - maze.length / 2) * tileSize;
                    renderer.drawCube(x, 0.0f, z, tileSize);
                }
            }
        }
    }
}

