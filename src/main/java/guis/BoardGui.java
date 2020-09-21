package guis;

import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import com.pw.board.Board;

import javax.swing.*;
import java.awt.*;

public class BoardGui implements Runnable {
    private JLabel[] labels;
    private JLabel[] tokenLabels;
    private JPanel panel;
    private JFrame frame;
    private Thread t;
    private Board board;
    private ImageIcon emptyIcon;
    private ImageIcon gotrIcon;
    private ImageIcon trIcon;
    private ImageIcon gomIcon;
    private ImageIcon gomwithtrsIcon;
    private Boolean stop;

    public BoardGui(Board _board) {
        board = _board;
        stop = false;
        frame = new JFrame();
        JPanel boardPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10,10));
        mainPanel.add(getHeader(),BorderLayout.NORTH);
        int b = 50;
        boardPanel.setBorder(BorderFactory.createEmptyBorder(b, b, b, b));
        boardPanel.setLayout(new GridLayout(board.height, board.width, 10, 10));
        boardPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        emptyIcon = new ImageIcon("src/main/resources/icons/block.png");
        gotrIcon = new ImageIcon("src/main/resources/icons/gotr.png");
        gomIcon = new ImageIcon("src/main/resources/icons/gom.png");
        trIcon = new ImageIcon("src/main/resources/icons/tr.png");
        gomwithtrsIcon = new ImageIcon("src/main/resources/icons/gomwithtrs.png");

        labels = new JLabel[board.height * board.width];
        for (int i = 0; i < board.height * board.width; i++) {
            //labels[i] = new JLabel("" + i);
            labels[i] = new JLabel(emptyIcon);
            boardPanel.add(labels[i]);
        }
        mainPanel.add(boardPanel,BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Gui");

        frame.pack();

        frame.setVisible(true);

        System.out.println("GUI created");

    }
    private JComponent getHeader(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        tokenLabels = new JLabel[board.TrList.size()];
        for (int i = 0; i < board.TrList.size(); i++) {
            tokenLabels[i] = new JLabel(" TR "+(i+1)+ " tokens: 0 ");
            panel.add(tokenLabels[i]);
        }

        return panel;
    }

    public void run() {
        System.out.println("Running " + "GUI");
        try {
            while (!stop) {
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
    public void stop() throws InterruptedException {
        stop = true;
        frame.dispose();
//        t.interrupt();
    }

    public void updateGUI(int[] gui) {
        for (int i = 0; i < labels.length; i++) {
            //labels[i].setText("" + gui[i]);
            if(gui[i]==0) {
                labels[i].setIcon(emptyIcon);
            }
            else if(gui[i]==1){
                labels[i].setIcon(trIcon);
            }
            else if (gui[i]==100){
                labels[i].setIcon(gomIcon);
            }
            else if (gui[i]<100){
                labels[i].setIcon(gotrIcon);
            }
            else {
                labels[i].setIcon(gomwithtrsIcon);
            }
        }
        for (int i = 0; i < board.TrList.size(); i++) {
            tokenLabels[i].setText(" TR "+(i+1)+ " tokens: "+board.TrList.get(i).getTokens()+" ");
        }
    }


}
