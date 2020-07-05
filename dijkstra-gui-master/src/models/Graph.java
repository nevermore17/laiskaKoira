package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    public int stepRealisation = 0;
    private int count = 1;
    private List<Node> nodes = new ArrayList<>();          //Лист Вершин графа
    private List<Edge> edges = new ArrayList<>();          //ЛИСТ Ребер графа

    private Node source;                                   //Стартовая Вершина

    private boolean solved = false;                        //Флаг Решенности задачи(Найден путь или нет)

    public void setSolved(boolean solved) {
        this.solved = solved;
    }                                                      //Setter Решенности задачи

    public boolean isSolved() {
        return solved;
    }                                                      //Getter Решенности задачи

    public void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }                                                      //Setter Листа Вершин графа

    public List<Node> getNodes(){
        return nodes;
    }                                                      //Getter Листа Вершин графа

    public void setEdges(List<Edge> edges){
        this.edges = edges;
    }                                                      //Setter Листа Ребер графа

    public List<Edge> getEdges(){
        return edges;}                                     //Getter Листа Ребер графа

    public boolean isNodeReachable(Node node){             //Имеет ли вершина Свое ребро
        for(Edge edge : edges)
            if(node == edge.getNodeOne() || node == edge.getNodeTwo())
                return true;

        return false;
    }

    public void setSource(Node node){                      //Устанавливает Первую вершину
        if(nodes.contains(node)) {
            source = node;
        }
    }


    public Node getSource(){
        return source;
    }


    public boolean isSource(Node node){
        return node == source;
    }

    public void addNode(Point coord){
        Node node = new Node(coord);
        addNode(node);
    }

    public void addNode(Node node){
        node.setId(count++);
        nodes.add(node);
        if(node.getId()==1)
            source = node;
    }

    public void addEdge(Edge new_edge){                   //Проверяет, есть ли ребро которое мы хотим добавить и если нет то добавляет
        boolean added = false;
        for(Edge edge : edges){
            if(edge.equals(new_edge)){
                added = true;
                break;
            }
        }
        if(!added)
            edges.add(new_edge);
    }

    public void deleteNode(Node node){                     //Удаляет вершину
        List<Edge> delete = new ArrayList<>();
        for (Edge edge : edges){
            if(edge.hasNode(node)){
                delete.add(edge);
            }
        }
        for (Edge edge : delete){
            edges.remove(edge);
        }
        nodes.remove(node);
    }

    public void clear(){                                   //Чистит граф
        count = 1;
        nodes.clear();
        edges.clear();
        solved = false;

        source = null;
        //destination = null;
    }

}
