package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemInstance {
	
	public ProblemInstance(Scanner scanner) throws IllegalArgumentException {
		Pattern p = Pattern.compile("(<.+>)(.*)(</.+>)");
		Matcher m = p.matcher(scanner.next());
		
		String source, destination;
				
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
		
		graph = new Graph(scanner);
		
		this.source = graph.nodes.get(source);
		this.destination = graph.nodes.get(destination);
	}
	
	final Node source;
	final Node destination;
	final Graph graph;
	
	
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("======== < DESCRIPTION > ========\n\n");
		buffer.append("Source: " + source + "\n");
		buffer.append("Destination: " + destination + "\n");
		buffer.append("\n=================================\n\n");
		
		buffer.append(graph);
		return buffer.toString();
	}
	
	public void simulate(Algorithms alg) {
		switch (alg) {
		
		case DIJKSTRA:
			dijkstra(graph, source, destination);
			break;
		
		default: 
			break;
		}
	}
	
	/**
	 * Shortest Path Search Algorithm (Dijkstra)
	 * @param g
	 * @param source
	 */
	public void dijkstra(Graph g, Node source, Node destination) {
		
				
		
		
	}
	
	public void export() throws IOException {
		PrintWriter pr = new PrintWriter(new File("output" + File.separator + "output.txt"));
	}
}
