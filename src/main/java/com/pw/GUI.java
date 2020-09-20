package com.pw;

import com.pw.agents.GomAgent;
import com.pw.agents.GuiAgent;
import com.pw.agents.TrAgent;
import com.pw.board.Board;
import jade.gui.GuiEvent;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Getter
public class GUI  extends JFrame implements ActionListener {
    private JLabel[] labels;
    private Thread t;
    private Board board;
    private GuiAgent myAgent;
    private boolean factoryRunning;
    private String currentScenario;
    private Container container;

    private JComponent header, grid;

    public GUI(GuiAgent guiAgent) {
        myAgent = guiAgent;
        board = null;
        factoryRunning = false;
        currentScenario = null;
        container = getContentPane();
        container.setLayout(new BorderLayout());

        header = getHeader();
        grid = getEmptyGrid();

        add(header, BorderLayout.NORTH);
        add(grid, BorderLayout.SOUTH);

        setTitle("Gui");
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("GUI created");

    }

    public void setBoard(Board board) {
        this.board = board;
        remove(this.grid);
        this.grid = getGrid();
        add(this.grid, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }

    private JComponent getHeader(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton start = new JButton("START");
        start.addActionListener(this);

        JButton stop = new JButton("STOP");
        stop.addActionListener(this);

        String[] scenarioNames = {"LinearFlowScenario", "TreeFlowScenario"};
        JComboBox scenarios = new JComboBox(scenarioNames);
        this.currentScenario = scenarioNames[0];

        panel.add(start);
        panel.add(stop);
        panel.add(scenarios);

        return panel;
    }

    private JComponent getEmptyGrid(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        return panel;
    }

    private JComponent getGrid(){
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

        return panel;
    }

    public void updateGUI(int[] gui) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setText("" + gui[i]);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button.getText() == "START") {
                if(!factoryRunning && this.currentScenario != null){
                    GuiEvent ge = new GuiEvent(this, GuiAgent.START_SCENARIO);
                    myAgent.postGuiEvent(ge);
                    factoryRunning = true;
                }

            } else if (button.getText() == "STOP") {
                GuiEvent ge = new GuiEvent(this, GuiAgent.STOP_SCENARIO);
                myAgent.postGuiEvent(ge);
                factoryRunning = false;
            }
        }
        else if(e.getSource() instanceof JComboBox){
            JComboBox combo = (JComboBox)e.getSource();
            this.currentScenario = (String)combo.getSelectedItem();
        }
    }
}
