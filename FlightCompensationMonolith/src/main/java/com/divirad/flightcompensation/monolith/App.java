package com.divirad.flightcompensation.monolith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.divirad.flightcompensation.monolith.data.api.DataLoader;
import com.divirad.flightcompensation.monolith.data.api.DownloadParseSaveController;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.lib.StreamThread;
import com.divirad.flightcompensation.monolith.user.Command;



public class App {
	
	public static final InputStream userInput = System.in;
	
    public static void main(String[] args) {
    	DownloadParseSaveController controller = new DownloadParseSaveController();
    	DataLoader.getInstance().addDownloadListener(controller);
    	Parser.getInstance().addParseListener(controller);
    	StreamThread main = new StreamThread(() -> {
    		try (Scanner user = new Scanner(userInput)) {    		
    			System.out.print("> ");
    			while(true) {
    				if(userInput.available() != 0) {
    					String command = user.nextLine();
    					process_command(command);
    					StreamThread.currentThread().getOut().print("> ");
    				}
    			}
    		} catch(IOException e) {
    			e.printStackTrace();
    		}    		
    	}, System.out);
    	main.start();
    }
    
    private static void process_command(String command) {
    	String[] split = command.split(" ");
    	Command cmd = Command.valueOf(split[0].toUpperCase());
    	cmd.execute(split);
    }
}
