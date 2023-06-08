package com.slimanice.qlearning.sma.containers;

import com.slimanice.qlearning.sma.agents.MasterAgent;
import com.slimanice.qlearning.sma.agents.QLAgent;
import com.slimanice.qlearning.utils.InitUnit;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class SimpleContainer {
    private static final InitUnit initUnit = new InitUnit();
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createMainContainer(profile);

        for (int i = 0; i< initUnit.getNbrAgents(); i++){
            try {
                AgentController agent = agentContainer.createNewAgent("QLAgent-" + (i + 1), QLAgent.class.getName(), new Object[]{initUnit});
                agent.start();
            }catch (StaleProxyException e) {
                System.out.println("Error while starting QLAgent- " + i);
                throw new RuntimeException(e);
            }
        }
        try {
            AgentController masterAgent = agentContainer.createNewAgent("MasterAgent", MasterAgent.class.getName(), new Object[]{initUnit});
            masterAgent.start();
        } catch (StaleProxyException e) {
            System.out.println("Error while starting MasterAgent");
            throw new RuntimeException(e);
        }
    }
}
