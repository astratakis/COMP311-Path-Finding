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
		
		days = new ArrayList<Day>();
		
		int dayID = 0;
		
		while (true) {
			
			try {
				Day d = new Day(dayID++, graph, scanner);
				days.add(d);
			}
			catch (Exception e) {
				break;
			}
		}
		
		for (int i=0; i<days.size(); i++) {
			
			String line = scanner.nextLine(); // Consume <Day>
			
			while (true) {
				line = scanner.nextLine();
				
				if (line.contentEquals("</Day>")) {
					break;
				}
				
				String[] arr = line.split(";");
				
				Road r = graph.roads.get(arr[0]);
				
				Double virtualWeight  = 0.0;
				
				switch (arr[1].trim()) {
				
				case "low":
					// -10%
					virtualWeight = r.weight - 0.1 * r.weight;
					break;
				case "normal":
					virtualWeight = 1.0 * r.weight;
					break;
				case "heavy":
					// +25%
					virtualWeight = r.weight + 0.25 * r.weight;
					break;
				default:
					throw new Error("Should not reach this...");
				}
				
				days.get(i).actual.put(arr[0], virtualWeight);
			}
		}				
	}
	
	final Node source;
	final Node destination;
	final Graph graph;
	
	final List<Day> days;
	Map<Node,Road> cheapestRoads;
	
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
	
	public void simulate() {
		
		for (int i=0; i<days.size(); i++) {
			
			findCheapestRoads(days.get(i));

			//System.out.println("Day: " + (i+1));
			
			// Run bfs with predicions ...
			
			//bfs(graph, source, destination, days.get(i));
			
			// Run ida* with predicions ...
			
			//ida(graph, source, destination, days.get(i));
		
		}		
		
		System.out.println(heuristic(source,destination,days.get(0)));
	}
	
	public void findCheapestRoads(Day d) {
		
		cheapestRoads = new HashMap<Node,Road>();
		cheapestRoads.clear();
		
		for(Node n : graph.nodes.values()) {
			Road minRoad = Collections.min(n.neighbors.keySet(), new Comparator<Road>() {

				@Override
				public int compare(Road r1, Road r2) {
					if (d.predictions.get(r1.name) > d.predictions.get(r2.name)) {
						return 1;
					}
					else if (d.predictions.get(r1.name) == d.predictions.get(r2.name)) {
						return 0;
					}
					return -1;
				}
				
			});
			
			cheapestRoads.put(n, minRoad);
		}
		
	}
	
	public Node findCheapestNeighbor(Node e) {
		Node c;
		Road r = cheapestRoads.get(e);
		c = e.neighbors.get(r);
		return c;
	}
	
	public Road findCheapestRoad(ArrayList<Road> e,Day d) {
		
		Road minRoad = Collections.min(e, new Comparator<Road>() {

			@Override
			public int compare(Road r1, Road r2) {
				if (d.predictions.get(r1.name) > d.predictions.get(r2.name)) {
					return 1;
				}
				else if (d.predictions.get(r1.name) == d.predictions.get(r2.name)) {
					return 0;
				}
				return -1;
			}
			
		});
		
		return minRoad;
	}
	
	private LinkedList<Node> bfs(Graph graph, Node source, Node destination, Day d) {
		
		long start = System.nanoTime();
		
		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		LinkedList<Node> spt = new LinkedList<Node>();
		
		int visitedNodes = 0;
		
		path.add(source);
		queue.add(source);
		explored.add(source);
		
		Node curNode;

		while(!queue.isEmpty()) {
			curNode = queue.pop();
			for(Node n: curNode.neighbors.values()) {
				
				if(!explored.contains(n)){
					queue.add(n);
					explored.add(n);
					path.add(n);
					
					if(curNode.equals(destination)) {	
						break;
					}
				}
				
				visitedNodes++;
			}
		}
		
		Node currentSrc = destination;
		spt.add(destination);
		
		while(!path.isEmpty()) {
			curNode = path.get(path.size()-1);
			path.remove(curNode);
			
			if(currentSrc.neighbors.containsValue(curNode)) {
				spt.add(curNode);
				currentSrc = curNode;
				
				if(curNode.equals(source)) {
					break;
				}
			}			
		}
		
		LinkedList<Road> roadPath = findRoadPath(spt, d);
		double totCost = 0.0;
		double realCost = 0.0;
		
		long end = System.nanoTime();
		
		Collections.reverse(roadPath);
		
		for(Road r: roadPath) {
			totCost += d.predictions.get(r.name);
			realCost += d.actual.get(r.name);
		}
						
		System.out.println("<BREADTH FIRST SEARCH>: ");
		System.out.println("\tVisited Nodes Num: " + visitedNodes);
		System.out.println("\tExecution time: " + (end - start < 1_000_000 ? end - start + " ns" : (end - start) / 1_000_000 + " ms"));
		
		StringBuilder buffer = new StringBuilder();
		
		for (int i=0; i<roadPath.size(); i++) {
			buffer.append(roadPath.get(i).name + " ( " + d.predictions.get(roadPath.get(i).name) + " )");
			if (i < roadPath.size() - 1) {
				buffer.append(" -> ");
			}
		}
		
		System.out.println("\tPath: " + buffer.toString());
		System.out.printf("\tPredicted Cost: %.2f\n", totCost);
		System.out.printf("\tReal Cost: %.2f\n\n", realCost);
				
		return spt;
	}
	
	private LinkedList<Road> findRoadPath(LinkedList<Node> nodePath, Day d){
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
		
		for(int i=0; i<nodePath.size(); i++) {
			
			if(nodePath.isEmpty() || i+1 >= nodePath.size()) {
				break;
			}
			
			Node n1 = nodePath.get(i);
			Node n2 = nodePath.get(i+1);
			
			Node[] nodeArray = {n1,n2};
			HashSet<Node> setToSearch = new HashSet<Node>(Arrays.asList(nodeArray));

			Road r = Collections.min(graph.roadNodes.get(setToSearch), new Comparator<Road>() {

				@Override
				public int compare(Road r1, Road r2) {
					if (d.predictions.get(r1.name) > d.predictions.get(r2.name)) {
						return 1;
					}
					else if (d.predictions.get(r1.name) == d.predictions.get(r2.name)) {
						return 0;
					}
					return -1;
				}
				
			});
						
			roadPath.add(r);			
		}
		
		return roadPath;
	}
	
	private static final double FOUND = -1.0;
	
	private LinkedList<Node> ida(Graph graph, Node source, Node destination, Day d) {
		
		double bound = heuristic(source, destination,d);
		
		LinkedList<Node> path = new LinkedList<Node>();
		
		path.add(source);
		
		while (true) {
			
			double type = search(path, 0, bound,d);
			
			if (type == Double.MAX_VALUE) {
				System.out.println("Not found ...");
				return null;
			}
			else if (type == FOUND) {
				//System.out.println("Found");
				//System.out.println(path);
				return path;
			}
			
			bound = type;
		}
	}
	
	private double search(LinkedList<Node> path, double cost, double bound, Day d) {
		Node curNode = path.getLast();
		
		double f = cost + heuristic(curNode, destination,d);
		
		if (f > bound) {
			return f;
		}
		
		if (curNode.equals(destination)) {
			return FOUND;
		}
		
		double min = Double.MAX_VALUE;
		
		for (Node n : curNode.neighbors.values()) {
			if (!path.contains(n)) {
				path.add(n);
				double type = search(path, cost + heuristic(curNode,destination,d), bound, d);
				
				if (type == FOUND) {
					return FOUND;
				}
				else if (type < min) {
					min = type;
				}
				path.pop();
			}
		}
		
		return min;
	}
	
	private double heuristic(Node curNode, Node destination, Day d) {
		
		LinkedList<Node> path = new LinkedList<Node>(); 
		path = ucs(curNode,destination,d);
	
		for(Node n: path) {
			System.out.println(n.name);
		}
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
	    roadPath = findRoadPath(path,d);
		
		
		double totCost = 0.0;
		
		
		for(Road r: roadPath) {
			System.out.println("Road name: "+r.name);
		}
		
		return totCost;
	}
	
	private LinkedList<Node> ucs(Node source,Node destination, Day d){
		
		Node node = source;
		double cost = 0.0;
		
		for(Node n : graph.nodes.values()) {
			n.cost = 0.0;
		}
		
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		Vector<Node> explored = new Vector<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		
		frontier.add(node);
		
		while(true) {
			if(frontier.isEmpty()) {
				break;
			}
			
			node = frontier.removeFirst();
			
			path.add(node);
			
			if(node.equals(destination)) {
				break;
			}
			
			explored.add(node);
			
			for(Road r : node.neighbors.keySet()) {
				Node neighbor = node.neighbors.get(r);

				if(!explored.contains(neighbor)) {
					
					if(!frontier.contains(neighbor)) {
						frontier.add(neighbor);

						neighbor.cost = d.predictions.get(r.name);
					}
					
				}
			}
		}
		return path;
	}
	
	private double astar(Node source, Node destination, Day d) {
		
	     HashSet<Node> visited = new HashSet<Node>();
	     Map<Node, Double> distances = initializeInfimum();
	     Queue<Node> priorityQueue = new PriorityQueue<Node>();
	     
	     double currentDistnce = 0.0; 
	     
	     source.cost = 0.0;
	     distances.put(source, 0.0);
	     priorityQueue.add(source);
	     
	     Node current = null;
	     
	     
	     
	     while (!priorityQueue.isEmpty()) {
	            current = priorityQueue.remove();
	 
	            if (!visited.contains(current) ){
	                visited.add(current);
	                // if last element in PQ reached
	                if (current.equals(destination)) break;
	 
	                Set<Node> neighbors = (Set<Node>) current.neighbors.values();
	                
	                for (Node neighbor : neighbors) {
	                    if (!visited.contains(neighbor) ){  
	                    	
	                    	HashSet<Node> nodes = new HashSet<Node>();
	                    	nodes.add(neighbor);
	                    	nodes.add(current);
	                    	ArrayList<Road> connectors = graph.roadNodes.get(nodes);
	                    	
	                        // calculate predicted distance to the end node
	                        double predictedDistance = astarHeuristic(source,destination,d);
	                        
	                        // 1. calculate distance to neighbor. 2. calculate dist from start node
	                        double neighborDistance = d.predictions.get(findCheapestRoad(connectors,d).name);
	                        double totalDistance = current.distanceToStart + neighborDistance + predictedDistance;
	 
	                        // check if distance smaller
	                        if(totalDistance < distances.get(neighbor) ){
	                            // update n's distance
	                            distances.put(neighbor, totalDistance);
	                            // used for PriorityQueue
	                            neighbor.distanceToStart = totalDistance;
	                            neighbor.predictedDistance = predictedDistance;
	                            // enqueue
	                            priorityQueue.add(neighbor);
	                        }
	                    }
	                }
	            }
	        }
	        return current.distanceToStart + current.cost;
	}
	
	
	private double astarHeuristic(Node source, Node destination, Day d) {
		LinkedList<Node> nodes = bfs(graph,source,destination,d);
		LinkedList<Road> roads = findRoadPath(nodes,d);
		
		double totCost = 0.0;
		
		for(Road r : roads) {
			totCost+=d.predictions.get(r.name);
		}
		
		return totCost;
	}
	
	private HashMap<Node,Double> initializeInfimum() {
		HashMap<Node,Double> m = new HashMap<Node,Double>();
		
		for(Node n: graph.nodes.values()) {
			n.cost = Double.MAX_VALUE;
			m.put(n,n.cost);
		}
		
		return m;
	}
	
	public void export() throws IOException {
		PrintWriter pr = new PrintWriter(new File("output" + File.separator + "output.txt"));
	}
}
