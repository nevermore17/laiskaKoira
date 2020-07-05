package Logs;

import models.Node;
import java.util.*;

public class Logs {

    private Queue<String> distancesLog;
    private Queue<Node> visitedQueue;

    public Logs(){


        distancesLog = new LinkedList<>();

        visitedQueue = new LinkedList<>();

    };

    public void addLogs(Map<Node, Node> predecessors, Map<Node, Integer> distances, Node visited){


        String disTmp = "Destination Now:\n";

        for(Node node : distances.keySet()) {
            if (distances.get(node) != Integer.MAX_VALUE) {

                disTmp += node.nodeToString() + ": ";
                disTmp += distances.get(node) + "\n";
            }
        }
        for(Node node : distances.keySet()){
            if(distances.get(node) == Integer.MAX_VALUE){
                disTmp += node.nodeToString() + ": ";
                disTmp += "\u221E\n";}

        }
        distancesLog.add(disTmp);
        visitedQueue.add(visited);

    }



    public String getDistancesLog(){return distancesLog.remove();}

    public Node getVisitedLog() {
        return visitedQueue.remove();
    }

    public boolean isEmptyVisited(){
        return visitedQueue.isEmpty();
    }

    public void clear(){
;
        distancesLog.clear();
        visitedQueue.clear();
    }
}


