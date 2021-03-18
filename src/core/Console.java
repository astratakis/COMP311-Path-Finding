package core;

import java.io.*;
import java.util.*;

import model.ProblemInstance;

public class Console {
	
	private final static String INPUT_FILE = "sampleGraph1.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File("input" + File.separator + args[0]));
		}
		catch (IndexOutOfBoundsException e) {
			scanner = new Scanner(new File("src" + File.separator + "input" + File.separator + INPUT_FILE));
		}
		
		ProblemInstance pi = new ProblemInstance(scanner);
		System.out.println(pi);
		
		scanner.close();
	}

}
