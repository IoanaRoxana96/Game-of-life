package com.example.gameoflife;

public class Cell {

    public int x, y;
    public boolean alive;

    public Cell (int x, int y, boolean alive) {
        this.x = x;
        this.y = y;
        this.alive = alive;
    }

    public void die() {
        alive = false;
    }

    public void reborn() {
        alive = true;
    }

    public void invert() {
        alive = !alive;
    }
}
