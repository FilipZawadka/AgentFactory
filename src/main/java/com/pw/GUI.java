package com.pw;
import javax.swing.*;
import java.awt.*;

public class GUI {
    private JLabel[] labels;
    private  JPanel panel;
    private JFrame frame;

    public GUI(Integer boardHeight, Integer boardWidth) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        panel.setLayout(new GridLayout(boardHeight,boardWidth,30,30) );
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        labels = new JLabel[boardHeight*boardWidth];
        for(int i =0;i<boardHeight*boardWidth; i++) {
            labels[i]= new JLabel(""+i);
            panel.add(labels[i]);
        }
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Gui");
        frame.pack();
        frame.setVisible(true);

    }

    public void updateGUI(int[] gui){
        for(int i = 0;i<labels.length;i++){
            labels[i].setText(""+gui[i]);
        }
    }


}
