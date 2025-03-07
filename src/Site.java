import jade.core.Agent;

import java.util.ArrayList;

public class Site extends Agent {
    int id;
    boolean initiator;
    ArrayList<String> peers = new ArrayList<String>();

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null){
            id = Integer.parseInt((String) args[0]);
            initiator= Boolean.parseBoolean((String) args[1]);

            for(int i = 2; i < 6; i++){
                peers.add((String) args[i]);
            }

            System.out.println("id: "+id+" initiator: "
                    + initiator+" peers: "+ peers);
        }


    }


    public static void main(String[] args) {
        String[] commands = new String[3];
        String argument = "";

        argument += "S1:Site(1,false,S1,S2,S3,S4);";
        argument += "S2:Site(2,false,S1,S2,S3,S4);";
        argument += "S3:Site(3,true,S1,S2,S3,S4);";
        argument += "S4:Site(4,false,S1,S2,S3,S4);";

        commands[0]= "-cp";
        commands[1]="jade.boot";
        commands[2]= argument;

        jade.Boot.main(commands);
    }
}
