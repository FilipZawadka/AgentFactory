package com.pw;

import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import com.pw.board.Board;
import com.pw.scenerios.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.EventListener;

public class ScenarioGui extends JFrame implements ActionListener {
    private JLabel[] labels;
    private Thread t;
    private boolean factoryRunning;
    private String currentScenario;
    private JComponent header;
    private ArrayList<Thread> threads;
    private AgentContainer mainContainer;
    private appThread app;


    public ScenarioGui() {
        header = getHeader();

        add(header, BorderLayout.NORTH);
        setTitle("Scenario select");
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("GUI created");

    }


    private JComponent getHeader(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton start = new JButton("START");
        start.addActionListener(this);

        JButton stop = new JButton("STOP");
        stop.addActionListener(this);

        String[] scenarioNames = {"LinearScenario", "TreeFlowScenario","OutcastScenario","CollisionsAndCrossingsScenario"};
        JComboBox scenarios = new JComboBox(scenarioNames);
        this.currentScenario = scenarioNames[0];

        panel.add(start);
        panel.add(stop);
        panel.add(scenarios);

        return panel;
    }



    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button.getText() == "START") {
                if(!factoryRunning && this.currentScenario != null){
                    System.out.println("start");
                    app = new appThread(currentScenario);
                }

            } else if (button.getText() == "STOP") {
                app.kill();
                app.join();
                System.out.println("stop");
            }
        }
        else if(e.getSource() instanceof JComboBox){
            JComboBox combo = (JComboBox)e.getSource();
            this.currentScenario = (String)combo.getSelectedItem();
        }
    }
}
