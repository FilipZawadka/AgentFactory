package com.pw;

import com.pw.scenerios.*;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import lombok.SneakyThrows;

public class AppThread extends Thread {
    Thread t;
    String currentScenario;
    AgentContainer mainContainer;
    Factory factory;


    @SneakyThrows
    public void run() {
        Properties properties = new Properties();
        properties.setProperty(Profile.GUI, Boolean.TRUE.toString());
        mainContainer = Runtime.instance().createMainContainer(new ProfileImpl(properties));
        Scenario scenario =null;// = new LinearScenario();
        switch (currentScenario){
            case "LinearScenario":
                scenario = new LinearScenario();
                break;
            case "TreeFlowScenario":
                scenario = new TreeFlowScenario();
                break;
            case "OutcastScenario":
                scenario = new OutcastScenario();
                break;
            case "CollisionsAndCrossingsScenario":
                scenario = new CollisionsAndCrossingsScenario();
                break;
            default:
                break;

        }
        factory = new Factory(scenario, mainContainer);
    }

    public AppThread(String _currentScenario) {
        currentScenario = _currentScenario;
        System.out.println("Starting " + _currentScenario);
        if (t == null) {
            t = new Thread(this, _currentScenario);
            t.start();
        }
    }
    public void kill() throws StaleProxyException, InterruptedException {
        System.exit(0);


    }
}