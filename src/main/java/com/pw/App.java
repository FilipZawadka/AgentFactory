package com.pw;

import guis.ScenarioGui;
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
		ScenarioGui scenarioGui = new ScenarioGui(mainContainer);
	}
}