package com.divirad.flightcompensation.micro.calculator;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divirad.flightcompensation.micro.calculator.data.AirportDao;
import com.divirad.flightcompensation.micro.calculator.data.FlightDao;
import com.divirad.flightcompensation.monolith.data.Airport;
import com.divirad.flightcompensation.monolith.data.Flight;

import static com.divirad.flightcompensation.monolith.lib.SpecialMaths.distance;

/**
 * Servlet implementation class CheckServlet
 */
@WebServlet("/check")
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append(
				"<form action=\"CheckServlet\" method=post>" + 
				"		<table>" + 
				"			<tr><td>Flight Number</td><td><input type=text name=flight_number></td><tr>" + 
				"			<tr><td>Date</td><td><input type=text name=flight_date></td></tr>" + 
				"		</table><button type=submit>Submit</button>" + 
				"</form>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flight_number = request.getParameter("flight_number");
		Date date = Date.valueOf(request.getParameter("flight_date"));
		PrintWriter out = response.getWriter();
		
		Flight f = FlightDao.instance.getFlight(flight_number, date);
		System.out.println(f);
		
		
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		if(f == null) {
			out.println("Flight not found");
			return;
		}
		out.println("Flight " + f.flight__iata + " on day " + f.flight_date);
		out.println("\t\t" + f.departure__iata + "\t->\t" + f.arrival__iata);
		out.println("Scheduled:\t" + format.format(f.departure__scheduled) + "\t->\t" + format.format(f.arrival__scheduled));
		
		out.println("Actual:\t\t" + format.format(f.departure__actual) + "\t->\t" + format.format(f.arrival__actual));
		
		out.printf("Delay:\t\t\t\t\t%02d:%02d:00\n", f.arrival__delay / 60, f.arrival__delay % 60);
		
		if(f.arrival__delay < 180) {
			out.println("Flight delayed less than three hours");
			out.println("Airline is not obligated to pay");
			return;
		}
		
		Airport origin = AirportDao.instance.get(f.departure__iata);
		Airport destination = AirportDao.instance.get(f.arrival__iata);
		
		double distance = distance(origin.latitude, origin.longitude, destination.latitude, destination.longitude, "K");
		out.println("Distance between " + origin.airport_name + " and " + destination.airport_name + " is " + distance + "km");
		
		double compensation;
		if(distance <= 1500) compensation = 250;
		else if(distance <= 3500) compensation = 400;
		else compensation = 600;
		
		out.println("You can get a cash compensation of " + compensation / (f.arrival__delay < 240 ? 2 : 1) + "€");
	
		
		
	}

}
