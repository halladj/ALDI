import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Site extends Agent {
    int id;
    boolean initiator;
    String RdpPath;
    ArrayList<String> peers = new ArrayList<String>();
    Rdp rdp = new Rdp();

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null){
            id = Integer.parseInt((String) args[0]);
            initiator= Boolean.parseBoolean((String) args[1]);

            for(int i = 2; i < args.length-1; i++){
                peers.add((String) args[i]);
            }

            RdpPath = (String) args[args.length-1];

            System.out.println("id: "+id+" initiator: "
                    + initiator+" peers: "+ peers +" RdpName: "+ RdpPath);
            rdp = new Rdp();
            rdp = rdp.loadRdp(RdpPath);
            if (initiator){
                this.addBehaviour(new LaunchCalculation());
            }

            this.addBehaviour(new ConsulterBoite());
        }


    }
    public class LaunchCalculation extends OneShotBehaviour{
        @Override
        public void action() {
            int siteId = rdp.hachage(rdp.getM0(), peers.size());
            String agentName = peers.get(siteId);
//            System.out.println("Agent-id: "+ siteId+" Agent-Name: "+agentName);

            if (!agentName.equals(  getLocalName())){
                //TODO: Send Marquage to the appropriate site.
                sendMarquage(agentName, rdp.getM0());
            }else{
                ProcessMarking(rdp.getM0());
            }
        }
    }

    private void ProcessMarking(int[] M) {
        if (rdp.searchMarquage(M) == -1){
            rdp.addMarquage(M);
            for (int t = 0; t < rdp.nbTransition; t++) {
                if (rdp.t_franchissable(t, M) == 1) {
                    int[] nM = rdp.succ(t, M);
                    int siteId = rdp.hachage(nM, peers.size());
                    String agentName = peers.get(siteId );
                    System.out.println(
                        Arrays.toString(M) + " " + getLocalName() +
                                " --t"+(t+1)+"--> " + agentName + " " + Arrays.toString(nM)
                    );
                    if (!agentName.equals(getLocalName())) {
                        //TODO: Send Marquage to the appropriate site.
                        sendMarquage(agentName, nM);
                    } else {
                        ProcessMarking(nM);
                    }
                }
            }
        }
    }

    public class ConsulterBoite extends CyclicBehaviour{

        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null){
                try{
                    int[] M = (int[]) msg.getContentObject();
//                    System.out.println("Agent: "+getLocalName()+" has received: "+
//                            Arrays.toString(M)+" from Agent: "+ msg.getSender().getLocalName());

//                    if (!marquageList.contains(M)){
//                        marquageList.add(M);
//                    }

                    ProcessMarking(M);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void sendMarquage(String agentName, int[] M){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));

        try{
            msg.setContentObject(M);
            send(msg);
//            System.out.println("Agent "+getLocalName()+ " Sent "+ Arrays.toString(M)
//                    +" to Agent: "+agentName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String[] commands = new String[3];
        String argument = "";

        argument += "S1:Site(1,false,S1,S2,S3,S4,1erRdp.dat);";
        argument += "S2:Site(2,false,S1,S2,S3,S4,1erRdp.dat);";
        argument += "S3:Site(3,true,S1,S2,S3,S4,1erRdp.dat);";
        argument += "S4:Site(4,false,S1,S2,S3,S4,1erRdp.dat);";

        commands[0]= "-cp";
        commands[1]="jade.boot";
        commands[2]= argument;

        jade.Boot.main(commands);
    }
}
