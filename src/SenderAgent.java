import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class SenderAgent extends Agent{
    String peer;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null){
            peer = (String) args[0];
        }
        addBehaviour(new Send());

        addBehaviour(new ConsulterBoite());
    }

    //TODO: Extend the proper behaviour
    public class Send extends OneShotBehaviour {
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Ping");
            msg.addReceiver(new AID(peer, AID.ISLOCALNAME));

            send(msg);
            System.out.println(getLocalName() + " sent: Ping");
        }
    }

    //TODO: Extend the proper behaviour
    public class ConsulterBoite extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if(msg != null){
                String receivedMessage = msg.getContent();
                System.out.println(getLocalName() + " received: "+receivedMessage);
            }
        }
    }

}
