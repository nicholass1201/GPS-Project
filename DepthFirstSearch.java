import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Arrays;

public class DepthFirstSearch {
	int verticies;
	int edges;
	int source;
	int destin;
	int pathCounter;
	boolean flipped;
	ArrayList<ArrayList<Integer>> paths;
	ArrayList<LinkedList<Integer>> adjacency;

	public DepthFirstSearch(int verticies, int edges, int source, int destin,
			ArrayList<LinkedList<Integer>> adjacency) {
		this.verticies = verticies;
		this.edges = edges;
		this.source = source;
		this.destin = destin;
		this.adjacency = adjacency;
		this.pathCounter = 0;
		this.flipped = false;
		paths = new ArrayList<>();
	}

	public void dfs() {
		Stack<Integer> stack = new Stack<>();
		boolean visited[] = new boolean[verticies + 1];
		int[][] adjlist = new int[verticies + 1][verticies + 1];

		for (int i = 0; i < verticies + 1; i++) {
			for (int j = 0; j < verticies + 1; j++) {
				adjlist[i][j] = 0;
			}
		}

		for (int i = 0; i < adjacency.size(); i++) {
			for (int j = 0; j < adjacency.get(i).size(); j++) {
				adjlist[i][adjacency.get(i).get(j)] = 1;
			}
		}

		if (source > destin) {
			int temp = source;
			source = destin;
			destin = temp;
			flipped = true;
		}

		dfsHelper(source, destin, stack, adjlist, visited);
	}

	public void dfsHelper(int source, int destin, Stack<Integer> stack, int[][] adjArray, boolean[] visited) {
		stack.push(source);
		visited[source] = true;

		if (source == destin) {
			paths.add(new ArrayList<>());
			for (int i = 0; i < stack.size(); i++) {
				paths.get(pathCounter).add(stack.elementAt(i));
			}
			pathCounter++;
		}

		for (int i = 1; i <= adjArray.length - 1; i++) {
			if (adjArray[source][i] == 1 && !visited[i]) {
				dfsHelper(i, destin, stack, adjArray, visited);
				visited[i] = false;
			}
		}

		stack.pop();
	}

