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
	<button value="Send" onclick="javascript:doSend('Test Message')">Send</button>
	<button value="Close" onclick="javascript:disconnect()">Disconnect</button>

	<script type="text/javascript">
		//var wsURI = "ws://localhost:8081/ServerProcessISIS/ISISServlet";
		var wsURI = "ws://" + location.host + "${pageContext.request.contextPath}/ISIS";
		var output;
		
		function connect() {
			websocket = new WebSocket(wsURI);
			websocket.onopen = function(event) {
				onOpen(event);
			};
			websocket.close = function(event) {
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
			websocket.close;
		}
		
		function doSend(message) {
			//Ignore message ('Test Message) for now'
			/*writeToScrn("SENT: " + message);
			websocket.send(message);*/
			
			var msg = document.getElementById('message').value;
			writeToScrn("SENT: " + msg);
			websocket.send(msg);
		}
		
		function writeToScrn(message) {
			var elem = document.getElementById('output');
			elem.innerHTML = message;
		}
		
		function onOpen(event) {
			writeToScrn("CONNECTED TO WEBSOCKET SERVER");
		}
		
		function onClose(event) {
			writeToScrn("DISCONNECTED FROM WEBSOCKET SERVER");
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
