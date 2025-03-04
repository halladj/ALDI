public class Main {
    public static void main(String[] args) {
        String[] commands = new String[3];
        String arg = "";

        arg = arg + "sender:SenderAgent";
        arg = arg + ";";
        arg = arg + "receiver:ReceiverAgent";

        commands[0] = "-cp";
        commands[1] = "jade.boot";
        commands[2] = arg;

        jade.Boot.main(commands);
    }
}