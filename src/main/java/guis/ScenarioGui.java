package guis;

import com.pw.AppThread;
import jade.wrapper.AgentContainer;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ScenarioGui extends JFrame implements ActionListener {
    private JLabel[] labels;
    private Thread t;
    private boolean factoryRunning;
    private String currentScenario;
    private JComponent header;
    private ArrayList<Thread> threads;
    private AgentContainer mainContainer;
    private AppThread app;
    private ImageIcon legend;
    private JLabel imageLabel;


    public ScenarioGui(AgentContainer _mainContainer) {
        header = getHeader();
        mainContainer = _mainContainer;

        add(header, BorderLayout.NORTH);
        setTitle("Scenario select");
        //pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        legend = new ImageIcon("src/main/resources/legends/"+ this.currentScenario +".JPG");
        imageLabel =  new JLabel(legend);
        JPanel panel = new JPanel();
        panel.add(imageLabel);
        add(panel,BorderLayout.CENTER);
        System.out.println("GUI created");
        setSize(700,700);

    }


    private JComponent getHeader(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton start = new JButton("START");
        start.addActionListener(this);

        JButton stop = new JButton("STOP");
        stop.addActionListener(this);

        JButton exit = new JButton("EXIT");
        exit.addActionListener(this);

        String[] scenarioNames = {"LinearScenario", "TreeFlowScenario","OutcastScenario","CollisionsAndCrossingsScenario"};
        JComboBox scenarios = new JComboBox(scenarioNames);
        this.currentScenario = scenarioNames[0];
        scenarios.addActionListener(this);

        panel.add(start);
        panel.add(stop);
        panel.add(exit);
        panel.add(scenarios);

        return panel;
    }



    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button.getText() == "START") {
                if(this.currentScenario != null){
                    System.out.println("start");
                    app = new AppThread(mainContainer, this.currentScenario);
                }

            } else if (button.getText() == "EXIT") {
                System.exit(0);
                System.out.println("stop");
            }
            else if(button.getText() == "STOP"){
                app.stopApp();
            }
        }
        else if(e.getSource() instanceof JComboBox){
            JComboBox combo = (JComboBox)e.getSource();
            this.currentScenario = (String)combo.getSelectedItem();
            imageLabel.setIcon(new ImageIcon("src/main/resources/legends/"+ this.currentScenario +".JPG"));
        }
    }
}
