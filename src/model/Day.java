package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day {
	
	final int ID;
	Map<String, Double> predictions;
	Map<String, Double> actual;
	
	public Day(int ID, Graph g, Scanner scanner) {
		this.ID = ID;
		
		String line = scanner.nextLine();	// Consume <Day>
		
		predictions = new HashMap<String, Double>();
		actual = new HashMap<String, Double>();
		
		while (true) {
			
			line = scanner.nextLine();

			if (line.contentEquals("</Day>")) {
				break;
			}

			String[] arr = line.split(";");
			
			Road r = g.roads.get(arr[0]);
			
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
			
			predictions.put(arr[0], virtualWeight);
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("Day: " + ID + "\n----------------\n\n");
		
		for (String key : predictions.keySet()) {
			buffer.append(key + " -> Predicted: " + predictions.get(key) + " Actual: " + actual.get(key) + "\n");
		}
		
		return buffer.toString();
		
	}
}
