import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class GraphVisualizer extends JFrame {
    private final ArrayList<LinkedList<Integer>> adjacency;
    private final ArrayList<String> vertices;
    private final ArrayList<ArrayList<Integer>> paths;

    public GraphVisualizer(ArrayList<LinkedList<Integer>> adjacency, ArrayList<String> vertices, ArrayList<ArrayList<Integer>> paths) {
        this.adjacency = adjacency;
        this.vertices = vertices;
        this.paths = paths;
        setTitle("Graph Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int radius = 200;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int vertexCount = vertices.size();
        Point[] points = new Point[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            double angle = 2 * Math.PI * i / vertexCount;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            points[i] = new Point(x, y);
            g.fillOval(x - 5, y - 5, 10, 10);
            g.drawString(vertices.get(i), x - 15, y - 15);
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < adjacency.size(); i++) {
            for (int j : adjacency.get(i)) {
                g.drawLine(points[i].x, points[i].y, points[j].x, points[j].y);
            }
        }

        g.setColor(Color.RED);
        for (ArrayList<Integer> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                g.drawLine(points[path.get(i)].x, points[path.get(i)].y, points[path.get(i + 1)].x, points[path.get(i + 1)].y);
            }
        }
    }
}
