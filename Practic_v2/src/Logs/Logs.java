package Logs;

import models.Node;
import java.util.*;

public class Logs {

    private Queue<String> distancesLog;
    private Queue<Node> visitedQueue;
    private Queue<String> endDistancesLog;
    private Queue<String> edgeLogs;

    public Logs(){


        distancesLog = new LinkedList<>();
        endDistancesLog = new LinkedList<>();
        visitedQueue = new LinkedList<>();
        edgeLogs = new LinkedList<>();

    };

    public void addLogs(Map<Node, Node> predecessors, Map<Node, Integer> distances, Node visited, String edgeLog){


        String disTmp = "Destination Now:\n";

        for(Node node : distances.keySet()) {
            if (distances.get(node) != Integer.MAX_VALUE) {

                disTmp += "\t" + node.nodeToString() + ": " + distances.get(node) + "\n";
            }
        }
        for(Node node : distances.keySet()){
            if(distances.get(node) == Integer.MAX_VALUE){
                disTmp += "\t" + node.nodeToString() + ": ";
                disTmp += "\u221E\n";}

        }
        distancesLog.add(disTmp);
        visitedQueue.add(visited);
        edgeLogs.add(edgeLog);

    }
    public void addEndDistantLog(HashSet<Node> nodeHashSet,  Map<Node, Integer> distances){
        String str = "This step has next end Nodes:\n";
        for(Node node: nodeHashSet){
            str += "\t" + node.nodeToString() + " = " + distances.get(node).toString() + "\n";
        }
        for(int i = 0; i < 128 ; i++){
            str += "-";
        }
        endDistancesLog.add(str);
    }


    public String getDistancesLog(){return distancesLog.remove();}

    public Node getVisitedLog() {
        return visitedQueue.remove();
    }

    public String getEdgeLog(){
        return edgeLogs.remove();
    }

    public String getEndDistancesLog(){return  endDistancesLog.remove();}

    public boolean isEmptyVisited(){
        return visitedQueue.isEmpty();
    }

    public void clear(){

        distancesLog.clear();
        visitedQueue.clear();
    }
}


