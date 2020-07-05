package algo;

import models.Edge;
import models.Graph;
import models.Node;
import Logs.Logs;
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
            return false;
        }

        for(Node node : graph.getNodes()){                          //Есть ли У кождой вершины свое ребро (Проверка на "Одиноких вершин")
            if(!graph.isNodeReachable(node)){
                message = "Graph contains unreachable nodes";
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


        for (Edge neighbor : getNeighbors(source)){
            Node adjacent = getAdjacent(neighbor, source);
            if(adjacent==null)
                continue;

            distances.put(adjacent, neighbor.getWeight());  //Расставлят метки
            predecessors.put(adjacent, source);             //Добавляет каждой смежной source вершине предыдущую вершину source
            unvisited.add(adjacent);                        //Добпавляет вершину в очередь
        }

        dLog.addLogs(predecessors,distances,source);

        while (!unvisited.isEmpty()){                       //
            Node current = unvisited.poll();

            updateDistance(current);

            unvisited.remove(current);
            visited.add(current);
            dLog.addLogs(predecessors,distances,current);
        }

        for(Node node : graph.getNodes()) {
            node.setPath(getPath(node));
        }

        graph.setSolved(true);

    }

    private void updateDistance(Node node){
        int distance = distances.get(node);

        for (Edge neighbor : getNeighbors(node)){
            Node adjacent = getAdjacent(neighbor, node);
            if(visited.contains(adjacent))
                continue;

            int current_dist = distances.get(adjacent);
            int new_dist = distance + neighbor.getWeight();

            if(new_dist < current_dist) {
                distances.put(adjacent, new_dist);
                predecessors.put(adjacent, node);
                unvisited.add(adjacent);
            }
        }
    }

    private Node getAdjacent(Edge edge, Node node) {                      //Выдает вершину ктоторая смежна к вершине Node по ребру Edge если ребро содержит вершину
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

    public List<Node> getDestinationPath( Node node) {
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
