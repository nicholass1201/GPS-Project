// NICHOLAS SUNG CS 3345.501
// THIS IS THE MAIN FILE
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class flightPlan
{
	public static void main(String[] args) throws FileNotFoundException
	{
		int numLine = 0; // number of lines integer
		int barCount = 0; // number of bars integer used when iterating through each lines
		int edges = 0; // number of edges integer
		Scanner console = new Scanner(System.in); // console to read the flight data file
		ArrayList<LinkedList<Integer>> adjacency = new ArrayList<LinkedList<Integer>>(); // adjacency list implemented as LinkedList of ArrayList
		ArrayList<String> verticies = new ArrayList<>(); // array list of verticies
		String dataFile = args[0]; // reads the dataFile input from the command line
		String inputFile = args[1]; // reads the inputFile input from the command line
		String outputFile = args[2]; // reads the outputFile input from the comand line
		String outputString = ""; // string that will be outputed in the end
		String[] dataLines; // string array to hold data file lines
		String[] inputLines; // string array to hold input file lines
		boolean tc = true; // true looking for time false if looking for cost
		Scanner fileScanner;
		File file = new File(dataFile);
		fileScanner = new Scanner(file); // fileScanner will be used to read the file
		numLine = Integer.parseInt(fileScanner.nextLine()); // total number of lines to read
		dataLines = new String[numLine]; // dataLines with numLine size
		
		for (int i = 0; i < numLine; i++)
		{
			dataLines[i] = fileScanner.nextLine(); // fills dataLines with elements of the file
		}
		
		fileScanner.close(); // closes the file scanner
		
		for (int i = 0; i < dataLines.length; i++) // loops through dataLines and if a city that wasn't found before is found, adds it to verticies and adds an empty linkedlist to adjanceny
		{
			String line = "";
			barCount = 0;
			for (int j = 0; j < dataLines[i].length(); j++)
			{
				if (dataLines[i].charAt(j) != '|')
				{
					line += dataLines[i].charAt(j);
				}
				else if (barCount < 2)
				{
					barCount++;
					if (!verticies.contains(line))
					{
						verticies.add(line);
						adjacency.add(new LinkedList<Integer>());
					}
					line = "";
				}
			}
		}
		for (int i = 0; i < dataLines.length; i++) // loops through dataLines once more and fills the arraylist of linkedlist. Index 0 will be the first city found, 1 will be the second city found,  2 will be the third city and vice versa
		{
			String line = "";
			String tempCity = "'";
			barCount = 0;
			for (int j = 0; j < dataLines[i].length(); j++)
			{
				if (dataLines[i].charAt(j) != '|')
				{
					line += dataLines[i].charAt(j);
				}
				else if (barCount == 0)
				{
					barCount++;
					tempCity = line;
					line = "";
				}
				else if (barCount == 1)
				{
					barCount++;
					int source = 0;
					int destin = 0;
					for (int k = 0; k < verticies.size(); k++)
					{
						if (line.equals(verticies.get(k)))
							source = k;
					}
					for (int l = 0; l < verticies.size(); l++)
					{
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
		for (int i = 0; i < numLine; i++)
		{
			inputLines[i] = fileScanner.nextLine();
		}
		for (int i = 0; i < inputLines.length; i++) // the for loop will find the source and destination index. In addition it will determine whether time or cost is being tested. If test, tc will be set to true
		{
			String line = "";
			int sourceI = 0;
			int destI = 0;
			barCount = 0;
			for (int j = 0; j < inputLines[i].length(); j++)
			{
				if (inputLines[i].charAt(j) != '|')
				{
					line += inputLines[i].charAt(j);
				}
				else if (barCount == 0)
				{
					for (int k = 0; k < verticies.size(); k++)
					{
						if (verticies.get(k).equals(line))
						{
							sourceI = k;
						}		
					}
					barCount++;
					line = "";
				}
				else if (barCount == 1)
				{
					for (int k = 0; k < verticies.size(); k++)
					{
						if (verticies.get(k).equals(line))
						{
							destI = k;
						}		
					}
					if (inputLines[i].charAt(inputLines[i].length() - 1) == 'T')
					{
						tc = true;
					}
					else
					{
						tc = false;
					}
				}
			}
			// adds elements to the outputed string
			outputString += "Flight " + (i+1) + ": " + verticies.get(sourceI) + ", " + verticies.get(destI);
			if (tc == true)
			{
				outputString += " (Time)\n";
			}
			else
			{
				outputString += " (Cost)\n";
			}
			// creates an object of DepthFirstSearch with number of verticies, edges, source index, destination index, and the adjacnecy list
			DepthFirstSearch depth = new DepthFirstSearch(verticies.size(), edges, sourceI, destI, adjacency);
			depth.dfs(); // performs depth first search
			outputString += depth.getPath(dataLines, verticies, tc); // adds the paths to the output string
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)); // creates a new output file
			writer.write(outputString); // adds outputted string to the file
			writer.close(); // closes the file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
