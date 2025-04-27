import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Site extends Agent {
    int id, tailleC,l=0, valuer = 0;
    boolean initiator, marqueurPresent= false;
    String RdpPath,successeur , etat = Etat.ACTIF, couleur = Couleur.BLANC;
    ArrayList<String> peers = new ArrayList<String>();
    Rdp rdp = new Rdp();


    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null){
            id = Integer.parseInt((String) args[0]);
            initiator= Boolean.parseBoolean((String) args[1]);

            for(int i = 2; i < args.length-3; i++){
                peers.add((String) args[i]);
            }

            RdpPath   = (String) args[args.length-3];
            tailleC   = Integer.parseInt((String)args[args.length-2]);
            successeur= (String) args[args.length-1];

            System.out.println("id: "+id+" initiator: "
                    + initiator+" peers: "+ peers +" RdpName: "+ RdpPath +
                    " TailleC: "+tailleC + " successeur: "+successeur
                    );
            rdp = new Rdp();
            rdp = rdp.loadRdp(RdpPath);
            if (initiator){
                marqueurPresent = true;
                this.addBehaviour(new LaunchCalculation());
                this.addBehaviour(new EmissionDeMarqueur());
            }

            this.addBehaviour(new ConsulterBoite());
        }


    }
    public class LaunchCalculation extends OneShotBehaviour{
        @Override
        public void action() {
            int siteId = rdp.hachage(rdp.getM0(), peers.size());
            String agentName = peers.get(siteId);

            if (!agentName.equals(  getLocalName())){
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
            etat = Etat.PASSIVE;
            if (msg != null){
                try{
                    Message contentObject = (Message) msg.getContentObject();

                    if(contentObject.getType().equals(MessageTypes.MARQUAGE)){
                        int[] M = (int[]) contentObject.getVal();
                        couleur = Couleur.BLANC;
                        etat    = Etat.ACTIF;
                        ProcessMarking(M);
                    }

                    if (contentObject.getType().equals(MessageTypes.MARQUEUR)){
                        l = (Integer) contentObject.getVal();
                        
                        marqueurPresent = true;
                        if ( (l == tailleC) && (couleur.equals(Couleur.NOIR) )){
                            System.err.println("Terminision Detectee");
                        }else {
                            addBehaviour(new EmissionDeMarqueur());
                        }
                    }


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
            msg.setContentObject(new Message(
                    MessageTypes.MARQUAGE,
                    M
            ));
            send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void sendMarqueur(String agentName, int l){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));

        try{
            msg.setContentObject(new Message(
                    MessageTypes.MARQUEUR,
                    l
            ));
            System.out.println("Marqueur sent from: "+ getLocalName()+
                    " towards: "+agentName+" of value: "+l);
            send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class EmissionDeMarqueur extends Behaviour{

        @Override
        public void action() {
            if (couleur.equals(Couleur.BLANC)){
                valuer = 0;
            }else {
                valuer = l + 1;
            }

            sendMarqueur(successeur,valuer);
            couleur = Couleur.NOIR;
            marqueurPresent = false;
        }

        @Override
        public boolean done() {
            return !marqueurPresent;
        }
    }

    public static void main(String[] args) {
        String[] commands = new String[3];
        String argument = "";

        argument += "S1:Site(1,false,S1,S2,S3,S4,1erRdp.dat,4,S2);";
        argument += "S2:Site(2,false,S1,S2,S3,S4,1erRdp.dat,4,S3);";
        argument += "S3:Site(3,true,S1,S2,S3,S4,1erRdp.dat,4,S4);";
        argument += "S4:Site(4,false,S1,S2,S3,S4,1erRdp.dat,4,S1);";

        commands[0]= "-cp";
        commands[1]="jade.boot";
        commands[2]= argument;

        jade.Boot.main(commands);
    }
}


class Etat{
    public static String ACTIF = "ACTIF";
    public static String  PASSIVE=  "PASSIVE";
}

class Couleur{
    public static String BLANC = "BLANC";
    public static String NOIR = "NOIR";
}

class MessageTypes {
    public static String MARQUAGE = "MARQUAGE";
    public static String MARQUEUR = "MARQUEUR";
}