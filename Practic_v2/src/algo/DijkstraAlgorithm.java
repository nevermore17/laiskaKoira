package algo;

import Logs.Logs;
import models.Edge;
import models.Graph;
import models.Node;

import javax.xml.stream.events.EntityDeclaration;
import java.util.*;

public class DijkstraAlgorithm {
    public Logs dLog = new Logs();
    private boolean safe = false;
    private String message = null;

    private Graph graph;                    //Граф11
    private Map<Node, Node> predecessors;   //Массив Предшественников каждой вершине
    private Map<Node, Integer> distances;   //Массив меток

    private PriorityQueue<Node> unvisited;
    private HashSet<Node> visited;          //Массив просмотренных вершин

    public class NodeComparator implements Comparator<Node>  {
        @Override
        public int compare(Node node1, Node node2) {
            return distances.get(node1) - distances.get(node2);     //Разница меток между 2 вершинами
        }
    };

    public DijkstraAlgorithm(Graph graph){
        this.graph = graph;
        predecessors = new HashMap<>();                             //HashMap в котором указанно какая вершина соответствует предыдущей в наденном пути Алгоритма
        distances = new HashMap<>();                                //HashMap в котором указанны метки соответствующие каждой вершине

        for(Node node : graph.getNodes()){
            distances.put(node, Integer.MAX_VALUE);                 //Выставляет метки равные бесконечности для работы графа
        }
        visited = new HashSet<>();                                  //Создает Массив посещенных вершин

        safe = evaluate();                                          //Флаг проверки корректности графа
    } //Конструктор

    private boolean evaluate(){
        if(graph.getSource()==null){                                //Если ли начальная вершина
            message = "Source must be present in the graph";
            graph.stepRealisation = 0;
            return false;
        }

        for(Node node : graph.getNodes()){                          //Есть ли У кождой вершины свое ребро (Проверка на "Одиноких вершин")
            if(!graph.isNodeReachable(node)){
                message = "Graph contains unreachable nodes";
                graph.stepRealisation = 0;
                return false;
            }
        }

        return true;
    }                                 //Метод сравнения вершин

    public void run() throws IllegalStateException {
        if(!safe) {
            throw new IllegalStateException(message);
        }

        unvisited = new PriorityQueue<>(graph.getNodes().size(), new NodeComparator());                                 //Очередь

        Node source = graph.getSource();
        distances.put(source, 0);
        visited.add(source);

        String edgeLogSource = "Check Edge:";
        for (Edge neighbor : getNeighbors(source)){
            Node adjacent = getAdjacent(neighbor, source);
            if(adjacent==null)
                continue;
            else edgeLogSource += "\n\t" + neighbor.edgeToString();

            distances.put(adjacent, neighbor.getWeight());  //Расставлят метки
            predecessors.put(adjacent, source);             //Добавляет каждой смежной source вершине предыдущую вершину source
            unvisited.add(adjacent);                        //Добпавляет вершину в очередь
        }

        dLog.addLogs(predecessors,distances,source, edgeLogSource);

        while (!unvisited.isEmpty()){                       //
            Node current = unvisited.poll();

            HashSet<Edge> edgeHashSet = updateDistance(current);
            String edgeLog = "Check Edge:";
            for(Edge edge: edgeHashSet){
                edgeLog += "\n\t" + edge.edgeToString();
            }
            unvisited.remove(current);
            visited.add(current);
            dLog.addLogs(predecessors,distances,current, edgeLog);
            dLog.addEndDistantLog(visited, distances);
        }

        for(Node node : graph.getNodes()) {
            node.setPath(getPath(node));
        }

        graph.setSolved(true);

    }

    private boolean isVisited(Node node){
        for(Node visNode: visited){
            if(node.equals(visNode)){
                return true;
            }
        }
        return false;
    }

    private HashSet<Edge> updateDistance(Node node){
        int distance = distances.get(node);
        HashSet<Edge> edgeList = new HashSet<>();
        for (Edge neighbor : getNeighbors(node)){
            Node adjacent = getAdjacent(neighbor, node);
            if(visited.contains(adjacent))
                continue;
            else edgeList.add(neighbor);


            int current_dist = distances.get(adjacent);
            int new_dist = distance + neighbor.getWeight();

            if(new_dist < current_dist) {
                distances.put(adjacent, new_dist);
                predecessors.put(adjacent, node);
                unvisited.add(adjacent);
            }
        }
        return edgeList;
    }

    private Node getAdjacent(Edge edge, Node node) {                      //Выдает вершину которая смежна к вершине Node по ребру Edge если ребро содержит вершину
        if(edge.getNodeOne()!=node && edge.getNodeTwo()!=node)            //Можно заменить на !edge.hasNode(node)
            return null;

        return node==edge.getNodeTwo()?edge.getNodeOne():edge.getNodeTwo();
    }

    private List<Edge> getNeighbors(Node node) {                          //Выдает лист ребер связанных с данной вершиной
        List<Edge> neighbors = new ArrayList<>();

        for(Edge edge : graph.getEdges()){
            if(edge.getNodeOne()==node ||edge.getNodeTwo()==node)
                neighbors.add(edge);
        }

        return neighbors;
    }

    public Integer getDistance(Node node){
        return distances.get(node);
    }

    public List<Node> getDestinationPath(Node node) {
        for(Node n: graph.getNodes()){
            if(n.getId() == node.getId()) return getPath(n);
        }
        return getPath(node);
    }

    public List<Node> getPath(Node node){
        List<Node> path = new ArrayList<>();

        Node current = node;
        path.add(current);
        while (current!=graph.getSource()){
            current = predecessors.get(current);
            path.add(current);
        }

        Collections.reverse(path);

        return path;
    }

    public String getPathToString(Node node){
        List<Node> path = getPath(node);
        String ret = "Path to " + node.nodeToString() + ":\t" + path.get(0).nodeToString();
        for(int i = 1; i < path.size(); i++){
            ret += " -> " + path.get(i).nodeToString();
        }
        return ret;
    }

    public Graph getGraph() {
        return graph;
    }

    public void clear(){
        dLog.clear();
        unvisited.clear();
        visited.clear();
        distances.clear();
        predecessors.clear();
        message = null;
        safe = false;
    }
}
