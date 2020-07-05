package models;

public class Edge {
    private Node one;
    private Node two;
    private int weight = 1;

    public Edge(Node one, Node two){
        this.one = one;
        this.two = two;
    }

    public Node getNodeOne(){
        return one;
    }       //Первая вершина ребра

    public Node getNodeTwo(){
        return two;
    }       //Вторая вершина ребра

    public void setWeight(int weight){
        this.weight = weight;
    } //Записывает вес ребра

    public int getWeight(){
        return weight;
    }      //Возвращает вес ребра

    public boolean hasNode(Node node){
        return one==node || two==node;
    }                                                //Передается вершина и проверяется содержится ли она в данномм ребре

    public boolean equals(Edge edge) {              
        return (one ==edge.one && two ==edge.two) || (one ==edge.two && two ==edge.one) ;
    }

    @Override
    public String toString() {
        return "Edge ~ "
                + getNodeOne().getId() + " - "
                + getNodeTwo().getId();
    }
}
