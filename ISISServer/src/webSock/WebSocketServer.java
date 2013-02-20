package webSock;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

public class WebSocketServer {
	/**
	 * Queue for placing incoming WebSocket items (from Internet clients)
	 */
	private WebSocketIncomingQueue inQueue;

	/**
	 * Queue for placing outgoing WebSocket items (bound for Internet clients)
	 */
	private WebSocketOutgoingQueue outQueue;

	/**
	 * Embedded (GlassFish) server supporting the WebSocket protocol used to send and
	 * receive WebSocket messages
	 */
	private GlassFish embeddedServer;

	/**
	 * Deployer for the embedded server
	 */
	//private Deployer deployer;

	/**
	 * Name of the deployed app on the embedded server
	 */
	private String deployedApp;

	/**
	 * File name string of the app to deploy on the embedded server
	 */
	//private String fileName;

	/**
	 * HTTP port number for the embedded server
	 */
	private int httpPortNum = 2000;	//8080

	/**
	 * HTTPS port number for the embedded server
	 */
	private int httpsPortNum = 4000;	//8081

	/**
	 * Constructor
	 */
	public WebSocketServer() {
		// Create Incoming Queue
		this.inQueue = new WebSocketIncomingQueue();

		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created incoming queue for the WebSocket server component");

		// Create Outgoing Queue
		this.outQueue = new WebSocketOutgoingQueue();

		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created outgoing queue for the WebSocket server component");

		/*
		 * Create and configure GlassFish server (Has Sender/Receiver within the
		 * application it deploys to handle sending and receiving messages)
		 * Call initialization function
		 */
		if(!this.initEmbeddedServer()) {
			// Log the failure to initialize the embedded server
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
					"Failure: failed to start and configure the embedded server");
		}

		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created and started the embedded server");
	}

	/**
	 * Initialize and configure the embedded server
	 */
	// Private because only the class constructor needs to mess around with the server configuration
	private boolean initEmbeddedServer() {
		try {
			// Create GlassFish properties (SEE DEFAULT domain.xml FILE USED BY EMBEDDED GLASSFISH ON ORACLE SITE)
			GlassFishProperties glassfishProperties = new GlassFishProperties();
			// Configure GlassFish to listen on the specified HTTP and HTTPS ports
			// (If the ports are not set, the application fails to start and throws an exception)
			glassfishProperties.setPort("http-listener", this.httpPortNum);
			glassfishProperties.setPort("https-listener", this.httpsPortNum);
			//glassfishProperties.setPort(arg0, arg1);

			// Create new server instance
			this.embeddedServer = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);
			//this.embeddedServer = GlassFishRuntime.bootstrap().newGlassFish();
			// Start the new server
			this.embeddedServer.start();

			// Current working directory
			String cwdStr = "";

			// Deploy a web application at the given URI with /ISISServer as the context root
			//TODO: GET RID OF THIS this.deployer = this.embeddedServer.getDeployer();

			// Print the path of the current working directory (Check)
			try {
				cwdStr = new File(".").getCanonicalPath();
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", " + 
								"Current working directory: " + cwdStr);
			} 
			catch (IOException ex) {
				ex.printStackTrace();
			}


			///////////////////////////
			try {
				// Create a scattered archive web application
				ScatteredArchive archive = new ScatteredArchive("ISISServer", ScatteredArchive.Type.WAR);
				// Print the created archive (CHECKING)
				System.out.println("Time: " + new java.util.Date() + 
						", Created scattered archive: " + archive.toString());

				
				// Add directories and files to the scattered archive
				
				// Print the path for the servlet and WebSocket classes (CHECKING)
				System.out.println("Time: " + new java.util.Date() + ", Path of class files: " + 
						new File("bin", "webSock").toURI());
				// webSock directory contains the compiled servlets and code
				archive.addClassPath(new File("bin", "webSock"));
				////////
				/*// Print the path for the servlet classes (CHECKING)
				System.out.println("Time: " + new java.util.Date() + 
						", Path of servlet source file: " + new File("src" + File.separator + "webSock" + 
				File.separator + "ISISServlet.java").toURI());
				// webSock directory contains the compiled servlets and code
				archive.addClassPath(new File("src" + File.separator + "webSock" + File.separator + 
					"ISISServlet.java"));*/
				
				
				// Print the path for the Web app .jsp file(s)
				System.out.println("Time: " + new java.util.Date() + 
						", Path of web app .jsp files: " + new File("WebContent" + File.separator + 
								"index.jsp").toURI());
				archive.addClassPath(new File("WebContent" + File.separator + "index.jsp"));

				// Print the path for the deployment descriptor file (CHECKING)
				System.out.println("Path of deployment descriptor: " + new File("WebContent" + File.separator + 
						"WEB-INF" + File.separator + "glassfish-web.xml").toURI());
				// WebContent/WEB-INF/glassfish-web.xml is the WEB-INF/glassfish-web.xml
				archive.addMetadata(new File("WebContent" + File.separator + 
						"WEB-INF" + File.separator + "glassfish-web.xml"));
				////////
				//System.out.println("Path of deployment descriptor: " + new File("WEB-INF", "sun-web.xml").toURI());
				//archive.addMetadata(new File("WEB-INF", "sun-web.xml"));
				/*System.out.println("Time: " + new java.util.Date() + 
						", Path of deployment descriptor: " + new File("WEB-INF", "glassfish-web.xml").toURI());
				archive.addMetadata(new File("WEB-INF", "glassfish-web.xml"));*/

				
				// Print the path for the manifest file
				System.out.println("Time: " + new java.util.Date() + 
						", Path of manifest file: " + new File("WebContent" + File.separator + "META-INF" + 
								File.separator + "MANIFEST.MF").toURI());
				archive.addMetadata(new File("WebContent" + File.separator + "META-INF" + File.separator + 
						"MANIFEST.MF"));
				////////
				/*// Print the path for the manifest file
				System.out.println("Time: " + new java.util.Date() + 
						", Path of manifest file: " + new File("META-INF", "MANIFEST.MF").toURI());
				archive.addMetadata(new File("META-INF", "MANIFEST.MF"));*/

				
				// resources/MyLogFactory is my META-INF/services/org.apache.commons.logging.LogFactory
				//archive.addMetadata(new File("resources", "MyLogFactory"), "META-INF/services/org.apache.commons.logging.LogFactory");

				
				// Print the path for the scattered archive URI passed to the deploy method
				System.out.println("Time: " + new java.util.Date() + 
						", Scattered archive URI (passed to deploy method): " + archive.toURI());
				
				// Print the name of the deployed app
				this.deployedApp = "";
				this.deployedApp = this.embeddedServer.getDeployer().deploy(
						archive.toURI(), "--name=ISISServer", "--force=true", "--contextroot=ISISServer");
				System.out.println("Time: " + new java.util.Date() + 
						", Deployed app name created from passed in scattered archive URI: " + 
						this.deployedApp + "\n");

				// Log the name of the web app that an attempt was made to deploy
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", " + 
								"Attempted to deploy App: \"" + this.deployedApp +"\"");
			}
			catch(GlassFishException exc) {
				exc.printStackTrace();
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Failed to deploy web application on server");
			}
			catch(Exception exception) {
				exception.printStackTrace();
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Failed to create scattered archive");
			}
			///////////////////////////


			/*//Remember to add ServerProcessISIS.war to the current folder to test this
			try {
			// Web archive file
			File war = new File("ServerProcessISIS.war");

			// Print the path for the web archive file URI passed to the deploy method
			System.out.println("Web archive URI (passed to deploy method): " + war.toURI().toString());

			// Print the name of the deployed app
			this.deployedApp = "";
			this.deployedApp = this.embeddedServer.getDeployer().deploy(
					war, "--name=ServerProcessISIS", "--force=true", "--contextroot=ISISServer");
			System.out.println("Deployed app name created from passed in scattered archive URI: " + 
					this.deployedApp + "\n");

			// Log the name of the web app that an attempt was made to deploy
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
							"Attempted to deploy App: \"" + this.deployedApp +"\"");
			}
			catch(Exception excpt) {
			System.out.println("Exception occured when an attempt was made to deploy the war file");
				excpt.printStackTrace();
			}*/


			// Set the path to the directory containing the files for the Web application
			//xx/this.fileName = cwdStr + File.separator + "bin" + File.separator + "webSock";
			//this.fileName = cwdStr + File.separator + "WebContent";
			// Deploy the application 
			//xx/"--contextroot=/ISISServer"
			//this.deployedApp = this.deployer.deploy(new File(fileName).toURI(), 
			//										"--force=true",
			//										"--contextroot=/ISISServer");//
			//java.util.logging.Logger.getAnonymousLogger().log(
			//		Level.INFO, "Time: " + new java.util.Date() + ", " + 
			//				"Attempted to deploy App \"" + this.deployedApp + "\" at: " + new File(fileName).toURI());


			////
			// Run commands as required (to configure embedded GlassFish server)
			////
			CommandRunner commandRunner = this.embeddedServer.getService(CommandRunner.class);
			CommandResult commandResult;
			
			// Run a command to create a http listener (8080??)	 TODO PORT NUMBER???
			commandResult = commandRunner.run("create-http-listener",
					"--listenerport=8000", "--listeneraddress=0.0.0.0",
					"--default-virtual-server=server", "http-listener-1");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to create primary http listener for the embedded server\n" +
								"Command Result: " + commandResult.toString());
			}
			// Run a command to create a thread pool
			commandResult = null;
			commandResult = commandRunner.run("create-threadpool", "--maxthreadpoolsize=5",
					"--minthreadpoolsize=2", "thread-pool-1");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to create thread pool for the primary http listener on " +
								"the embedded server\n" +
								"Command Result: " + commandResult.toString());
			}
			// Run a command to associate the thread pool with the previously created http listener
			commandResult = null;
			commandResult = commandRunner.run("set", "server.network-config.network-listeners." +
					"network-listener.http-listener-1.thread-pool=thread-pool-1");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to associate thread pool with the primary http listener on " +
								"the embedded server\n" +
								"Command Result: " + commandResult.toString());
			}
			// Run a command to enable WebSocket support the new http listener
			commandResult = null;
			//asadmin set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.websockets-support-enabled=true
			commandResult = commandRunner.run("set", "configs.config.server-config.network-config." +
					"protocols.protocol.http-listener-1.http.websockets-support-enabled=true");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to enable WebSocket support for the new http listener on " +
								"the embedded server\n" +
								"Command Result: " + commandResult.toString());
			}
			
			// Run a command to enable WebSocket support the default http listener 
			commandResult = null;
			commandResult = commandRunner.run("set", "configs.config.server-config.network-config." +
					"protocols.protocol.http-listener.http.websockets-support-enabled=true");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to enable WebSocket support for the default http listener on " +
								"the embedded server\n" +
								"Command Result: " + commandResult.toString());
			}

			return true;
		} 
		catch (GlassFishException e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
					"Exception: thrown when configuring and starting embedded server");
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * Shut down the embedded server and undeploy the ISIS Server Application from it
	 */
	//TODO MAKE SURE TO CALL THIS METHOD OUTSIDE BEFORE ENTIRE ISIS SERVER PROGRAM SHUTS DOWN
	public void shutDownEmbeddedServer() {
		try {
			// Undeploy the ISIS Server Application from the embedded server
			this.embeddedServer.getDeployer().undeploy(this.deployedApp);
			// Stop the embedded server
			this.embeddedServer.stop();
			// Dispose of the embedded server
			this.embeddedServer.dispose();
		}
		catch(GlassFishException e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
					"Exception: thrown when attempting to shut down the embedded server");
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the status of the embedded server
	 */
	public String getStatusOfEmbeddedServer() {
		try {
			return this.embeddedServer.getStatus().toString();
		} catch (GlassFishException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Get the applications deployed on the embedded server
	 */
	public String getDeployedApplications() {
		try {
			return this.embeddedServer.getDeployer().getDeployedApplications().toString();
		} catch (GlassFishException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Obtain the WebSocket incoming message queue
	 */
	public WebSocketIncomingQueueInterface getIncomingMsgQueue() {
		/*
		 * Note: the interface is returned to restrict access to select few methods
		 * to manipulate the queue
		 */
		return this.inQueue;
	}

	/**
	 * Obtain the WebSocket outgoing message queue
	 */
	public WebSocketOutgoingQueueInterface getOutgoingMsgQueue() {
		/*
		 * Note: the interface is returned to restrict access to select few methods
		 * to manipulate the queue
		 */
		return this.outQueue;
	}

	/**
	 * Obtain the deployed app on the server
	 */
	//public String getDeployedApp() {
	//return this.deployedApp;
	//}

	/**
	 * Obtain the embedded server
	 */
	// Maybe return interface that only allows the calls: Destroy destroyGlassFish
	//public GlassFish getEmbeddedServer() {
	//return this.embeddedServer;
	//}
}
