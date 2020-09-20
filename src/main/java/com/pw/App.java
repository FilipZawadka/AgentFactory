package com.pw;

import com.pw.scenerios.LinearScenario;
import com.pw.scenerios.Scenario;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class App {

    public static void main(String[] args) throws StaleProxyException {
        ScenarioGui scenarioGui = new ScenarioGui();
    }
}