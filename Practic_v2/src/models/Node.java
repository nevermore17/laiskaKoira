package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Point coord = new Point();                                                                                  //Координаты вершина
    private int id;                                                                                                     //Id вершины
    private List<Node> path;                                          //Путь до вершины                                                                                  //Путь от вершины до вершны
    public int status;
    public Node(){}
    public boolean isSourse = false;//Пустой конструктор

    public Node(int id){
        this.id = id;
        status = 0;

    }                                                                            //Конструктор по id

    public Node(Point p){
        this.coord = p;
        status = 0;
    }                                                                            //Конструктор по координатам

    public void setId(int id){
        this.id = id;
    }                                  //Установить Id

    public void setCoord(int x, int y){
        coord.setLocation(x, y);
    }              //Установить координаты

    public Point getCoord(){
        return coord;
    }                                    //Выдает координаты

    public void setPath(List<Node> path) {
        this.path = path;
    }                   //Устанавливает путь до вершины

    public List<Node> getPath() {
        return path;
    }                                 //Выдает путь до вершины

    public int getX(){
        return (int) coord.getX();
    }

    public int getY(){
        return (int) coord.getY();
    }

    public int getId(){
        return id;
    }

    public boolean isId(List<Node> nodeList){
        for(Node node : nodeList){
            if(node.getId() == id) return true;
        }
        return false;
    }


    public String nodeToString() {
        return "Node " + getId();
    }
}
