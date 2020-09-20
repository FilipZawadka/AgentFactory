package com.pw.behaviours;

import com.pw.agents.GomAgent;
import com.pw.biddingOntology.GomInfo;
import com.pw.biddingOntology.GomJobRequest;
import com.pw.biddingOntology.MaterialInfo;
import com.pw.biddingOntology.PositionInfo;
import com.pw.utils.GomDefinition;
import com.pw.utils.GomProcess;
import com.pw.utils.Material;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Vector;

import static com.pw.utils.Naming.GOM;
import static com.pw.utils.Naming.TR;

public class GomProcessingBehavior extends TickerBehaviour {

    public GomProcessingBehavior(GomAgent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        GomAgent agent = (GomAgent) myAgent;
        Map<Material, Integer> availableMaterials = agent.getMaterials();

        agent.getDefinition().getProcesses().forEach(process -> {
            Map<Material, Integer> requiredMaterials = process.getInputMaterials();
            if (!isThereEnoughMaterialsToStartProcess(availableMaterials, requiredMaterials)) {
                return;
            }
            requiredMaterials.forEach((material, amount) ->
                availableMaterials.put(material, availableMaterials.get(material) - amount));
            startProcess(process);
        });
    }

    private boolean isThereEnoughMaterialsToStartProcess(Map<Material, Integer> availableMaterials, Map<Material, Integer> requiredMaterials) {
        for (Map.Entry<Material, Integer> entry : requiredMaterials.entrySet()) {
            if (!availableMaterials.containsKey(entry.getKey()) || availableMaterials.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private void startProcess(GomProcess process) {
        //TODO: May want to add some processing delay later..
        // ..

        process.getOutputs().forEach(output -> {
            sendJobToTR(output.getMaterial(), output.getDestination());
        });
    }

    private void sendJobToTR(Material material, GomDefinition destination) {
        ACLMessage request = createJobRequestToTR(material, destination);

        myAgent.addBehaviour(new AchieveREInitiator(myAgent, request) {
            @Override
            protected void handleAllResponses(Vector responses) {
                for (Object message : responses) {
                    if (((ACLMessage) message).getPerformative() != ACLMessage.AGREE) {
                        // TODO handle the case when TR failed to react
                    }
                }
            }
        });
    }

    @SneakyThrows
    private ACLMessage createJobRequestToTR(Material material, GomDefinition destination) {
        GomAgent agent = (GomAgent) myAgent;

        GomInfo from = new GomInfo(new AID(GOM(agent.getDefinition().getNumber()), AID.ISLOCALNAME),
            new PositionInfo(agent.getPosition()));
        GomInfo to = new GomInfo(new AID(GOM(destination.getNumber()), AID.ISLOCALNAME),
            new PositionInfo(destination.getPosition()));
        MaterialInfo materialInfo = new MaterialInfo(material.getName(), material.getWeight());
        GomJobRequest jobRequest = new GomJobRequest(from, to, materialInfo);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID(TR(agent.getDefinition().getNumber()), AID.ISLOCALNAME));
        request.setLanguage(agent.getCodec().getName());
        request.setOntology(agent.getOnto().getName());

        Action action = new Action(new AID(TR(destination.getNumber()), AID.ISLOCALNAME), jobRequest);
        agent.getContentManager().fillContent(request, action);

        return request;

    }
}
