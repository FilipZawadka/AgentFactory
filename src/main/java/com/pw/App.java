package com.pw;

import com.pw.scenerios.Scenerio1;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class App {

    public static void main(String[] args) throws StaleProxyException {
        Properties properties = new Properties();
        properties.setProperty(Profile.GUI, Boolean.TRUE.toString());
        AgentContainer mainContainer = Runtime.instance().createMainContainer(new ProfileImpl(properties));
        Scenerio1 scenerio = new Scenerio1();
        Factory factory = new Factory(scenerio, mainContainer);

        //GUI gui = new GUI(scenerio.getBoardHeight(),scenerio.getBoardWidth());
    }
}