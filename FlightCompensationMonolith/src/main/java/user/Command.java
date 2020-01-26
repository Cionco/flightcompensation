package user;

import static lib.SpecialMaths.distance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import com.divirad.flightcompensation.monolith.App;
import com.divirad.flightcompensation.monolith.data.Airport;
import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.DataLoader;
import com.divirad.flightcompensation.monolith.data.api.DataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.DownloadParseSaveController;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.AirportDao;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;

import lib.StreamThread;

public enum Command {
	
	BACKGROUND {
		public void execute(String...params) {
			try {
				StreamThread newThread = new StreamThread(() -> {
					String[] newParams = new String[params.length - 1];
					for(int i = 0; i < newParams.length; i++) newParams[i] = params[i + 1];
					Command cmd = Command.valueOf(newParams[0].toUpperCase());
			    	cmd.execute(newParams);
			    	StreamThread.currentThread().getOut().close();
				}, new PrintStream(new FileOutputStream(new File("BackgroundActions.log")), true));
				newThread.start();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
		}
		
		public void help() {
			StreamThread.currentThread().getOut().println("Executes Command in the background, logs output in BackgroundActions.log");
			StreamThread.currentThread().getOut().println();
		}
	},

	UPDATE {
		public void execute(String...params) {
			DownloadParseSaveController controller = new DownloadParseSaveController();
			Constraint[] constraints;
			Class<?> resource;
			if(params.length != 2) {
				StreamThread.currentThread().getOut().println("Syntax Error");
				help();
				return;
			}
			
			if(params[1].equals("flights")) {
				constraints = new Constraint[] {
						new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
					,	new Constraint("dep_iata", () -> "fra")
				};
				resource = Flight.class;
			} else if(params[1].equals("airports")) {
				constraints = new Constraint[] {
						//new Constraint("limit", () -> "150")
				};
				resource = Airport.class;
			} else {
				StreamThread.currentThread().getOut().println("Wrong argument error");
				help();
				return;
			}
			
			DataLoader fdl = DataLoader.getInstance(constraints);
			fdl.addDownloadListener(controller);
			Parser.getInstance().addParseListener(controller);
			fdl.getAllApiData(resource);
		}
		
		public void help() {
			StreamThread.currentThread().getOut().println("Updates Data the specified data\n");
			StreamThread.currentThread().getOut().println("Usage of update\n");
			StreamThread.currentThread().getOut().println("update <flights|airports>");
			StreamThread.currentThread().getOut().println();
		}
	},
	
	CHECK {
		public void execute(String... params) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			if(params.length != 3) {
				StreamThread.currentThread().getOut().println("Syntax error");
				help();
				return;
			}
			StreamThread.currentThread().getOut().println("Searching Flight...");
			Flight f = FlightDao.instance.getFlight(params[1],Date.valueOf(params[2]));
			if(f == null) {
				StreamThread.currentThread().getOut().println("Flight not found");
				return;
			}
			StreamThread.currentThread().getOut().println("Flight " + f.flight__iata + " on day " + f.flight_date);
			StreamThread.currentThread().getOut().println("\t\t" + f.departure__iata + "\t->\t" + f.arrival__iata);
			StreamThread.currentThread().getOut().println("Scheduled:\t" + format.format(f.departure__scheduled) + "\t->\t" + format.format(f.arrival__scheduled));
			StreamThread.currentThread().getOut().print("Correct Flight? (y/n)");
			Scanner input = new Scanner(App.userInput);
			String choice = input.nextLine().toUpperCase();
			input.close();
			if(!choice.equals("Y")) return;
			StreamThread.currentThread().getOut().println("Actual:\t\t" + format.format(f.departure__actual) + "\t->\t" + format.format(f.arrival__actual));
			
			StreamThread.currentThread().getOut().printf("Delay:\t\t\t\t\t%02d:%02d:00\n", f.arrival__delay / 60, f.arrival__delay % 60);
			
			if(f.arrival__delay < 180) {
				StreamThread.currentThread().getOut().println("Flight delayed less than three hours");
				StreamThread.currentThread().getOut().println("Airline is not obligated to pay");
				return;
			}
			
			Airport origin = AirportDao.instance.get(f.departure__iata);
			Airport destination = AirportDao.instance.get(f.arrival__iata);
			
			double distance = distance(origin.latitude, origin.longitude, destination.latitude, destination.longitude, "K");
			StreamThread.currentThread().getOut().println("Distance between " + origin.airport_name + " and " + destination.airport_name + " is " + distance + "km");
			
			double compensation;
			if(distance <= 1500) compensation = 250;
			else if(distance <= 3500) compensation = 400;
			else compensation = 600;
			
			StreamThread.currentThread().getOut().println("You can get a cash compensation of " + compensation / (f.arrival__delay < 240 ? 2 : 1) + "€");
		}
		
		public void help() {
			StreamThread.currentThread().getOut().println("Checks customer right for specified flight");
			StreamThread.currentThread().getOut().println("Usage of check");
			StreamThread.currentThread().getOut().println("check <flight number> <flight date yyyy-MM-dd>");
			StreamThread.currentThread().getOut().println();
		}
	},
	
	LIST {
		public void execute(String... params) {
			ArrayList<Flight> flights = FlightDao.instance.getFlights(Date.valueOf(params[1]));
			int i = 1;
			for(Flight f : flights) {
				StreamThread.currentThread().getOut().println(i++ + ". " + f);
			}
		}
	},
	
	EXIT {
		public void execute(String... params) {
			System.exit(0);
		}
	};
	
	private static final String COMMAND_NOT_IMPLEMENTED_ERROR = "Command is not implemented yet";
	@SuppressWarnings("unused")
	private static final String OPTION_NOT_IMPLEMENTED_ERROR = "Option is not implemented yet, try other options";
	
	public void execute(String...params) {
		throw new AbstractMethodError(COMMAND_NOT_IMPLEMENTED_ERROR);
	}
	
	public void help() {
		throw new AbstractMethodError("No manual page for this command");
	}
}
