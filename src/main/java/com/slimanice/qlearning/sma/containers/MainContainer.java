package com.slimanice.qlearning.sma.containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        profile.setParameter(ProfileImpl.GUI, "false");
        AgentContainer mainContainer = runtime.createMainContainer(profile);
        try {
            mainContainer.start();
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }

    }
}
