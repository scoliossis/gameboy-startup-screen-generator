
package scoliosis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class DrawCanvas extends Canvas {

    public DrawCanvas() {
    }

    public void startDrawing() {

        while (true) {
            BufferStrategy bs = this.getBufferStrategy();
            if (bs == null) createBufferStrategy(3);
            Draw.draw(bs);
        }
    }



    public static JFrame mainframe = new JFrame();

    static DrawCanvas canvas;

    public static void createCanvas() {
        canvas = new DrawCanvas();

        mainframe = new JFrame("scoliosis on top!");
        //mainframe.setUndecorated(true);
        mainframe.add(canvas);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setLocation(500, 200);
        mainframe.setSize(889, 500);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);
        canvas.startDrawing();
    }
}
