package webSock;

import java.io.File;
import java.util.logging.Level;

import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishRuntime;

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
	private String fileName;

	/**
	 * Constructor
	 */
	public WebSocketServer() {
		// Create Incoming Queue
		this.inQueue = new WebSocketIncomingQueue();

		// Create Outgoing Queue
		this.outQueue = new WebSocketOutgoingQueue();

		/*
		 * Create and configure GlassFish server (Has Sender/Receiver within the
		 * application it deploys to handle sending and receiving messages
		 * Call initialization function
		 */
		this.initEmbeddedServer();
	}

	/**
	 * Initialize and configure the embedded server
	 */
	// Private because only the constructor needs to mess around with the server configuration
	private void initEmbeddedServer() {
		try {
			// Create new server instance
			this.embeddedServer = GlassFishRuntime.bootstrap().newGlassFish();
			// Start the new server
			this.embeddedServer.start();

			// Deploy a web application at the given URI with /ISIS as the context root
			this.deployer = this.embeddedServer.getService(Deployer.class);
			this.fileName = "simple.war";	//TODO PUT LEGIT FILE NAME HERE
			this.deployedApp = this.deployer.deploy(new File(fileName).toURI(),
					"--contextroot=ISIS",
					"--force=true");

			/*
			 * Run commands as required (to confgure embedded GlassFish server)
			 */
			CommandRunner commandRunner = this.embeddedServer.getService(CommandRunner.class);
			// Run a command to create a http listener
			CommandResult commandResult;
			commandResult = commandRunner.run("create-http-listener",
					"--listenerport=8080", "--listeneraddress=0.0.0.0",
					"--default-virtual-server=server", "http-listener-1");
			/*if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				;
			}*/
			// Run a command to create a thread pool
			commandResult = commandRunner.run("create-threadpool", "--maxthreadpoolsize=200",
					"--minthreadpoolsize=200", "thread-pool-1");
			/*if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				;
			}*/
			// Run a command to associate the thread pool with the previously created http listener
			commandResult = commandRunner.run("set", "server.network-config.network-listeners." +
					"network-listener.http-listener-1.thread-pool=thread-pool-1");
			/*if(commandResult.getExitStatus() != CommandResult.ExitStatus.SUCCESS) {
				;
			}*/
		} 
		catch (GlassFishException e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
					"Exception: thrown when configuring and starting embedded server");
			e.printStackTrace();
		}
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

	//TODO NEED A WAY TO BE ABLE TO DO THE FOLLOWING CODE TO SHUT DOWN GLASSFISH SERVER IF THE SERVER APPLICATION IS DESTROYED
	// Undeploy the application from the embedded server
	/*deployer.undeploy(deployedApp);
	// Stop GlassFish
	embeddedServer.stop();
	// Dispose of GlassFish
	embeddedServer.dispose();*/

	/**
	 * Obtain the deployed app on the server
	 */
	public String getDeployedApp() {
		return this.deployedApp;
	}

	/**
	 * Obtain the embedded server
	 */
	// Maybe return interface that only allows the calls: Destroy destroyGlassFish
	public GlassFish getEmbeddedServer() {
		return this.embeddedServer; //TODO USE THIS FOR NOW
	}
}
