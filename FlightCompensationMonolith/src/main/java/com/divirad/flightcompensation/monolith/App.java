package com.divirad.flightcompensation.monolith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;



public class App {
	
	private static InputStream userInput = System.in;
	
    public static void main(String[] args) {
    	try (Scanner user = new Scanner(System.in)) {
	    	while(true) {
				if(userInput.available() != 0) {
					String command = user.nextLine();
					process_command(command);
				}
	    	}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private static void process_command(String command) {
    	
    }
}
