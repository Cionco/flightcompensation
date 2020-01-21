package com.divirad.flightcompensation.monolith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import user.Command;



public class App {
	
	public static final InputStream userInput = System.in;
	
    public static void main(String[] args) {
    	try (Scanner user = new Scanner(userInput)) {    		
    		System.out.print("> ");
	    	while(true) {
				if(userInput.available() != 0) {
					String command = user.nextLine();
					process_command(command);
					System.out.print("> ");
				}
	    	}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private static void process_command(String command) {
    	String[] split = command.split(" ");
    	Command cmd = Command.valueOf(split[0].toUpperCase());
    	cmd.execute(split);
    }
}
