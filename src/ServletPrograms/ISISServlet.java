package ServletPrograms;

import java.io.IOException;
//import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import com.sun.grizzly.websockets.WebSocketEngine;
//import com.sun.xml.ws.transport.http.servlet.WSServlet;

import WebSocketClasses.ISISServerApplication;

@WebServlet("/ISISServlet")
public class ISISServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The application (WebSocketListener) registered with this servlet
	 */
	private ISISServerApplication app = new ISISServerApplication();

	/**
	 * Method to initialize the servlet
	 */
	public void init() {
		// Resister the ISIS application with the WebSocket Engine
		WebSocketEngine.getEngine().register(app);
		
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, this.getServletName() + "Time: " + new java.util.Date() + ", " + 
				"initialised servlet and registered ISIS application");
	}

	/**
	 * Method to handle HTTP-GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + "Servlet doGet() event fired");

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
	}

	/**
	 * Method to handle HTTP-POST requests
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + "Servlet doPost() event fired");

		doGet(request, response);
	}

	/**
	 * Method to destroy the servlet
	 */
	public void destroy() {
		// Unregister the application from the WebSocket Engine
		WebSocketEngine.getEngine().unregister(app);
		
		// Create log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						this.getServletName() + "servlet: destroyed");
	}
}
