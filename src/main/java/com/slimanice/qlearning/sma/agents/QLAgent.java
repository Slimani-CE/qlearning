package com.slimanice.qlearning.sma.agents;


import com.slimanice.qlearning.sequential.QLearning;
import com.slimanice.qlearning.utils.Action;
import com.slimanice.qlearning.utils.InitUnit;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

// Q Learning agent
public class QLAgent extends Agent {
    @Override
    protected void setup() {
        // Get the master agent's AID
        AID masterAgentAID = null;
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("master");
        template.addServices(sd);
        try {
            boolean isMasterAgentFound = false;
            while(!isMasterAgentFound) {
                DFAgentDescription[] result = DFService.search(this, template);
                if (result.length > 0) {
                    masterAgentAID = result[0].getName();
                    isMasterAgentFound = true;
                }
                else {
                    System.out.println("Master agent not found. Retrying in 1 seconds...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        InitUnit initUnit = (InitUnit) getArguments()[0];

        QLearning qLearning = new QLearning(initUnit.getGrid(), initUnit.getStartI(), initUnit.getStartJ(), initUnit.getGridSize(), initUnit.getAlpha(), initUnit.getGamma(), initUnit.getMaxEpoch());

        qLearning.runQLearning();

        ArrayList<Action> bestPath = qLearning.getBestPath();
        String result = "";
        for(int i = 0; i < bestPath.size(); i++){
            Action action = bestPath.get(i);
            result += "Action" + action.getCurrI() + "-" +action.getCurrJ() + "-" + action.getAction();
        };
        sendMsg(masterAgentAID, "best-path", result);
    }

    private void sendMsg(AID aid, String type, String content) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(aid);
        message.setContent(content);
        message.setConversationId(type);
        send(message);
    }
}
