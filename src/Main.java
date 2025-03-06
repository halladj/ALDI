public class Main {
    public static void main(String[] args) {
        String[] commands = new String[3];
        String argument = "";

        // TODO: create a sender-Agent and a Receiver-Agent.
        argument = argument + "sender:SenderAgent(receiver)";
        argument = argument + ";";
        argument = argument + "receiver:ReceiverAgent(sender)";


        commands[0] = "-cp";
        commands[1] = "jade.boot";
        commands[2] = argument;

        jade.Boot.main(commands);

    }
}