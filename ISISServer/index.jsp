<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<h1>Hello ISIS World!</h1>
	<div id="output"></div>
	<br />
	<input name="usermsg" type="text" id="message" size="63" value="Enter your message here" />
	<button onclick="javascript:connect()">Connect</button>
	<button value="Send" onclick="javascript:doSend()">Send</button>
	<button value="Close" onclick="javascript:disconnect()">Disconnect</button>

	<script type="text/javascript">
		//var wsURI = "ws://localhost:8081/ServerProcessISIS/ISISServlet";
		// Requests need to be made to URL ending in /ISIS the way the server is currently set up
		// for requests to be noticed by the ISIS Server Application
		var wsURI = "ws://" + location.host + "${pageContext.request.contextPath}/ISIS";
		// WebSocket connection between this client and the server
		var websocket = null;
		// Output field of the page for client feedback messages
		var output = null;
		// Client message typed in to send to server
		var msg = null;
		
		function connect() {
			if(websocket != null) {
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
			if(websocket == null) {
				return;
			}
			
			websocket.close();
		}
		
		function doSend() {
			if(websocket == null) {
				writeToScrn('<span style="color:red;">NEED TO CONNECT TO WEBSOCKET ' + 
				'SERVER BEFORE MESSAGES CAN BE SENT</span>');
				return;
			}
			
			msg = document.getElementById('message').value;
			writeToScrn('<span style="color:green;">SENT MESSAGE TO WEBSOCKET SERVER</span>');
			websocket.send(msg);
		}
		
		function writeToScrn(message) {
			var elem = document.getElementById('output');
			elem.innerHTML = message;
		}
		
		function onOpen(event) {
			writeToScrn('<span style="color:green;">CONNECTED TO WEBSOCKET SERVER</span>');
		}
		
		function onClose(event) {
			writeToScrn('<span style="color:red;">DISCONNECTED FROM WEBSOCKET SERVER</span>');
			websocket = null;
		}
		
		function onError(event) {
			writeToScrn('<span style="color:red;">ERROR:</span>' + event.data);
		}
		
		function onMessage(event) {
			writeToScrn('<span style="color:blue;">SERVER RESPONSE: ' + event.data + '</span>');
		}
	</script>
</body>
</html>
