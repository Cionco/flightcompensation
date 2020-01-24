package user;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.JSONArray;

import com.divirad.flightcompensation.monolith.App;
import com.divirad.flightcompensation.monolith.data.Airport;
import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.DataLoader;
import com.divirad.flightcompensation.monolith.data.api.DataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.DownloadParseSaveController;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;

public enum Command {
	
	BACKGROUND {
		private int output_count;
		public void execute(String...params) {
			
		}
	},

	UPDATE {
		public void execute(String...params) {
			DownloadParseSaveController controller = new DownloadParseSaveController();
			Constraint[] constraints;
			Class<?> resource;
			if(params.length != 2) {
				System.out.println("Syntax Error");
				help();
				return;
			}
			
			if(params[1].equals("flights")) {
				constraints = new Constraint[] {
						new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
					,	new Constraint("dep_iata", () -> "haj")
				};
				resource = Flight.class;
			} else if(params[1].equals("airports")) {
				constraints = new Constraint[] {
						new Constraint("limit", () -> "150")
				};
				resource = Airport.class;
			} else {
				System.out.println("Wrong argument error");
				help();
				return;
			}
			
			DataLoader fdl = DataLoader.getInstance(constraints);
			fdl.addDownloadListener(controller);
			Parser.getInstance().addParseListener(controller);
			fdl.getAllApiData(resource);
		}
		
		public void help() {
			System.out.println("Updates Data the specified data\n");
			System.out.println("Usage of update\n");
			System.out.println("update <flights|airports>");
			System.out.println();
		}
	},
	
	CHECK {
		public void execute(String... params) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			if(params.length != 3) {
				System.out.println("Syntax error");
				help();
				return;
			}
			System.out.println("Searching Flight...");
			Flight f = FlightDao.instance.getFlight(params[1],Date.valueOf(params[2]));
			if(f == null) {
				System.out.println("Flight not found");
				return;
			}
			System.out.println("Flight " + f.flight__iata + " on day " + f.flight_date);
			System.out.println("\t\t" + f.departure__iata + "\t->\t" + f.arrival__iata);
			System.out.println("Scheduled:\t" + format.format(f.departure__scheduled) + "\t->\t" + format.format(f.arrival__scheduled));
			System.out.print("Correct Flight? (y/n)");
			Scanner input = new Scanner(App.userInput);
			if(!input.nextLine().toUpperCase().equals("Y")) return;
			System.out.println("Actual:\t\t" + format.format(f.departure__actual) + "\t->\t" + format.format(f.arrival__actual));
			
			System.out.printf("Delay:\t\t\t\t\t%02d:%02d:00\n", f.arrival__delay / 60, f.arrival__delay % 60);
		}
		
		public void help() {
			System.out.println("Checks customer right for specified flight");
			System.out.println("Usage of check");
			System.out.println("check <flight number> <flight date yyyy-MM-dd>");
			System.out.println();
		}
	},
	
	EXIT {
		public void execute(String... params) {
			System.exit(0);
		}
	};
	
	private static final String COMMAND_NOT_IMPLEMENTED_ERROR = "Command is not implemented yet";
	private static final String OPTION_NOT_IMPLEMENTED_ERROR = "Option is not implemented yet, try other options";
	
	public void execute(String...params) {
		throw new AbstractMethodError(COMMAND_NOT_IMPLEMENTED_ERROR);
	}
	
	public void help() {
		throw new AbstractMethodError("No manual page for this command");
	}
}