	int getWeight(String[] dataLines, String v1, String v2, boolean tc, ArrayList<String> verticiesList) {
		for (String dataLine : dataLines) {
			int barCount = 0;
			String line = "";
			String city1 = "'";
			String city2 = "";
			int time = 0;
			int cost = 0;
			for (int j = 0; j < dataLine.length(); j++) {
				if (dataLine.charAt(j) == '|' && barCount == 0) {
					city1 = line;
					barCount++;
					line = "";
				} else if (dataLine.charAt(j) == '|' && barCount == 1) {
					city2 = line;
					barCount++;
					line = "";
				} else if (dataLine.charAt(j) == '|' && barCount == 2) {
					cost = Integer.parseInt(line);
					barCount++;
					line = "";
				} else {
					line += dataLine.charAt(j);
				}
				if (j == dataLine.length() - 1) {
					time = Integer.parseInt(line);
					for (ArrayList<Integer> path : paths) {
						for (int l = 0; l < path.size() - 1; l++) {
							if ((v1.equals(city1) && v2.equals(city2)) || (v2.equals(city1) && v1.equals(city2))) {
								if (tc) {
									return time;
								} else {
									return cost;
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	String getPath(String[] dataLines, ArrayList<String> verticiesList, boolean tc) {
		StringBuilder returnString = new StringBuilder();
		int[] costs = new int[paths.size()];
		int[] times = new int[paths.size()];
		String[] pathArray = new String[paths.size()];
		for (int i = 0; i < pathArray.length; i++) {
			pathArray[i] = "";
		}
		if (!flipped && tc) {
			for (int i = 0; i < paths.size(); i++) {
				for (int j = 0; j < paths.get(i).size(); j++) {
					if (j + 1 < paths.get(i).size()) {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					} else {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++) {
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (times.length > 3) {
				Arrays.sort(times);
				int time1 = times[0];
				int time2 = times[1];
				int time3 = times[2];
				int pathNum = 1;
				for (String s : pathArray) {
					for (int j = 1; j < s.length(); j++) {
						if (s.charAt(j - 1) == 'e' && s.charAt(j) == ':') {
							int num = 2;
							StringBuilder time = new StringBuilder();
							while (Character.isDigit(s.charAt(j + num))) {
								time.append(s.charAt(j + num));
								num++;
							}
							if (Integer.parseInt(time.toString()) == time1 || Integer.parseInt(time.toString()) == time2
									|| Integer.parseInt(time.toString()) == time3) {
								returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
								pathNum++;
								break;
							}
						}
					}
				}
			} else {
				int pathNum = 1;
				for (String s : pathArray) {
					returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
					pathNum++;
				}
			}
		} else if (!flipped && !tc) {
			for (int i = 0; i < paths.size(); i++) {
				for (int j = 0; j < paths.get(i).size(); j++) {
					if (j + 1 < paths.get(i).size()) {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					} else {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++) {
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (costs.length > 3) {
				Arrays.sort(costs);
				int cost1 = costs[0];
				int cost2 = costs[1];
				int cost3 = costs[2];
				int pathNum = 1;
				for (String s : pathArray) {
					for (int j = 1; j < s.length(); j++) {
						if (s.charAt(j - 1) == 't' && s.charAt(j) == ':') {
							int num = 2;
							StringBuilder cost = new StringBuilder();
							while (Character.isDigit(s.charAt(j + num))) {
								cost.append(s.charAt(j + num));
								num++;
							}
							if (Integer.parseInt(cost.toString()) == cost1 || Integer.parseInt(cost.toString()) == cost2
									|| Integer.parseInt(cost.toString()) == cost3) {
								returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
								pathNum++;
								break;
							}
						}
					}
				}
			} else {
				int pathNum = 1;
				for (String s : pathArray) {
					returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
				}
			}
		} else if (flipped && tc) {
			for (int i = 0; i < paths.size(); i++) {
				for (int j = paths.get(i).size() - 1; j >= 0; j--) {
					if (j != 0) {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					} else {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++) {
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (times.length > 3) {
				Arrays.sort(times);
				int time1 = times[0];
				int time2 = times[1];
				int time3 = times[2];
				int pathNum = 1;
				for (String s : pathArray) {
					for (int j = 1; j < s.length(); j++) {
						if (s.charAt(j - 1) == 'e' && s.charAt(j) == ':') {
							int num = 2;
							StringBuilder time = new StringBuilder();
							while (Character.isDigit(s.charAt(j + num))) {
								time.append(s.charAt(j + num));
								num++;
							}
							if (Integer.parseInt(time.toString()) == time1 || Integer.parseInt(time.toString()) == time2
									|| Integer.parseInt(time.toString()) == time3) {
								returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
								pathNum++;
								break;
							}
						}
					}
				}
			} else {
				int pathNum = 1;
				for (String s : pathArray) {
					returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
					pathNum++;
				}
			}
		} else if (flipped && !tc) {
			for (int i = 0; i < paths.size(); i++) {
				for (int j = paths.get(i).size() - 1; j >= 0; j--) {
					if (j != 0) {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					} else {
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++) {
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)),
							verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (costs.length > 3) {
				Arrays.sort(costs);
				int cost1 = costs[0];
				int cost2 = costs[1];
				int cost3 = costs[2];
				int pathNum = 1;
				for (String s : pathArray) {
					for (int j = 1; j < s.length(); j++) {
						if (s.charAt(j - 1) == 't' && s.charAt(j) == ':') {
							int num = 2;
							StringBuilder cost = new StringBuilder();
							while (Character.isDigit(s.charAt(j + num))) {
								cost.append(s.charAt(j + num));
								num++;
							}
							if (Integer.parseInt(cost.toString()) == cost1 || Integer.parseInt(cost.toString()) == cost2
									|| Integer.parseInt(cost.toString()) == cost3) {
								returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
								pathNum++;
								break;
							}
						}
					}
				}
			} else {
				int pathNum = 1;
				for (String s : pathArray) {
					returnString.append("Path ").append(pathNum).append(": ").append(s).append("\n");
				}
			}
		}
		return returnString.toString();
	}
}
