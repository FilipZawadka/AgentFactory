package com.pw.agents;

import com.pw.Factory;
import com.pw.GUI;
import com.pw.behaviours.UpdateBoardBehaviour;
import com.pw.scenarios.Scenario;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class GuiAgent extends jade.gui.GuiAgent {
    private GUI gui;
    private Scenario currentScenario;
    private Factory currentFactory;
    private AgentContainer container;

    private static final String SCENARIO_PATH = "com.pw.scenarios.";

    public static final int START_SCENARIO = 1000;
    public static final int STOP_SCENARIO = 1001;

    @SneakyThrows
    @Override
    protected void setup() {
        super.setup();
        Object[] args = getArguments();
        this.container = (AgentContainer) args[0];

        this.gui = new GUI(this);
    }

    @SneakyThrows
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        switch(guiEvent.getType()){
            case START_SCENARIO:
                setScenario(this.gui.getCurrentScenario());
                setFactory();
                addBehaviour(new UpdateBoardBehaviour());
                break;
            case STOP_SCENARIO:
                this.currentFactory.takeDown();
                break;
        }
    }

    @SneakyThrows
    private void setScenario(String scenarioName){
        Object o = Class.forName(SCENARIO_PATH+scenarioName).getConstructor().newInstance();
        this.currentScenario = (Scenario)o;
    }

    @SneakyThrows
    private void setFactory(){
        this.currentFactory = new Factory(this.currentScenario, this.container);
        this.gui.setBoard(this.currentFactory.getBoard());
        this.currentFactory.start();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }
}
