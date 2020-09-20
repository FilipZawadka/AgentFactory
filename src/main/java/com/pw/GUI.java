package com.pw;

import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import com.pw.board.Board;

import javax.swing.*;
import java.awt.*;

public class GUI implements Runnable {
    private JLabel[] labels;
    private JPanel panel;
    private JFrame frame;
    private Thread t;
    private Board board;

    public GUI(Board _board) {
        board = _board;
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        int b = 130;
        panel.setBorder(BorderFactory.createEmptyBorder(b, b, b, b));
        panel.setLayout(new GridLayout(board.height, board.width, 22, 22));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        labels = new JLabel[board.height * board.width];
        for (int i = 0; i < board.height * board.width; i++) {
            labels[i] = new JLabel("" + i);
            panel.add(labels[i]);
        }
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Gui");
        frame.pack();
        frame.setVisible(true);

        System.out.println("GUI created");

    }

    public void run() {
        System.out.println("Running " + "GUI");
        try {
            while (true) {
                int[] guiArr = new int[board.height * board.width];
                for (GomAgent a : board.GomList) {
                    guiArr[a.getPosition().getX() + a.getPosition().getY() * board.width] += 100;
                }
                for (TrAgent a : board.TrList) {
                    guiArr[a.getPosition().getX() + a.getPosition().getY() * board.width] += 1;
                }
                updateGUI(guiArr);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + "GUI" + " interrupted.");
        }
    }

    public void start() {
        System.out.println("Starting " + "GUI");
        if (t == null) {
            t = new Thread(this, "GUI");
            t.setDaemon(true);
            t.start();
        }
    }

    public void updateGUI(int[] gui) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setText("" + gui[i]);
        }
    }


}
