import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

public class ReceiverAgent extends Agent{

    @Override
    protected void setup() {
        System.out.println(getAID().getLocalName());

        Object[] args = getArguments();
        if (args != null){
            System.out.println(Arrays.toString(args));
        }

        addBehaviour(new ConsulterBoite());
    }

    public class ConsulterBoite extends CyclicBehaviour{

        @Override
        public void action() {
            ACLMessage msg= receive();

            if (msg != null){
                String receivedMessage = msg.getContent();
                System.out.println("The Received Message: "+receivedMessage);
            }
        }
    }
}
