package webSock;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;
import org.glassfish.embeddable.Deployer;
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
	private Deployer deployer;

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
	private int httpPortNum = 8000;	//8080

	/**
	 * HTTPS port number for the embedded server
	 */
	private int httpsPortNum = 8001;	//8081

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
		 * application it deploys to handle sending and receiving messages
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

			// Create new server instance
			//this.embeddedServer = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);
			this.embeddedServer = GlassFishRuntime.bootstrap().newGlassFish();
			// Start the new server
			this.embeddedServer.start();

			// Current working directory
			String cwdStr = "";
			// Deploy a web application at the given URI with /ISISServer as the context root TODO: UPDATE COMMENT
			this.deployer = this.embeddedServer.getDeployer();
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
				// Create a scattered web application.
				ScatteredArchive archive = new ScatteredArchive("ISISServer", ScatteredArchive.Type.WAR);
				
				// Add directories and files to scattered archive
				
				//Print the path for the servlet and WebSocket classes (CHECKING)
				System.out.println("Path of class files: " + new File("bin", "webSock").toURI());
				// webSock directory contains the compiled servlets and code
				archive.addClassPath(new File("bin", "webSock"));
				
				//Print the path for the deployment descriptor file (CHECKING)
				System.out.println("Path of deployment descriptor: " + new File("WebContent" + File.separator + 
						"WEB-INF" + File.separator + "sun-web.xml").toURI());
				// WebContent/WEB-INF/sun-web.xml is the WEB-INF/sun-web.xml
				archive.addMetadata(new File("WebContent" + File.separator + 
						"WEB-INF" + File.separator + "sun-web.xml"));
				
				// resources/MyLogFactory is my META-INF/services/org.apache.commons.logging.LogFactory
				//archive.addMetadata(new File("resources", "MyLogFactory"), "META-INF/services/org.apache.commons.logging.LogFactory");
				
				// Print the path for the scattered archive URI passed to the deploy method
				System.out.println("Scattered archive URI (passed to deploy method): " + archive.toURI().toString());
				
				// Print the name of the deployed app
				this.deployedApp = this.deployer.deploy(archive.toURI(), "--force", "true",
														"--contextroot", "ISISServer");	//TODO: WHY NULL??
				System.out.println("Deployed app name created from passed in scattered archive URI: " + 
									this.deployedApp + "\n");
			}
			catch(Exception exception) {
				exception.printStackTrace();
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Failed to create scattered archive");
			}
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
							"Attempted to deploy App: \"" + this.deployedApp +"\"");
			///////////////////////////

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

			/*
			 * Run commands as required (to configure embedded GlassFish server)
			 */
			CommandRunner commandRunner = this.embeddedServer.getService(CommandRunner.class);
			// Run a command to create a http listener (8080??)
			CommandResult commandResult;
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
			commandResult = commandRunner.run("create-threadpool", "--maxthreadpoolsize=20",
					"--minthreadpoolsize=20", "thread-pool-1");
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
			// Run a command to enable WebSocket support the http listener
			commandResult = null;
			commandResult = commandRunner.run("set", "configs.config.server-config.network-config." +
					"protocols.protocol.http-listener-1.http.websockets-support-enabled=true");
			if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				// Log the problem
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.SEVERE, "Time: " + new java.util.Date() + ", " + 
								"Failed to enable WebSocket support for the primary http listener on " +
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
			this.deployer.undeploy(this.deployedApp);
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
			return this.deployer.getDeployedApplications().toString();
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
