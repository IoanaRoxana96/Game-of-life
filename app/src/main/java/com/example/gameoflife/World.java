package com.example.gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    public static final Random RANDOM = new Random();
    public int width, height;
    private  Cell[][] board;

    public World (int width, int height) {
        this.width = width;
        this.height = height;
        board = new Cell[width][height];
        init();
    }

    private void init() {
        for (int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                board[i][j] = new Cell(i, j, RANDOM.nextBoolean());
            }
        }
    }

    public Cell get (int i, int j) {
            return board[i][j];
    }

    public int nbNeighboursOf (int i, int j) {
        int nb = 0;

        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; j <= j + 1; l++) {
                if ((k != i || l != j) && k >= 0
                        && k < width && l >= 0 && l < height) {
                    Cell cell = board[k][l];

                    if(cell.alive) {
                         nb++;
                    }
                }
            }
        }
         return nb;
    }

    public void nextGeneration() {
        List<Cell> liveCells = new ArrayList<>();
        List<Cell> deadCells = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board[i][j];

                int nbNeighbours = nbNeighboursOf(cell.x, cell.y);

                // Any live cell with fewer than 2 live neighbours and more than 3 neighbours die

                if (cell.alive && (nbNeighbours < 2 || nbNeighbours > 3)) {
                    deadCells.add(cell);
                }

                // Any live cell with 2 or 3 live neighbours lives on to the next generation and any dead cell with exactly 3 neighbours becomes a live cell

                if ((cell.alive && (nbNeighbours == 3 || nbNeighbours == 2))
                        ||
                        (!cell.alive && nbNeighbours == 3)) {
                    liveCells.add(cell);
                }
            }
        }

        // update future live and dead cells
        for (Cell cell : liveCells) {
            cell.reborn();
        }

        for (Cell cell : deadCells) {
            cell.die();
        }
    }
}
