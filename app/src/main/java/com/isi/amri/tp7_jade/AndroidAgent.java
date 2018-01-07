package com.isi.amri.tp7_jade;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * Created by root on 29/12/17.
 */

public class AndroidAgent extends Agent {



    public AndroidAgent() {
    }

    @Override
    protected void setup() {

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID("MyAgent", AID.ISLOCALNAME));
        message.setContent("bonjour MyAgent je suis MyAgentAndroid");
        send(message);
        System.out.println("message bien envoyer :" + message);
    }

}
