package com.slimanice.qlearning.sma.agents;


import com.slimanice.qlearning.utils.Action;
import com.slimanice.qlearning.utils.InitUnit;
import com.slimanice.qlearning.utils.Path;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class MasterAgent extends Agent {
    private int solutionsFound = 0;
    private int nbrAgents;
    private int grid[][];
    private ArrayList<Path> paths = new ArrayList<>();
    @Override
    protected void setup() {
        // Get number of agents
        InitUnit initUnit = (InitUnit) getArguments()[0];
        nbrAgents = initUnit.getNbrAgents();
        grid = initUnit.getGrid();

        // Register the master agent in the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("master");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        // Cyclic behaviour to handle messages
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (msg.getConversationId().equals("best-path")) {
                        Path path = new Path();
                        ArrayList<Action> actions = new ArrayList<>();
                        String[] strActions = content.split("Action");
                        for(String action : strActions){
                            if(action.isEmpty())
                                continue;
                            String[] actionDetails = action.split("-");
                            int currI = Integer.parseInt(actionDetails[0]);
                            int currJ = Integer.parseInt(actionDetails[1]);
                            String detail = actionDetails[2];
                            actions.add(new Action(currI, currJ, detail));
                        }
                        path.setPath(actions);
                        path.setLength(actions.size());
                        path.setAgent(msg.getSender());
                        paths.add(path);
                        solutionsFound++;
                    }
                }
                else {
                    block();
                }
            }

            @Override
            public boolean done() {
                if(solutionsFound == nbrAgents)
                    displayResults();

                return solutionsFound == nbrAgents;
            }

            private void displayGrid() {
                System.out.println("Grid = [");
                for(int[] row : grid) {
                    System.out.print("\t[");
                    for (int cell : row) {
                        System.out.print(cell + ", ");
                    }
                    System.out.println("], ");
                }
                System.out.println("]");
                System.out.println("Start position: ("+initUnit.getStartI()+","+initUnit.getStartJ()+")");
            }

            private void displayResults() {
                // Sort the solutions
                paths.sort(Comparator.comparingInt(Path::getLength));
                displayGrid();
                System.out.println("All solutions found:");
                for(Path path : paths){
                    System.out.println("\t" + path.getAgent().getLocalName() + " Path Length: " + path.getLength());
                    for(Action action : path.getPath())
                        System.out.println("\t\t Current I: " + action.getCurrI() + " Current J: " + action.getCurrJ() + " Next Action: " + action.getAction());
                }
            }

        });
    }
}
