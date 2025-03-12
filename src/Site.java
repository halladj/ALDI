import jade.core.Agent;

import java.util.ArrayList;

public class Site extends Agent {
    int id;
    boolean initiator;
    String RdpPath;
    ArrayList<String> peers = new ArrayList<String>();

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

            if (initiator){
                Rdp rdp = new Rdp();
                rdp = rdp.loadRdp(RdpPath);
                rdp.displayRDP();

                int id = rdp.hachage(rdp.getM0(), peers.size());
                System.out.println("Agent-id: "+ id);
            }
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
