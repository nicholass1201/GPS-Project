import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class flightPlan {
    public static void main(String[] args) throws FileNotFoundException {
        int numLine;
        int barCount;
        int edges = 0;
        Scanner console = new Scanner(System.in);
        ArrayList<LinkedList<Integer>> adjacency = new ArrayList<>();
        ArrayList<String> verticies = new ArrayList<>();
        String dataFile = args[0];
        String inputFile = args[1];
        String[] dataLines;
        String[] inputLines;
        boolean tc = true;
        Scanner fileScanner;
        File file = new File(dataFile);
        fileScanner = new Scanner(file);
        numLine = Integer.parseInt(fileScanner.nextLine());
        dataLines = new String[numLine];

        for (int i = 0; i < numLine; i++) {
            dataLines[i] = fileScanner.nextLine();
        }

        fileScanner.close();

        for (String dataLine : dataLines) {
            String line = "";
            barCount = 0;
            for (int j = 0; j < dataLine.length(); j++) {
                if (dataLine.charAt(j) != '|') {
                    line += dataLine.charAt(j);
                } else if (barCount < 2) {
                    barCount++;
                    if (!verticies.contains(line)) {
                        verticies.add(line);
                        adjacency.add(new LinkedList<>());
                    }
                    line = "";
                }
            }
        }
        for (String dataLine : dataLines) {
            String line = "";
            String tempCity = "'";
            barCount = 0;
            for (int j = 0; j < dataLine.length(); j++) {
                if (dataLine.charAt(j) != '|') {
                    line += dataLine.charAt(j);
                } else if (barCount == 0) {
                    barCount++;
                    tempCity = line;
                    line = "";
                } else if (barCount == 1) {
                    barCount++;
                    int source = 0;
                    int destin = 0;
                    for (int k = 0; k < verticies.size(); k++) {
                        if (line.equals(verticies.get(k)))
                            source = k;
                    }
                    for (int l = 0; l < verticies.size(); l++) {
                        if (tempCity.equals(verticies.get(l)))
                            destin = l;
                    }
                    adjacency.get(source).add(destin);
                    adjacency.get(destin).add(source);
                    edges++;
                }
            }
        }

        file = new File(inputFile); // reads from the second file now
        fileScanner = new Scanner(file);
        numLine = Integer.parseInt(fileScanner.nextLine());
        inputLines = new String[numLine];
        for (int i = 0; i < numLine; i++) {
            inputLines[i] = fileScanner.nextLine();
        }

        for (String inputLine : inputLines) {
            String line = "";
            int sourceI = 0;
            int destI = 0;
            barCount = 0;
            for (int j = 0; j < inputLine.length(); j++) {
                if (inputLine.charAt(j) != '|') {
                    line += inputLine.charAt(j);
                } else if (barCount == 0) {
                    for (int k = 0; k < verticies.size(); k++) {
                        if (verticies.get(k).equals(line)) {
                            sourceI = k;
                        }
                    }
                    barCount++;
                    line = "";
                } else if (barCount == 1) {
                    for (int k = 0; k < verticies.size(); k++) {
                        if (verticies.get(k).equals(line)) {
                            destI = k;
                        }
                    }
                    if (inputLine.charAt(inputLine.length() - 1) == 'T') {
                        tc = true;
                    } else {
                        tc = false;
                    }
                }
            }

            DepthFirstSearch depth = new DepthFirstSearch(verticies.size(), edges, sourceI, destI, adjacency);
            depth.dfs();

            GraphVisualizer visualizer = new GraphVisualizer(adjacency, verticies, depth.paths);
            SwingUtilities.invokeLater(() -> visualizer.setVisible(true));

            while (visualizer.isVisible()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
