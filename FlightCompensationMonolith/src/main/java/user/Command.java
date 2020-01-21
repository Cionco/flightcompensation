package user;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.divirad.flightcompensation.monolith.App;
import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.DownloadParseSaveController;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;

public enum Command {

	UPDATE {
		public void execute(String...params) {
			DownloadParseSaveController controller = new DownloadParseSaveController();
			Constraint[] constraints;
			Class<?> resource;
			if(params[1].equals("flights")) {
				constraints = new Constraint[] {
						new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
					,	new Constraint("dep_iata", () -> "haj")
				};
				resource = Flight.class;
			} else if(params[1].equals("airports")) {
				System.out.println(OPTION_NOT_IMPLEMENTED_ERROR);
				return;
			} else {
				help();
				return;
			}
			
			FlightDataLoader fdl = FlightDataLoader.getInstance(constraints);
			fdl.addDownloadListener(controller);
			Parser.getInstance().addParseListener(controller);
			new Thread(() -> {
				fdl.getAllApiData(resource);
			}).start();
		}
		
		public void help() {
			System.out.println("Updates Data the specified data\n");
			System.out.println("Usage of update\n");
			System.out.println("update <flights|airports>");
		}
	},
	
	CHECK {
		public void execute(String... params) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			System.out.println("Checking Flight ");
			Flight f = FlightDao.instance.getFlight(params[1],Date.valueOf(params[2]));
			if(f == null) {
				System.out.println("Flight not found");
				return;
			}
			System.out.println("Flight " + f.flight_number + " on day " + f.flight_date);
			System.out.println("\t\t" + f.origin_airport + "\t->\t" + f.destination_airport);
			System.out.println("Scheduled:\t" + format.format(f.scheduled_departure) + "\t->\t" + format.format(f.scheduled_arrival));
			System.out.print("Correct Flight? (y/n)");
			Scanner input = new Scanner(App.userInput);
			if(!input.nextLine().toUpperCase().equals("Y")) return;
			System.out.println("Actual:\t\t" + format.format(f.actual_departure) + "\t->\t" + format.format(f.actual_arrival));
			
			System.out.printf("Delay:\t\t\t\t\t%02d:%02d:00\n", f.arrival_delay / 60, f.arrival_delay % 60);
		}
		
		public void help() {
			System.out.println("Checks customer right for specified flight");
			System.out.println("Usage of check");
			System.out.println("check <flight number> <flight date yyyy-MM-dd>");
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
