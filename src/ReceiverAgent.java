import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiverAgent extends Agent{
    String peer;
    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null){
            peer = (String) args[0];
        }
        addBehaviour(new ConsulterBoite());
    }



    //TODO: Extend the proper behaviour
    public class ConsulterBoite extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if(msg != null){
                String receivedMessage = msg.getContent();
                System.out.println(getLocalName() + " received: "+receivedMessage);

                //TODO: send back a "Pong" msg towards the Sender-Agent.
                ACLMessage pong = new ACLMessage(ACLMessage.INFORM);
                pong.setContent("Pong");
                pong.addReceiver(new AID(peer, AID.ISLOCALNAME));

                System.out.println(getLocalName() + " sent: Pong");
                send(pong);
            }
        }
    }
}
