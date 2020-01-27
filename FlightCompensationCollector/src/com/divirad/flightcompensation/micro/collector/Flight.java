package com.divirad.flightcompensation.micro.collector;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divirad.flightcompensation.micro.collector.data.api.DataLoader;
import com.divirad.flightcompensation.micro.collector.data.api.DataLoader.Constraint;
import com.divirad.flightcompensation.micro.collector.data.api.DownloadController;
import com.divirad.flightcompensation.micro.collector.data.database.JSONDao;
import com.divirad.flightcompensation.micro.collector.server.JSON;
import com.divirad.flightcompensation.monolith.data.api.DownloadListener;

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
		System.out.println("Hello");
		Date date = Date.valueOf(request.getParameter("date"));
		JSON j = JSONDao.instance.get(com.divirad.flightcompensation.monolith.data.Flight.class.getSimpleName(), date);
		
		response.getWriter().append(j.toJson().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DownloadController controller = new DownloadController();
		controller.setNewDataDate(Date.valueOf(request.getParameter("flight_date")));
		Constraint[] constraints = new Constraint[] {
				new Constraint("flight_date", () -> controller.getNewDataDate().toString()),
				new Constraint("dep_iata", () -> "fra"),
				new Constraint("limit", () -> "50")
		};
		
		DataLoader dl = new DataLoader(constraints);
		dl.addDownloadListener(controller);
		dl.getAllApiData(com.divirad.flightcompensation.monolith.data.Flight.class);
		
	}

}