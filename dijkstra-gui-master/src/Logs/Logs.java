package Logs;

import models.Node;
import java.util.*;

public class Logs {

    private Queue<Map<Node, Node>>predecessorsLog;
    private Queue<Map<Node, Integer>> distancesLog;
    private Queue<Node> visitedQueue;

    public Logs(){

        predecessorsLog = new LinkedList<>();

        distancesLog = new LinkedList<>();

        visitedQueue = new LinkedList<>();

    };
    public void addLogs(Map<Node, Node> predecessors, Map<Node, Integer> distances, Node visited){

        predecessorsLog.add(predecessors);

        distancesLog.add(distances);

        visitedQueue.add(visited);

    }
    public Map<Node, Node>  getPredecessorsLog() { return predecessorsLog.remove();}

    public Map<Node, Integer> getDistancesLog(){return distancesLog.remove();}

    public Node getVisitedLog() {
        return visitedQueue.remove();
    }

    public boolean isEmptyVisited(){
        return visitedQueue.isEmpty();
    }
    //вывод меток на каком-то шаге
    //Какая вершина добавленна на каком-то шаге
    public void clear(){
        predecessorsLog.clear();
        distancesLog.clear();
        visitedQueue.clear();
    }
}


