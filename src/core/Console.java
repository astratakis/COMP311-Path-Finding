package core;

import java.io.*;
import java.util.*;

import model.ProblemInstance;

public class Console {
	
	private final static String INPUT_FILE = "sampleGraph1.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("src" + File.separator + "input" + File.separator + INPUT_FILE));
		
		ProblemInstance pi = new ProblemInstance(scanner);
		System.out.println(pi);
		
		scanner.close();
	}

}
