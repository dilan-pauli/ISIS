package ServletPrograms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.grizzly.websockets.WebSocketEngine;

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
	private ISISServerApplication app;

	/**
	 * Method to initialize the servlet
	 */
	public void init() {
		// TODO LEAVE AS IS FOR NOW
		app = new ISISServerApplication();
		WebSocketEngine.getEngine().register(app);
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, this.getServletName() + ": initialised");
	}

	/**
	 * Method to handle HTTP-GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String heading;
		if (session.isNew()) {
			heading = "Welcome";
		} 
		else {
			heading = "Welcome Back";
		}

		out.println("\n" + heading + "\n" + 
				"<br />" +
				"<button onclick='<% ; %>'>Send WebSocket Server A Message</button>" + 
				"\n");
	}

	/**
	 * Method to handle HTTP-POST requests
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Method to destroy the servlet
	 */
	public void destroy() {
		// TODO LEAVE AS IS FOR NOW
		WebSocketEngine.getEngine().unregister(app);
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, this.getServletName() + ": destroyed");
	}
}
