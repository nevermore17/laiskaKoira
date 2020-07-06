package gui;

import models.Edge;
import models.Graph;
import models.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;


public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener {

    private DrawUtils drawUtils;

    private Graph graph;

    private Node selectedNode = null;
    private Node hoveredNode = null;
    private Edge hoveredEdge = null;

    private java.util.List<Node> path = null;

    private java.util.List<Node> tmpPath = null;

    private Point cursor;
 //   public Graphics g;

    public GraphPanel(Graph graph){
        this.graph = graph;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void uResetGraphPanel(Graph graph){
        this. graph = graph;
    }

    public void setPath(List<Node> path) {
        this.path = path;
        hoveredEdge = null;
        repaint();
    }

    public void setTmpPath(List<Node> path) {
        this.tmpPath = path;
        hoveredEdge = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawUtils = new DrawUtils(graphics2d);

        if(path != null && graph.stepRealisation == 2){
            drawUtils.drawPath(path);
        }


        if(selectedNode != null && cursor != null){
            Edge e = new Edge(selectedNode, new Node(cursor));
            drawUtils.drawEdge(e);
        }

        for(Edge edge : graph.getEdges()){
            if(edge == hoveredEdge)
                drawUtils.drawHoveredEdge(edge);
            drawUtils.drawEdge(edge);
        }

        for(Node node : graph.getNodes()){



            if(graph.stepRealisation == 0) {
                if (node == selectedNode || node == hoveredNode)
                    drawUtils.drawHalo(node);
                if (!graph.isSource(node)){
                    drawUtils.drawNode(node);
                }
            } else  {
                if(node.status == 0) drawUtils.drawNode(node);
                else if(node.status == 1) {
                    drawUtils.drawCurrentNode(node);
                }
                else drawUtils.drawDestinationNode(node);
            }if (graph.isSource(node))  drawUtils.drawSourceNode(node);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Node selected = null;
        for(Node node : graph.getNodes()) {
            if(DrawUtils.isWithinBounds(e, node.getCoord())){
                System.out.println(node.getX() + " " + node.getY());
                selected = node;
                break;
            }
        }
        if(selected!=null && graph.stepRealisation == 0) {
            if(e.isControlDown() && e.isShiftDown()){
                graph.deleteNode(selected);
                graph.setSolved(false);
                repaint();
                return;
            } else if(e.isControlDown() && graph.isSolved()){

            } else if(e.isShiftDown()){
                if(SwingUtilities.isLeftMouseButton(e)){
                    graph.setSource(selected);
                } else if(SwingUtilities.isRightMouseButton(e)) {

                }else
                    return;
                graph.setSolved(false);
                repaint();
                return;
            }
        }

        if(hoveredEdge!=null && graph.stepRealisation == 0){
            if(e.isControlDown() && e.isShiftDown()){
                graph.getEdges().remove(hoveredEdge);
                hoveredEdge = null;
                graph.setSolved(false);
                repaint();
                return;
            }

            String input = JOptionPane.showInputDialog("Enter weight for " + hoveredEdge.toString()
                    + " : ");
            try {
                int weight = Integer.parseInt(input);
                if (weight > 0) {
                    hoveredEdge.setWeight(weight);
                    graph.setSolved(false);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Weight should be positive");
                }
            } catch (NumberFormatException nfe) {}
            return;
        }

        if((e.isShiftDown() || graph.stepRealisation == 2) && selected != null && SwingUtilities.isRightMouseButton(e))  {
            if(!graph.isSource(selected)) {
                setPath(selected.getPath());
            }
            else JOptionPane.showMessageDialog(null, "Source can't be set as Destination");
        }

        for(Node node : graph.getNodes()) {
            if(DrawUtils.isOverlapping(e, node.getCoord()) && !(e.isShiftDown() || graph.stepRealisation == 2) && selected != null && SwingUtilities.isRightMouseButton(e)){
                JOptionPane.showMessageDialog(null, "Overlapping Node can't be created");
                return;
            }
        }

        if(graph.stepRealisation == 0){
            graph.addNode(e.getPoint());
            System.out.println(e.getX() + " " + e.getY());
        }
        graph.setSolved(false);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Node node : graph.getNodes()) {
            if(selectedNode !=null && node!= selectedNode && DrawUtils.isWithinBounds(e, node.getCoord())){
                Edge new_edge = new Edge(selectedNode, node);
                graph.addEdge(new_edge);
                graph.setSolved(false);
            }
        }
        selectedNode = null;
        hoveredNode = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        hoveredNode = null;

        for (Node node : graph.getNodes()) {
            if(selectedNode ==null && DrawUtils.isWithinBounds(e, node.getCoord())){
                selectedNode = node;
            } else if(DrawUtils.isWithinBounds(e, node.getCoord())) {
                hoveredNode = node;
            }
        }

        if(selectedNode !=null){
            if(e.isControlDown()){
                selectedNode.setCoord(e.getX(), e.getY());
                cursor = null;
                repaint();
                return;
            }

            cursor = new Point(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(e.isControlDown()){
            hoveredNode = null;
            for (Node node : graph.getNodes()) {
                if(DrawUtils.isWithinBounds(e, node.getCoord())) {
                    hoveredNode = node;
                }
            }
        }

        hoveredEdge = null;

        for (Edge edge : graph.getEdges()) {
            if(DrawUtils.isOnEdge(e, edge)) {
                hoveredEdge = edge;
            }
        }

        repaint();
    }

    public void reset(){
        if(graph != null) graph.clear();
        if(path != null ) path.clear();
        selectedNode = null;
        hoveredNode = null;
        hoveredEdge = null;
        repaint();
    }

    public void file(){

    }
}



