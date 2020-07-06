package gui;

import algo.DijkstraAlgorithm;
import models.Edge;
import models.Graph;
import models.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;

public class MainWindow extends JPanel {

    private Node node;
    private int state;
    private Graph graph;
    public DijkstraAlgorithm dijkstraAlgorithm;
    private GraphPanel graphPanel;
    private FileChooserTest fileChooser;
    private String fileContent;

    public MainWindow(){
        this.state = 0;
        super.setLayout(new BorderLayout());
        setGraphPanel();
    }

    private void setGraphPanel(){
        graph = new Graph();
        graphPanel = new GraphPanel(graph);
        graphPanel.setPreferredSize(new Dimension(9000, 4096));

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(graphPanel);
        scroll.setPreferredSize(new Dimension(750, 500));
        scroll.getViewport().setViewPosition(new Point(4100, 0));
        add(scroll, BorderLayout.CENTER);
        setTopPanel();
        setButtons();
    }

    private void setTopPanel() {
        JLabel info = new JLabel("Dijkstra Algorithm Visualiser by Brigade");
        info.setForeground(new Color(230, 220, 250));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 191, 255));
        panel.add(info);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panel, BorderLayout.NORTH);
    }

    private void setButtons(){
        JButton run = new JButton();
        setupIcon(run, "run");
        run.setToolTipText("Запуск алгоритма");
        JButton reset = new JButton();
        setupIcon(reset, "reset");
        reset.setToolTipText("Очищение холста");
        final JButton info = new JButton();
        setupIcon(info, "info");
        info.setToolTipText("Инструкция");
        JButton file = new JButton();
        setupIcon(file, "file");
        file.setToolTipText("Ввод из файла");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DrawUtils.parseColor("#DDDDDD"));
        buttonPanel.add(reset);
        buttonPanel.add(run);
        buttonPanel.add(info);
        buttonPanel.add(file);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = 0;
                graph.stepRealisation = 0;
                graphPanel.reset();
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Click on empty space to create new node\n" +
                                "Drag from node to node to create an edge\n" +
                                "Click on edges to set the weight\n\n" +
                                "Combinations:\n" +
                                "Shift + Left Click      :    Set node as source\n" +
                                "Shift + Right Click     :    Check path for node after ending of algorithm\n" +
                                "Right Click             :    Check paths after ending of step realization" +
                                "Ctrl  + Drag              :    Reposition Node\n" +
                                "Ctrl  + Shift + Click   :    Delete Node/Edge\n");
            }
        });

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    switch (state){
                        case 0:
                            System.out.println("Algorithm work");
                            graph.stepRealisation = 1;
                            dijkstraAlgorithm = new DijkstraAlgorithm(graph);
                            dijkstraAlgorithm.run();
                            graph = dijkstraAlgorithm.getGraph();
                            state = 1;
                            node = dijkstraAlgorithm.dLog.getVisitedLog();
                            for(Node nodeE : graph.getNodes()){
                                if(nodeE.getId() == node.getId()){
                                    nodeE.status = 1;
                                }
                            }
                            repaint();
                            break;
                        case 1:
                            if(dijkstraAlgorithm.dLog.isEmptyVisited()) {
                                state = 2;
                                repaint();
                                graph.stepRealisation = 2;
                                for(Node nodeE : graph.getNodes()){
                                    nodeE.status = 0;
                                }
                                System.out.println("Check Graph");

                                break;
                            }
                            node.status = 2;

                            for(Node nodeE : graph.getNodes()){

                                if(nodeE.getId() == node.getId())

                                    nodeE.status = 2;

                            }
                            node = dijkstraAlgorithm.dLog.getVisitedLog();
                            for(Node nodeE : graph.getNodes()){

                                if(nodeE.getId() == node.getId()){
                                    nodeE.status = 1;

                                }
                            }

                            System.out.println("Now visited: " + node.nodeToString());
                            System.out.println(dijkstraAlgorithm.dLog.getDistancesLog());
                            repaint();
                            break;
                        case 2:
                            System.out.println("End of work");
                            state = 0;

                            graphPanel.uResetGraphPanel(graph);
                            break;
                    }
                } catch (IllegalStateException ise){
                    JOptionPane.showMessageDialog(null, ise.getMessage());
                }
            }
        });
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state = 0;
                graph.stepRealisation = 0;

                fileChooser = new FileChooserTest();
                graphPanel.add(fileChooser.label);
                try {
                    try{
                    fileChooser.file.list();
                    fileContent = fileChooser.readUsingFiles(fileChooser.file.getPath());
                    System.out.println(fileContent);
                    graphPanel.reset();
                        graph = new Graph();
                        Node sourceNode = null, tmpNode1 = null, tmpNode2 = null;
                        Edge tmpEdge = null;
                        Integer sourceId = 0,weith;
                        Integer[] id = new Integer[2];
                        boolean flag1 = true, flag2 = true;;
                        System.out.println(fileContent.split("\n").length);
                        for(String line: fileContent.split("\n")){
                            System.out.println("line: " + line);
                            if(line.split(" ").length == 1) {

                                try{
                                    sourceId = Integer.parseInt(line.replaceAll("\\D", ""));
                                } catch (NumberFormatException e){
                                    System.out.println("Sourse Exeption");
                                }
                            }
                            else{

                                try{
                                    for(int i = 0; i < 2; i++){
                                        id[i] = Integer.parseInt(line.split(" ")[i].replaceAll("\\D", ""));
                                    }
                                    weith = Integer.parseInt(line.split(" ")[2].replaceAll("\\D", ""));
                                    tmpNode1 = new Node(id[0]);
                                    tmpNode2 = new Node(id[1]);
                                    for(Node node : graph.getNodes()){
                                        if(tmpNode1.getId() == node.getId()){
                                            tmpNode1 = node;
                                            flag1 = false;
                                        }
                                        if(tmpNode2.getId() == node.getId()) {
                                            tmpNode2 = node;
                                            flag2 = false;
                                        }
                                    }
                                    if(flag1){
                                        graph.addNode(tmpNode1, tmpNode1.getId());
                                        System.out.println("Add Node: " + tmpNode1.nodeToString());
                                    } else flag1 = true;
                                    if(flag2){
                                        graph.addNode(tmpNode2, tmpNode2.getId());
                                        System.out.println("Add Node: " + tmpNode2.nodeToString());
                                    } else flag2 = true;
                                    tmpEdge = new Edge(tmpNode1, tmpNode2);
                                    tmpEdge.setWeight(weith);
                                    graph.addEdge(tmpEdge);
                                    tmpEdge = null;
                                    tmpNode1 = null;
                                    tmpNode2 = null;
                                } catch (NumberFormatException e){
                                    System.out.println("Sourse Exeption");
                                }
                            }
                        }
                        for(Node node : graph.getNodes()) {
                            if (node.getId() == sourceId) graph.setSource(node);
                        }
                        Integer X = 4200, Y = 40, sizE = 0;
                        Integer x = X, y = Y;
                        while(sizE * sizE < graph.getNodes().size()){
                            sizE++;
                        }
                        for(Node node : graph.getNodes()){
                            if(x > X + 70 *(sizE - 1)){
                                x = X;
                                y += 70;
                            }
                            node.setCoord(x, y);
                            x += 70;
                            System.out.println(node.nodeToString());
                        }
                        graph.stepRealisation = 0;
                        graph.setSolved(false);
                        graphPanel.uResetGraphPanel(graph);
                        repaint();
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null, "You didn't upload file");
                    fileChooser.closeWind(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupIcon(JButton button, String img){
        try {
            Image icon = ImageIO.read(getClass().getResource(
                    "/resources/" + img + ".png"));
            ImageIcon imageIcon = new ImageIcon(icon);
            button.setIcon(imageIcon);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
