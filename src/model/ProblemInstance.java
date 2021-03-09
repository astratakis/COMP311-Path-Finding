package model;

import java.util.*;
import java.util.regex.*;

public class ProblemInstance {
	
	public ProblemInstance(Scanner scanner) throws IllegalArgumentException {
		Pattern p = Pattern.compile("(<.+>)(.*)(</.+>)");
		Matcher m = p.matcher(scanner.next());
				
		if (m.matches())
			source = m.group(2);
		else
			throw new IllegalArgumentException("Input file has wrong format...");
				
		m = p.matcher(scanner.next());
		
		if (m.matches())
			destination = m.group(2);
		else
			throw new IllegalArgumentException("Input file has wrong format...");
		
		scanner.next();			// Consume the next String
		scanner.nextLine();		// Consume the next /n char
		
		String line = scanner.nextLine();
		
		nodes = new HashMap<String, Node>();
		roads = new HashMap<String, Road>();
		
		while (!line.contentEquals("</Roads>")) {
			
			String[] arr = line.split("; ");
			
			if (!nodes.containsKey(arr[1])) {
				nodes.put(arr[1], new Node(arr[1]));
			}
			if (!nodes.containsKey(arr[2])) {
				nodes.put(arr[2], new Node(arr[2]));
			}
			
			roads.put(arr[0], new Road(arr[1], nodes.get(arr[1]), nodes.get(arr[2]), Integer.parseInt(arr[3])));
			line = scanner.nextLine();
		}
	}
	
	public final String source;
	public final String destination;
	Map<String, Road> roads;
	Map<String, Node> nodes;
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("======== < DESCRIPTION > ========\n\n");
		buffer.append("Source: " + source + "\n");
		buffer.append("Destination: " + destination + "\n");
		buffer.append("\n=================================\n\n");
		
		for (Road r : roads.values()) {
			buffer.append(r + "\n");
		}
		return buffer.toString();
	}
	
	public void simulate() {
		
	}
}
