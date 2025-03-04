import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class SenderAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println(getAID().getLocalName());
        Object[] args = getArguments();
        if (args != null){
            System.out.println("Some args" + Arrays.toString(args));
        }

        addBehaviour(new Send());

    }

    public class Send extends OneShotBehaviour{

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Hello World");
            msg.addReceiver(new AID("receiver", AID.ISLOCALNAME));

            send(msg);
        }
    }
}
