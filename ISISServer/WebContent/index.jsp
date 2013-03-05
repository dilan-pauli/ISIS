<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<h1>Hello ISIS World!</h1>
	<textarea id="output" rows="50" cols="150" readonly></textarea>
	<br />
	<input name="usermsg" type="text" id="message" size="63"
		value="Enter your message here" />
	<button onclick="javascript:connect()">Connect</button>
	<button value="Send" onclick="javascript:doSend()">Send</button>
	<button value="Close" onclick="javascript:disconnect()">Disconnect</button>

	<script type="text/javascript">
		//var wsURI = "ws://localhost:8081/ServerProcessISIS/ISISServlet";
		// Requests need to be made to URL ending in /ISIS the way the server is currently set up
		// for requests to be noticed by the ISIS Server Application
		//var wsURI = "ws://" + location.host + "${pageContext.request.contextPath}/ISISServer";
		var wsURI = "ws://" + location.host + "/ISISServer";
		// WebSocket connection between this client and the server
		var websocket = null;
		// Output field of the page for client feedback messages
		var output = null;
		// Client message typed in to send to server
		var msg = null;

		function connect() {
			if (websocket != null) {
				return;
			}

			websocket = new WebSocket(wsURI);
			websocket.onopen = function(event) {
				onOpen(event);
			};
			websocket.onclose = function(event) {
				onClose(event);
			};
			websocket.onmessage = function(event) {
				onMessage(event);
			};
			websocket.onerror = function(event) {
				onError(event);
			};
		}

		function disconnect() {
			if (websocket == null) {
				return;
			}

			websocket.close();
		}

		function doSend() {
			if (websocket == null) {
				/*writeToScrn('<span style="color:red;">NEED TO CONNECT TO WEBSOCKET ' + 
				'SERVER BEFORE MESSAGES CAN BE SENT</span>');*/
				writeToScrn('Time: ' + new Date().toString + 
						' WARNING: NEED TO CONNECT TO WEBSOCKET SERVER BEFORE MESSAGES CAN BE SENT');
				return;
			}

			msg = document.getElementById('message').value;
			//writeToScrn('<span style="color:green;">SENT MESSAGE TO WEBSOCKET SERVER</span>');
			websocket.send(msg);
			writeToScrn('Time: ' + new Date().toString + ' SENT MESSAGE TO WEBSOCKET SERVER');
		}

		function writeToScrn(message) {
			var elem = document.getElementById('output');
			//elem.innerHTML = message;
			//elem.innerHTML = elem.innerHTML + message + "<br/>";
			elem.innerHTML = 'Time: ' + new Date().toString + ' ' + elem.innerHTML + message + "\n";
		}

		function onOpen(event) {
			//writeToScrn('<span style="color:green;">CONNECTED TO WEBSOCKET SERVER</span>');
			writeToScrn('Time: ' + new Date().toString
					+ ' INFO: CONNECTED TO WEBSOCKET SERVER');
		}

		function onClose(event) {
			//writeToScrn('<span style="color:red;">DISCONNECTED FROM WEBSOCKET SERVER</span>');
			writeToScrn('Time: ' + new Date().toString
					+ ' INFO: DISCONNECTED FROM WEBSOCKET SERVER');
			websocket = null;
		}

		function onError(event) {
			//writeToScrn('<span style="color:red;">ERROR:</span>' + event.data);
			writeToScrn('Time: ' + new Date().toString + ' ERROR: ' + event.data);
		}

		function onMessage(event) {
			/*writeToScrn('<span style="color:blue;">SERVER RESPONSE: '
			+ event.data + '</span>');*/
			writeToScrn('Time: ' + new Date().toString
					+ ' INFO: SERVER RESPONSE: ' + event.data);
		}
	</script>
</body>
</html>
