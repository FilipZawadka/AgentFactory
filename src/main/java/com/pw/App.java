package com.pw;

import com.pw.scenerios.Scenario;
import com.pw.scenerios.TreeFlowScenario;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class App {

    public static void main(String[] args) throws StaleProxyException {
        Properties properties = new Properties();
        properties.setProperty(Profile.GUI, Boolean.FALSE.toString());
        AgentContainer mainContainer = Runtime.instance().createMainContainer(new ProfileImpl(properties));
        Scenario scenario = new TreeFlowScenario();
        Factory factory = new Factory(scenario, mainContainer);
    }
}