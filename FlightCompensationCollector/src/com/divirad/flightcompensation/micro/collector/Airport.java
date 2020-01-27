package com.divirad.flightcompensation.micro.collector;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divirad.flightcompensation.micro.collector.data.api.DataLoader;
import com.divirad.flightcompensation.micro.collector.data.api.DownloadController;
import com.divirad.flightcompensation.micro.collector.data.api.DataLoader.Constraint;
import com.divirad.flightcompensation.micro.collector.data.database.JSONDao;
import com.divirad.flightcompensation.micro.collector.server.JSON;

/**
 * Servlet implementation class Airport
 */
@WebServlet("/airports")
public class Airport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Airport() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSON j = JSONDao.instance.get(com.divirad.flightcompensation.monolith.data.Airport.class.getSimpleName());
		
		response.getWriter().append(j.toJson().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DownloadController controller = new DownloadController();
		controller.setNewDataDate(new Date(new java.util.Date().getTime()));
		Constraint[] constraints = new Constraint[] {};
		
		DataLoader dl = new DataLoader(constraints);
		dl.addDownloadListener(controller);
		dl.getAllApiData(com.divirad.flightcompensation.monolith.data.Airport.class);
		
	}

}
