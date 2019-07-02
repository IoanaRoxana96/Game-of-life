package com.example.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GameOfLifeView extends SurfaceView implements Runnable {

    public static final int DEFAULT_SIZE = 50;
    public static final int DEFAUL_ALIVE_COLOR = Color.WHITE;
    public static final int DEFAULT_DEAD_COLOR = Color.BLACK;
    // Thread which will be responsable to manage the evolution of the World
    private Thread thread;
    // Indicating if the World is evolving or not
    private boolean isRunning;
    private int columnWidth = 1;
    private int rowHeight = 1;
    private int nbColumns = 1;
    private int nbRows = 1;
    private World world;
    // Rectangle and Paint instance to draw the elements
    private Rect r = new Rect();
    private Paint p = new Paint();

    public GameOfLifeView (Context context) {
        super(context);
        initWorld();
    }

    public GameOfLifeView (Context context, AttributeSet attrs) {
        super(context, attrs);
        initWorld();
    }

    @Override
    public void run() {
        // while the world is evolving
        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;
            // Pause to better visualization
            try {
                Thread.sleep (300);
            } catch (Exception e) {

            }

            Canvas canvas = getHolder().lockCanvas();
            world.nextGeneration();
            drawCells (canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected void start() {
        // World is evolving
        isRunning = true;
        thread = new Thread(this);
        // start the Thread for the World's evolution
        thread.start();
    }

    public void stop() {
        isRunning = false;

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {

            }

            break;
        }
    }

    // Method to draw each cell of the world on the canvas
    private void drawCells(Canvas canvas) {

        for (int i = 0; i < nbColumns; i++) {
            for (int j = 0; j < nbRows; j++) {
                Cell cell = world.get (i, j);
                r.set((cell.x *columnWidth) - 1, (cell.y * rowHeight) - 1,
                        (cell.x * columnWidth + columnWidth) - 1, (cell.y *rowHeight + rowHeight) -1);
                // change the color according the alive status of the cell
                p.setColor(cell.alive ? DEFAUL_ALIVE_COLOR : DEFAULT_DEAD_COLOR);

                canvas.drawRect(r, p);
            }
        }
    }

    private void initWorld() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display dispaly = wm.getDefaultDisplay();
        Point point = new Point();
        dispaly.getSize(point);
        // calculate the number of columns and rows for the World
        // nbColumns = point.x / DEFAULT_SIZE;
        // nbRows = point.y / DEFAULT_SIZE;
        nbColumns = 5;
        nbRows = 5;
        // calculate the column width and row height
        columnWidth = point.x / nbColumns;
        rowHeight = point.y / nbRows;

        world = new World(nbColumns, nbRows);

    }

    // let the user to interact with the Cells of the World
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        // get the coordinates of the touch and we convert it in coordinates for the board
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int i = (int) (event.getX()) / columnWidth;
            int j = (int) (event.getY()) / rowHeight;
            // get the cell associated to these positions
            Cell cell = world.get (i, j);
            // call the invert method of the cell got to change its state
            cell.invert();
            invalidate();
        }

        return super.onTouchEvent(event);
    }

}
