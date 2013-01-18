package ServletPrograms;

//import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import com.sun.grizzly.websockets.WebSocketEngine;

import WebSocketClasses.ISISServerApplication;

/*
 * loadOnStartup=1 ensures that the servlet gets started as soon as the server gets deployed
 * And as soon as the server deploys, it will register the ISISServerApplication with the
 * WebSocketEngine of Grizzly
 * 
 * (Hook into Glassfish)
 */
@WebServlet(name="ISISServlet", urlPatterns="/ISISServlet", loadOnStartup=1)
public class ISISServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The application (WebSocketListener) registered with this servlet
	 */
	private ISISServerApplication app;

	/**
	 * Method to initialize the servlet
	 * When initializing, register the ISISServerApplication with the WebSocketEngine of Grizzly
	 */
	public void init() {
		try {
			super.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		app = new ISISServerApplication();
		
		// Resister the ISIS application with the (Grizzly's) WebSocket Engine
		WebSocketEngine.getEngine().register(app);
		
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, this.getServletName() + "Time: " + new java.util.Date() + ", " + 
				"initialised servlet and registered ISIS application");
	}
	
	/**
	 * Method to destroy the servlet
	 * When destroyed, unregister the ISISServerApplication from the WebSocketEngine of Grizzly
	 */
	public void destroy() {
		super.destroy();
		
		// Unregister the application from the (Grizzly's) WebSocket Engine
		WebSocketEngine.getEngine().unregister(app);
		
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						this.getServletName() + " servlet: destroyed");
	}
	
	
	

	/**
	 * Method to handle HTTP-GET requests
	 */
	/**public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + "Servlet doGet() event fired");*/

		//HttpSession session = request.getSession(true);
		//SCRAP request.getContentType().endsWith(arg0)

		// Set the response type
		//response.setContentType("text/html");

		//PrintWriter out = response.getWriter();

		/*String heading;
		if (session.isNew()) {
			heading = "Welcome";
		} 
		else {
			heading = "Welcome Back";
		}
		out.println("\n" + heading + "\n" + 
				"<br />" +
				"<button onclick='<% ; %>'>Send WebSocket Server A Message</button>" + 
				"\n");*/

		/*out.println("\n" + "SERVER RECEIVED YOUR MESSAGE:" + "\n" +
				"<br />" + "\n");*/
	/**}*/

	/**
	 * Method to handle HTTP-POST requests
	 */
	/**public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + "Servlet doPost() event fired");

		doGet(request, response);
	}*/
}
