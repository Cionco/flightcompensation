package com.divirad.flightcompensation.micro.transformer.server;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.divirad.flightcompensation.micro.transformer.data.api.ParseController;
import com.divirad.flightcompensation.monolith.data.api.Parser;


/**
 * Application Lifecycle Listener implementation class DbPmPlusContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {
	
	private static DataSource dataSource = null;

	/**
	 * Default constructor. 
	 */
	public ContextListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0)  { 
		
		System.out.println("und tschuess...");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event)  { 
		System.getProperties().setProperty("Dorg.apache.cxf.stax.allowInsecureParser", "1");
		System.out.println("sys prop set");

		ServletContext servletContext = event.getServletContext();
		
		dataSource = getDBConnection(servletContext);
		
		Parser.getInstance().addParseListener(new ParseController());
	}
	
	public static DataSource getDBConnection(ServletContext servletContext) {
		DataSource ds = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/Transformer");
			System.out.println("Database Connection created successfully");
		} catch(NamingException e) {
			e.printStackTrace();
		}
		return ds;
		
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
}
