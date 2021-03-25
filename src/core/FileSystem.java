package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.*;

public class FileSystem {
	
	private Scanner scanner;
	private ProblemInstance pi;
	private String source;
	private String destination;
	
	public FileSystem(Scanner scanner, ProblemInstance pi) {
		this.scanner = scanner;
		this.pi = pi;
		initializeRideInfo();
		initializeGraph();
		
		int ID = 0;
		while (true) {
			try {
				Day d = initializeDayPredictions(ID++);
				pi.getDays().add(d);
				
			}
			catch (Exception e) {
				break;
			}
		}
		
		initializeDayActuals();
		
		pi.setSource(pi.getGraph().getNodes().get(this.source));
		pi.setDestination(pi.getGraph().getNodes().get(this.destination));
	}
	
	
	private void initializeRideInfo() {
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
	}
	
	
	
	private void initializeGraph() {
		String line = scanner.nextLine();
		
		Map<String,Node> nodes = pi.getGraph().getNodes();
		Map<String,Road> roads = pi.getGraph().getRoads();
		Map<HashSet<Node>, ArrayList<Road>> roadNodes = pi.getGraph().getRoadNodes();
		
		while (!line.contentEquals("</Roads>")) {
			
			HashSet<Node> nodesHashSet = new HashSet<Node>();
			
			String[] arr = line.split("; ");
			
			if (!nodes.containsKey(arr[1])) {
				nodes.put(arr[1], new Node(arr[1]));
			}
			if (!nodes.containsKey(arr[2])) {
				nodes.put(arr[2], new Node(arr[2]));
			}
			
			Road r = new Road(arr[0], nodes.get(arr[1]), nodes.get(arr[2]), Integer.parseInt(arr[3]));
			
			roads.put(arr[0], r);
			
			Node start = nodes.get(arr[1]);
			Node end = nodes.get(arr[2]);
			
			nodesHashSet.add(start);
			nodesHashSet.add(end);
			
			if (!roadNodes.containsKey(nodesHashSet)) {
				roadNodes.put(new HashSet<Node>(nodesHashSet), new ArrayList<Road>());
				roadNodes.get(nodesHashSet).add(r);
			}
			else {
				roadNodes.get(nodesHashSet).add(r);
			}
								
			start.getNeighbors().put(roads.get(arr[0]),end);
			end.getNeighbors().put(roads.get(arr[0]),start);
			
			start.getAdjacencies().add(end);
			end.getAdjacencies().add(start);
			
			start.getRoads().add(r);
			end.getRoads().add(r);
			
			nodesHashSet.clear();
			line = scanner.nextLine();
		}
		
		scanner.nextLine();
	}
	
	
	private Day initializeDayPredictions(int ID) {
		String line = scanner.nextLine();	// Consume <Day>

		Day d = new Day(ID);
		
		while (true) {
			
			line = scanner.nextLine();

			if (line.contentEquals("</Day>")) {
				break;
			}

			String[] arr = line.split(";");
			Road r = pi.getGraph().getRoads().get(arr[0]);
			
			Double virtualWeight  = 0.0;
			
			switch (arr[1].trim()) {
			
			case "low":
				// -10%
				virtualWeight = r.getWeight() - 0.1 * r.getWeight();
				break;
			case "normal":
				virtualWeight = 1.0 * r.getWeight();
				break;
			case "heavy":
				// +25%
				virtualWeight = r.getWeight() + 0.25 * r.getWeight();
				break;
			default:
				throw new Error("Should not reach this...");
			}
			
			d.getPredictions().put(r, virtualWeight);
		}
		
		return d;
	}
	
	private void initializeDayActuals() {
		
		Vector<Day> days = pi.getDays();
		
		
		for (int i=0; i<days.size(); i++) {
			
			String line = scanner.nextLine(); // Consume <Day>
			
			while (true) {
				line = scanner.nextLine();
				
				if (line.contentEquals("</Day>")) {
					break;
				}
				
				String[] arr = line.split(";");
				
				Road r = pi.getGraph().getRoads().get(arr[0]);
				
				Double virtualWeight  = 0.0;
				
				switch (arr[1].trim()) {
				
				case "low":
					// -10%
					virtualWeight = r.getWeight() - 0.1 * r.getWeight();
					break;
				case "normal":
					virtualWeight = 1.0 * r.getWeight();
					break;
				case "heavy":
					// +25%
					virtualWeight = r.getWeight() + 0.25 * r.getWeight();
					break;
				default:
					throw new Error("Should not reach this...");
				}
				
				days.get(i).getActual().put(r, virtualWeight);
			}
		}
	}


}
