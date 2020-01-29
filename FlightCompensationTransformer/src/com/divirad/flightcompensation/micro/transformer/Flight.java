package com.divirad.flightcompensation.micro.transformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.divirad.flightcompensation.micro.transformer.data.database.FlightDao;
import com.divirad.flightcompensation.monolith.data.api.Parser;

/**
 * Servlet implementation class Flight
 */
@WebServlet("/flights")
public class Flight extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Flight() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String flight_number = request.getParameter("flight__iata");
			Date date = Date.valueOf(request.getParameter("flight_date"));
			com.divirad.flightcompensation.monolith.data.Flight f = FlightDao.instance.getFlight(flight_number, date);
			response.getWriter().append(f.toJson().toString());
		} catch(Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("response_code", 500);
			result.put("message", e.getMessage());
			response.getWriter().append(result.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date date = Date.valueOf(request.getParameter("flight_date"));
		String output = "";
		JSONObject result;
		long time_before = System.currentTimeMillis();
		try {
			URL obj = new URL("http://localhost:8080/FlightCompensationCollector/flights?flight_date=" + date);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		    conn.setDoOutput(false);
		    conn.setRequestMethod("GET");
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			for (String line; (line = reader.readLine()) != null;) {
			        output += line;
			}
			reader.close();
			Parser.getInstance().parseData(com.divirad.flightcompensation.monolith.data.Flight.class, new JSONObject(output).getJSONArray("data"));
			result = new JSONObject();
			result.put("response_code", 200);
			result.put("message", "Data parsed and Stored to DB");
		} catch(Exception e) {
			e.printStackTrace();
			result = new JSONObject();
			result.put("response_code", 500);
			result.put("message", e.getMessage());
		}
		System.out.println("Operation took " + (System.currentTimeMillis() - time_before) + " milliseconds");
		response.getWriter().append(result.toString());
	}
}
