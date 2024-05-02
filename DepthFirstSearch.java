import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class DepthFirstSearch {
	int verticies; // number of verticies
	int edges; // number of edges
	int source; // source index
	int destin; // destination index
	int pathCounter; // number of path
	boolean flipped; // boolean used to inverse to path. Explanation: if destination is higher than source, then the code won't function properly so adde this
	ArrayList<ArrayList<Integer>> paths; // arraylist of ararylist containing all the paths.
	ArrayList<LinkedList<Integer>> adjacency; // linkedlist of arraylist containting the adjacenecy list
	public DepthFirstSearch(int verticies, int edges, int source, int destin, ArrayList<LinkedList<Integer>> adjacency) // constructor
	{
		this.verticies = verticies;
		this.edges = edges;
		this.source = source;
		this.destin = destin;
		this.adjacency = adjacency;
		this.pathCounter = 0;
		this.flipped = false;
		paths = new ArrayList<ArrayList<Integer>>();
	}
	public void dfs() { // void method to setup the depth fist search
		Stack<Integer> stack = new Stack<>();
		
		boolean visited[] = new boolean[verticies + 1]; // boolean array with size of verticies + 1
		int[][] adjlist = new int[verticies+ 1][verticies + 1]; // 2D arraylist of adjacnecy list with both dimensions size greater than verticies + 1
		
		for (int i = 0; i < verticies + 1; i++)
		{
			for (int j = 0; j < verticies + 1; j++) // sets all elements of the adjlist to be 0
			{
				adjlist[i][j] = 0;
			}
		}
		
		for (int i = 0; i < adjacency.size(); i++) // sets 1 to elements at adjlist that have the edges traversing from vertex i to vertex adjacency.get(i).get(j)
		{
			for (int j = 0; j < adjacency.get(i).size(); j++)
			{
				adjlist[i][adjacency.get(i).get(j)] = 1;
			}
		}
		
		if (source > destin) // reverses source and destin
		{
			int temp = source;
			source = destin;
			destin = temp;
			flipped = true;
		}
		
		dfsHelper(source, destin, stack, adjlist, visited); // calls the recursive method dfsHelper
	}

	public void dfsHelper(int source, int destin, Stack<Integer> stack, int[][] adjArray, boolean[] visited) // recursive method that performs the depth first search
	{
		stack.push(source); // stores the current verticies into the stack
		visited[source] = true; // marks the current vertex as true
		
		if(source == destin) // if reached the destination vertex
		{
			paths.add(new ArrayList<Integer>()); // creates a new path list
			for(int i = 0; i < stack.size(); i++)
			{
				paths.get(pathCounter).add(stack.elementAt(i)); // adds the stack elements to the current path
			}
			pathCounter++; // increments the pathCounter
		}

		for(int i = 1; i <= adjArray.length-1; i++) // loops through the adjArray
		{
			if(adjArray[source][i] == 1 && !visited[i]) // if the verticies are connected and it isn't visited 
			{
				dfsHelper(i, destin, stack, adjArray, visited); // recursively calls the dfsHelper method
				// After the search, sets the vertex as false so that it is possible to find it in different path
				visited[i] = false; 
			}
		}
		
		stack.pop(); // pops the stack to exit
	}
	
	int getWeight(String[] dataLines, String v1, String v2, boolean tc, ArrayList<String> verticiesList) // returns the weight of the edge from v1 to v2
	{
		for (int i = 0; i < dataLines.length; i++) // returns time if tc is true, cost if tc is false, and 0 if there was an issue
		{
			int barCount = 0;
			String line = "";
			String city1 = "'";
			String city2 = "";
			int time = 0;
			int cost = 0;
			for (int j = 0; j < dataLines[i].length(); j++)
			{
				if (dataLines[i].charAt(j) == '|' && barCount == 0)
				{
					city1 = line;
					barCount++;
					line = "";
				}
				else if (dataLines[i].charAt(j) == '|' && barCount == 1)
				{
					city2 = line;
					barCount++;
					line = "";
				}
				else if (dataLines[i].charAt(j) == '|' && barCount == 2)
				{
					cost = Integer.parseInt(line);
					barCount++;
					line = "";
				}
				else
				{
					line += dataLines[i].charAt(j);
				}
				if (j == dataLines[i].length() - 1)
				{
					time = Integer.parseInt(line);
					for (int k = 0; k < paths.size(); k++)
					{
						for (int l = 0; l < paths.get(k).size() - 1; l++)
						{
							if ((v1.equals(city1) && v2.equals(city2)) || (v2.equals(city1) && v1.equals(city2)) && tc == true)
								return time;
							else if ((v1.equals(city1) && v2.equals(city2)) || (v2.equals(city1) && v1.equals(city2)) && tc == false)
								return cost;
						}
					}
				}
			}
		}
		return 0;
	}
	
	String getPath(String[] dataLines, ArrayList<String> verticiesList, boolean tc) // returns the up to 3 paths
	{
		String returnString = ""; // string to return
		int[] costs = new int[paths.size()]; // costs of the path
		int[] times = new int[paths.size()]; // times of the path
		String[] pathArray = new String[paths.size()]; // array to store the paths
		for (int i = 0; i < pathArray.length; i++) // sets all elements of the pathArray to ""
		{
			pathArray[i] = "";
		}
		if (flipped == false && tc == true) // if the source and destin wasn't flipped and time is being asked to find,
		{
			for (int i = 0; i < paths.size(); i++)
			{
				for (int j = 0; j < paths.get(i).size(); j++) // adds the path to pathArray
				{
					if (j + 1 < paths.get(i).size())
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					}
					else
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++) // finds the total time and cost of the paths
				{
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i]; // changes cost to double
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost; // adds time and cost to the pathArray
			}
			if (times.length > 3) // called if more than 3 paths are present
			{
				Arrays.sort(times);
				int time1 = times[0];
				int time2 = times[1];
				int time3 = times[2];
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++) // adds the 3 best paths to returnString
				{
					for (int j = 1; j < pathArray[i].length(); j++)
					{
						if (pathArray[i].charAt(j-1) == 'e' && pathArray[i].charAt(j) == ':')
						{
							int num = 2;
							String time = "";
							while (Character.isDigit(pathArray[i].charAt(j+num)))
							{
								time += Character.toString(pathArray[i].charAt(j+num));
								num++;
							}
							if (Integer.parseInt(time) == time1 || Integer.parseInt(time) == time2 || Integer.parseInt(time) == time3)
							{
								returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
								pathNum++;
								break;
							}
						}
					}
				}
			}
			else // adds all the paths to returnString if less than 3 or less paths are present
			{
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
					pathNum++;
				}
			}
		}
		// *** the else if statements below are all the same as the if statement above with little adjustment to match the given tc or flipped boolean variables
		else if (flipped == false && tc == false)
		{
			for (int i = 0; i < paths.size(); i++)
			{
				for (int j = 0; j < paths.get(i).size(); j++)
				{
					if (j + 1 < paths.get(i).size())
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					}
					else
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++)
				{
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (costs.length > 3)
			{
				Arrays.sort(costs);
				int cost1 = costs[0];
				int cost2 = costs[1];
				int cost3 = costs[2];
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					for (int j = 1; j < pathArray[i].length(); j++)
					{
						if (pathArray[i].charAt(j-1) == 't' && pathArray[i].charAt(j) == ':')
						{
							int num = 2;
							String cost = "";
							while (Character.isDigit(pathArray[i].charAt(j+num)))
							{
								cost += Character.toString(pathArray[i].charAt(j+num));
								num++;
							}
							if (Integer.parseInt(cost) == cost1 || Integer.parseInt(cost) == cost2 || Integer.parseInt(cost) == cost3)
							{
								returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
								pathNum++;
								break;
							}
						}
					}
				}
			}
			else
			{
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
				}
			}
		}
		else if(flipped == true && tc == true)
		{
			for (int i = 0; i < paths.size(); i++)
			{
				for (int j = paths.get(i).size() - 1; j >= 0; j--)
				{
					if (j != 0 )
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					}
					else
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++)
				{
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (times.length > 3)
			{
				Arrays.sort(times);
				int time1 = times[0];
				int time2 = times[1];
				int time3 = times[2];
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					for (int j = 1; j < pathArray[i].length(); j++)
					{
						if (pathArray[i].charAt(j-1) == 'e' && pathArray[i].charAt(j) == ':')
						{
							int num = 2;
							String time = "";
							while (Character.isDigit(pathArray[i].charAt(j+num)))
							{
								time += Character.toString(pathArray[i].charAt(j+num));
								num++;
							}
							if (Integer.parseInt(time) == time1 || Integer.parseInt(time) == time2 || Integer.parseInt(time) == time3)
							{
								returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
								pathNum++;
								break;
							}
						}
					}
				}
			}
			else
			{
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
					pathNum++;
				}
			}
		}
		else if(flipped == true && tc == false)
		{
			for (int i = 0; i < paths.size(); i++)
			{
				for (int j = paths.get(i).size() - 1; j >= 0; j--)
				{
					if (j != 0 )
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + " -> ";
					}
					else
					{
						pathArray[i] += verticiesList.get(paths.get(i).get(j)) + ". ";
					}
				}
				for (int j = 1; j < paths.get(i).size(); j++)
				{
					times[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), true, verticiesList);
					costs[i] += getWeight(dataLines, verticiesList.get(paths.get(i).get(j)), verticiesList.get(paths.get(i).get(j - 1)), false, verticiesList);
				}
				double dCost = costs[i];
				pathArray[i] += "Time: " + times[i] + " Cost: " + dCost;
			}
			if (costs.length > 3)
			{
				Arrays.sort(costs);
				int cost1 = costs[0];
				int cost2 = costs[1];
				int cost3 = costs[2];
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					for (int j = 1; j < pathArray[i].length(); j++)
					{
						if (pathArray[i].charAt(j-1) == 't' && pathArray[i].charAt(j) == ':')
						{
							int num = 2;
							String cost = "";
							while (Character.isDigit(pathArray[i].charAt(j+num)))
							{
								cost += Character.toString(pathArray[i].charAt(j+num));
								num++;
							}
							if (Integer.parseInt(cost) == cost1 || Integer.parseInt(cost) == cost2 || Integer.parseInt(cost) == cost3)
							{
								returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
								pathNum++;
								break;
							}
						}
					}
				}
			}
			else
			{
				int pathNum = 1;
				for (int i = 0; i < pathArray.length; i++)
				{
					returnString += "Path "+ pathNum + ": " + pathArray[i] + "\n";
				}
			}
		}
		// returns returnString
		return returnString;
	}
}