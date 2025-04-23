import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph implements Serializable{
    // Key-Value data structure, where
    // the key:String represents a source vertex.
    // and the Edge represents, the Label
    // on the edge and target vertex.
    private HashMap<String, ArrayList<Edge>> adjMatrix = new HashMap<>();

    public void addEdge(String source, String target, String label){
        ArrayList<Edge> vector  = adjMatrix.get(source);
        if (vector == null){
            vector = new ArrayList<Edge>();
            vector.add(new Edge(label, target));
            adjMatrix.put(
                    source, vector
            );
        }else{
            vector.add(new Edge(label, target));
        }
    }

    public void printGraph(){
        for (Map.Entry<String, ArrayList<Edge>> entry: adjMatrix.entrySet()){
            String key = entry.getKey();
            ArrayList<Edge> list = entry.getValue();
            for (Edge edge: list){
                System.out.print(key+" ->");
                System.out.println(edge.toString());
            }
        }
    }
}


class Edge implements Serializable{
    String label;
    String target;

    public Edge(String label, String target){
        this.label = label;
        this.target= target;
    }

    @Override
    public String toString() {
        return label+" -> "+target;
    }
}