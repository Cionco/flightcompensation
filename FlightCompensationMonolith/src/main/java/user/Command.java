package user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.divirad.flightcompensation.monolith.data.api.DownloadParseSaveController;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.Parser;

public enum Command {

	UPDATE {
		public void execute(String...params) {
			DownloadParseSaveController controller = new DownloadParseSaveController();
			Constraint[] constraints;
			String resource;
			if(params[1].equals("flights")) {
				constraints = new Constraint[] {
						new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
					,	new Constraint("dep_iata", () -> "haj")
				};
				resource = "flights";
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
	};
	
	private static final String COMMAND_NOT_IMPLEMENTED_ERROR = "Command is not implemented yet";
	private static final String OPTION_NOT_IMPLEMENTED_ERROR = "Option is not implemented yet, try other options";
	
	public void execute(String...params) {
		throw new AbstractMethodError();
	}
	
	public void help() {
		throw new AbstractMethodError("No manual page for this command");
	}
}
