package gui;

import algo.DijkstraAlgorithm;
import models.Graph;
import models.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JPanel {

    private Node node;
    private int state;
    private Graph graph;
    public DijkstraAlgorithm dijkstraAlgorithm;
    private GraphPanel graphPanel;

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
        JLabel info = new JLabel("Dijkstra Shortest Path Visualiser by iamareebjamal");
        info.setForeground(new Color(230, 220, 250));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(130, 50, 250));
        panel.add(info);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panel, BorderLayout.NORTH);
    }

    private void setButtons(){
        JButton run = new JButton();
        setupIcon(run, "run");
        JButton reset = new JButton();
        setupIcon(reset, "reset");
        final JButton info = new JButton();
        setupIcon(info, "info");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DrawUtils.parseColor("#DDDDDD"));
        buttonPanel.add(reset);
        buttonPanel.add(run);
        buttonPanel.add(info);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = 0;
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
                        "Shift + Left Click       :    Set node as source\n" +
                        "Shift + Right Click     :    Set node as destination\n" +
                        "Ctrl  + Drag               :    Reposition Node\n" +
                        "Ctrl  + Click                :    Get Path of Node\n" +
                        "Ctrl  + Shift + Click   :    Delete Node/Edge\n");
            }
        });

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    switch (state){
                        case 0:
                            System.out.println("Algoritm work");
                            graph.stepRealisation = 1;
                            dijkstraAlgorithm = new DijkstraAlgorithm(graph);
                            dijkstraAlgorithm.run();
                            state = 1;
                            node = dijkstraAlgorithm.dLog.getVisitedLog();
                            for(Node nodeE : graph.getNodes()){
                                if(nodeE.getId() == node.getId()){
                                    nodeE.status = 1;
                                }
                            }
 //                           graphPanel.SourceNode(node);
                            repaint();
                            break;
                        case 1:
                            if(dijkstraAlgorithm.dLog.isEmptyVisited()) {
                            state = 2;
                            repaint();
                            graph.stepRealisation = 2;

                            break;
                        }
                            node.status = 2;
                            for(Node nodeE : graph.getNodes()){
                                if(nodeE.getId() == node.getId()){
                                    nodeE.status = 2;
                                }
                            }
                              node = dijkstraAlgorithm.dLog.getVisitedLog();
                            for(Node nodeE : graph.getNodes()){
                                if(nodeE.getId() == node.getId()){
                                    nodeE.status = 1;
                                }
                            }
                            System.out.println(node.nodeToString());
                            repaint();
                            break;
                        case 2:
                            System.out.println("End of work");
                            state = 0;
                            for(Node nodeE : graph.getNodes()){
                                nodeE.status = 0;
                            }
                            dijkstraAlgorithm.clear();
                            graph.stepRealisation = 0;
                            repaint();
                            break;
                    }
                } catch (IllegalStateException ise){
                    JOptionPane.showMessageDialog(null, ise.getMessage());
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
