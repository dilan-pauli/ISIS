/*
 * These variables are used to get the document and context
 */
var troll;
var ctx;
var animRdy = true;

//counter to spawn the bullet. 
var count = 0; 

//an array that will hold the keys being pressed at that time
var keyArray= {};
var bulletArray = new Array();
var frameArray = new Array();
var MinionArray=new Array(); 

//************************************Start Image Objects***************************************/

/*
 * Main character sprite
 */
var heliRdy = false;
var heli = new Image();
heli.onload = function(){
	heliRdy = true;
};
heli.src = 'helicoptersmall.png';

/*
 * Main enemy Sprite
 */
var enemyRdy = false;
var enemy = new Image();
enemy.onload = function(){
	enemyRdy = true;
};
enemy.src = 'cartoon_bullet_nowind_small.png';

/*
 * The last boss Sprite
 */
var bossRdy = false;
var boss = new Image();
boss.onload = function(){
	bossRdy = true;
};
boss.src = 'boss_bullet.png';

/*
 * The model for each bullet being shot
 */
var bulletRdy = false;
var bullet = new Image();
bullet.onload = function(){
	bulletRdy = true;
};
bullet.src = 'bullet.png';


//**************************************End Image Objects***********************************/
//**************************************Start Game Objects**********************************/
/*
 * An object class to control the Main character
 */
var heliSprite = new Object(); 
heliSprite.x = 0;
heliSprite.y = 250;
heliSprite.gun = 5;

/*
 * An object class to control the Lifebar
 */
var lifeBar = new Object();
lifeBar.x = 0;
lifeBar.y = 0;
lifeBar.lives = 5;

/*
 * An object class to control the score
 * Will just be a simple string and an incrementing number
 */
var score = new Object();
score.x = 0;
score.y = 0;
score.total = 0;

/*
 * An object class to control the bullets
 * Note: How will we control this? Should we draw multiple bullets
 * and have new bullet objects created for each bullet shot?
 */
function gunShot(x,y,power)
{
	this.x = x;
	this.y = y;
	this.power = power;
}


function minionShot(x,y)
{
	this.x = x;
	this.y = y;
	//////
	this.life = 1;
}
/*
 * An object class to control the enemy minions
 * Starting position, direction, speed and life will need to be tracked
 */
var enemyMinion = new Object();
enemyMinion.x = 250;
enemyMinion.y = 1000;
enemyMinion.direction = "left";
enemyMinion.life = 3;
enemyMinion.damage = 1;

/*
 * An object class to control the last boss
 */
var lastBoss = new Object();
lastBoss.x = 0;
lastBoss.y = 0;
lastBoss.direction = "left";
lastBoss.life = 100;
lastBoss.damage = 2;
//lastBoss.frequency = 10;



//Flags to mark when to disallow movement outside the game view
var disallowUp = false;
var disallowDown = false;
var disallowLeft = false;
var disallowRight = false;
//**************************************End Game Objects************************************/

//**************************************Start Game Code************************************/
function initialize(){
	troll = document.getElementById('canvas');
	ctx = troll.getContext("2d");
	initAnim();
	draw();
	receiveInfoFromISISQueue(); //TODO: RECEIVE INCOMING WEBSOCKET MESSAGE
	moveHeli();
	TimetoSpawnMinion();
	//  spawnMinion();
	moveMinion();
	removeMinion();
	//spawn/delete Bullets  
	bulletShot();
	deleteBullet();
	////////////CHECK FOR COLLISIONS
	checkForAndHandleCollisions();
}

/*
 *  The draw function is in charge of drawing the images on the screen every 
 *  milisecond for use in the game. Its associated variables are 
 */
var frameCount = 0, index = 0;
function draw(){

	if (frameCount != 10){
		ctx.drawImage(frameArray[index], 0, 0);
		frameCount ++;
	}
	else{
		index ++;
		frameCount = 0;
		if(index == 29){
			index =0 ;
		}
	}
	if(heliRdy == true){
		ctx.drawImage(heli, heliSprite.x, heliSprite.y);
	}

	if(enemyRdy == true){
		ctx.drawImage(enemy, enemyMinion.x, enemyMinion.y); // first bullet always spawn in the middle
	}

	ctx.font="30px Arial";
	ctx.strokeText(score.total,950,460);
}

/*
 * A function that will move the car when a key is pressed
 */
function moveHeli(){
	if((40 in keyArray) && (disallowDown == false)){heliSprite.y++; } //down key
	if((38 in keyArray) && (disallowUp == false)){heliSprite.y--; } //up key
	if((39 in keyArray) && (disallowRight == false)){heliSprite.x++; } //right arrow
	if((37 in keyArray) && (disallowLeft == false)){heliSprite.x--; } //left arrow
}


/***************************************ISIS Signal Use Code***************************/
//TODO: ISIS SIGNAL USE CODE
/*
 * Buffered array of incoming ISIS signals
 */
var incomingSignals = new Array();

/*
 * Get info from incoming array of ISIS info. Could use this instead
 * of keyboard
 * (Could get the user to specify the type of input to use later)
 */
function receiveInfoFromISISQueue() {
	// Received message
	var msgStr = "";
	var msgJSON = null;
	// Get signal information if the array of incoming info is not empty
	if(incomingSignals.length > 0) {
		// Reverse the order of the elements in the array so earlier items
		// become later items
		incomingSignals.reverse();
		// Remove the last item (note: pop removes the last item in the array) 
		msgStr = incomingSignals.pop();
		// Reverse the order of the array again to ensure things get back in order
		incomingSignals.reverse();
		// Convert the request to JSON Object
		msgJSON = JSON.parse(msgStr);

		// Process the request
		if(msgJSON.ResponseCode === "BUTTON_EVENT") {
			console.log('Time: ' + new Date().toString() + ", Received BUTTON_EVENT");
			// Obtain the array of Object info (JSON form)
			var msgDataObject = msgJSON.Object;
			console.log('Time: ' + new Date().toString() + ", Received Msg Data Object string: " + msgDataObject);
			var buttonArrayJSON = msgDataObject.DataArray;
			console.log('Time: ' + new Date().toString() + ", Received Array in JSON: " + buttonArrayJSON);

			//TODO: Loop through the data and add keys to the key array for the appropriate keys (UP,DN,LT,RT,CNTR)
			// Handle key down events
			if(buttonArrayJSON[0] == true) {
				// UP
				keyArray[38] = true;
				console.log("UP BUTTON PRESS EVENT DETECTED FROM CONTROLLER");
			}
			if(buttonArrayJSON[1] == true) {
				// DOWN
				keyArray[40] = true;
				console.log("DOWN BUTTON PRESS EVENT DETECTED FROM CONTROLLER");
			}
			if(buttonArrayJSON[2] == true) {
				// LEFT
				keyArray[37] = true;
				console.log("LEFT BUTTON PRESS EVENT DETECTED FROM CONTROLLER");
			}
			if(buttonArrayJSON[3] == true) {
				// RIGHT
				keyArray[39] = true;
				console.log("RIGHT BUTTON PRESS EVENT DETECTED FROM CONTROLLER");
			}
			if(buttonArrayJSON[4] == true) {
				// CENTER
				createBullet();
				console.log("CENTER BUTTON PRESS EVENT DETECTED FROM CONTROLLER");
			}

			// Handle all key release events (key up)
			if((buttonArrayJSON[0] == false) || (buttonArrayJSON[1] == false) ||
					(buttonArrayJSON[2] == false) || (buttonArrayJSON[3] == false) ||
					(buttonArrayJSON[4] == false)) {

				if(buttonArrayJSON[0] == false) {
					delete keyArray[38];
					console.log("UP BUTTON RELEASE EVENT DETECTED FROM CONTROLLER");
				}
				if(buttonArrayJSON[1] == false) {
					delete keyArray[40];
					console.log("DOWN BUTTON RELEASE EVENT DETECTED FROM CONTROLLER");
				}
				if(buttonArrayJSON[2] == false) {
					delete keyArray[37];
					console.log("LEFT BUTTON RELEASE EVENT DETECTED FROM CONTROLLER");
				}
				if(buttonArrayJSON[3] == false) {
					delete keyArray[39];
					console.log("RIGHT BUTTON RELEASE EVENT DETECTED FROM CONTROLLER");
				}
				return;
			}
		}
		/*else {
			console.log('Time: ' + new Date().toString() + "Received unsupported event");
		}*/
	}
}

/*
 * Set up for WebSocket server
 */
//TODO: WEBSOCKET-CONTROLLER INTEGRATION

//URI for WebSocket application
var wsURI = "ws://" + location.host + "/ISISServer/WebGame/index.html";
//WebSocket connection between this client and the server
var websocket = null;

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
		console.log('Time: ' + new Date().toString() + 
		' WARNING: NEED TO CONNECT TO WEBSOCKET SERVER BEFORE MESSAGES CAN BE SENT');
		return;
	}
}

//WebSocket event handler functions
function onOpen(event) {
	console.log('Time: ' + new Date().toString() + ' INFO: CONNECTED TO WEBSOCKET SERVER');
}

function onClose(event) {
	console.log('Time: ' + new Date().toString() + ' INFO: DISCONNECTED FROM WEBSOCKET SERVER');
	websocket = null;
}

function onError(event) {
	console.log('Time: ' + new Date().toString() + ' ERROR: ' + event.data);
}

function onMessage(event) {
	console.log('Time: ' + new Date().toString() + ' INFO: SERVER RESPONSE: ' + event.data);
	incomingSignals.push(event.data); //TODO: Push incoming signals onto queue
}

/***************************************ISIS Signal Use Code***************************/




/***************************************Bullet Code***************************/
function createBullet(){

	for(var i = 0; i <= 3; i++){
		if (bulletArray[i] == null){
			var index = i;
			break;
		}
		//else{
		// throw new Error("The array is full.");
		// }  
	}
	bulletArray[index] = new gunShot(heliSprite.x+115, heliSprite.y, 5);

}

function deleteBullet(){
	for(var i=0; i <= bulletArray.length; i++){
		if(bulletArray[i] != null && bulletArray[i].x >= 1000){
			bulletArray[i]= null;
		}
	}
}

function bulletShot(){
	for(var i=0; i <= bulletArray.length; i++){
		if(bulletArray[i]!= null)
			ctx.drawImage(bullet, bulletArray[i].x= bulletArray[i].x + 1.8, bulletArray[i].y);
	}
}
/************************************End Bullet Code **************************/
//***********************************Minion Code******************************/
/*
 *	a function that will spawn the enemy bullet. 
 */
function spawnMinion(){
	for (var i = 0; i <=5 ; i++)
	{
		if(MinionArray[i] == null)
		{
			var index = i;
			break;
		}
	}
	MinionArray[index] = new minionShot(1000,randomizeMinion());
}

function removeMinion()
{
	for (var i = 0; i <= MinionArray.length; i++)
	{
		if (MinionArray[i] != null && MinionArray[i].x <=0)
		{
			MinionArray[i] = null;   
		}
	}
}

/*
 *  a function that will detect and spawn Minion every 250ms. 
 */
function TimetoSpawnMinion()
{
	if (count >= 250)
	{
		spawnMinion();
		count = 0;
	}
}

/*
 *	a function that will move the minion to the left.
 */
function moveMinion(){
	count++;
	for (var i = 0; i <= MinionArray.length; i++)
	{
		if (MinionArray[i] != null)
			ctx.drawImage(enemy, MinionArray[i].x--, MinionArray[i].y);
	}
}
/*
 *	a function that will randomize the minion's y location.
 */
function randomizeMinion(){
	//enemyMinion.y = Math.floor((Math.random()*500)+1);
	return Math.floor((Math.random()*380)+1);
}

//***********************************End Minion Code**************************/
/*
 * This adds the key being pressed into the key array with its associated code
 */
window.onkeydown= function(event){
	keyArray[event.keyCode] = true;
};

var pew = new Audio();
pew.src = 'pew.wav';
window.onkeypress = function(event){
	if(event.keyCode == 32){
		createBullet();
		pew.play();
	}
};
/*
 * Delete the key in the array once the key is lifted up
 */
window.onkeyup= function(event){
	delete keyArray[event.keyCode];
};
//**************************************End Game Code*********************************/
//***********************************Initialization Code******************************/

/*
 *  This function is run once and then done. It initializes the images
 */

var song = new Audio();
song.src = 'helicopterttroll.wav';
function initAnim(){
	if(animRdy == true){
		connect(); //TODO: CONNECT TO WEBSOCKET SERVER

		for(var i = 0; i <= 28; i++){
			frameArray[i] = (new Image());
			frameArray[i].src = 'background/background('+i+').jpg';
		}
		animRdy = false;
	}
	song.play();

}


//***********************************End Initialization Code***************************/








/*------------------------------COLLISION DETECTION CODE-------------------------------*/


/*---------------------------------------------------------------------------*/
/* Note : Most variables have already been declared in Andrew's & Lau's files */

//Boundaries of game view
var viewWidthX0 = 0;
var viewWidthX1 = 1000;
var viewHeightY0 = 0;
var viewHeightY1 = 500;

/* Data structure for rectangle (only need to specify two corners)
- (leftX, topY) specifies left corner
- (rightX, bottomY) specifies right corner
 */
function rectangle(leftX, rightX, topY, bottomY) {
	this.left = leftX;
	this.right = rightX;
	this.top = topY;
	this.bottom = bottomY;
};

/* Detect whether two rectangles overlap
Return true if two rectangles overlap. Return false otherwise
Input: two rectangles
 */
function intersectRect(r1, r2) {
	return !((r2.left > r1.right) || (r2.right < r1.left) || (r2.top > r1.bottom) || (r2.bottom < r1.top));
}


/*---------------------------------------------------------------------------*/
/* HELPER FUNCTION
Detect whether minion enemy bounding box coincides with that of the
player's bounding box
Returns true if minion enemy and player bounds coincide 
 */
function minionEnemyAndPlayerBoundsCoincide(minionIndex) {
	if(MinionArray[minionIndex] == null) {
		return false;
	}
	/*if(((MinionArray[i].x + 31) <= (heliSprite.x + 150)) || 
    ((MinionArray[i].y + 23) <= (heliSprite.y + 46))) {
    //console.log("");
    return true;
  }*/
	// Create two rectangles
	var minionRect = new rectangle(MinionArray[minionIndex].x,
			MinionArray[minionIndex].x + 31,
			MinionArray[minionIndex].y,
			MinionArray[minionIndex].y + 23);
	var playerRect = new rectangle(heliSprite.x, heliSprite.x + 150, 
			heliSprite.y, heliSprite.y + 46);

	if(intersectRect(minionRect, playerRect) == true) {
		console.log("minionEnemyAndPlayerBoundsCoincide() function returns true");
		return true;
	}
	return false;
}

/* Return true if a minion enemy and the player collided
 */
function minionEnemyAndPlayerCollided() {
	/* If x and y coordinates of both objects match or
  boundaries of objects overlap, return true */
	for(var i = 0; i < MinionArray.length; i++) {
		/*if((MinionArray[i] != null) && (MinionArray[i].x == heliSprite.x) && (MinionArray[i].y == heliSprite.y)) {
      return true;
    }*/
		if(minionEnemyAndPlayerBoundsCoincide(i) == true) {
			console.log("Minion enemy and player collided");
			return true;
		}
	}
	return false;
}
/*---------------------------------------------------------------------------*/
/* Return true if the boss enemy and the player collided
 */
function bossEnemyAndPlayerCollided() {
	/* If x and y coordinates of both objects match or
  boundaries of objects overlap, return true */
	/*if((lastBoss.x == heliSprite.x) && (lastBoss.y == heliSprite.y)) {
    return true;
  }*/
	// Create two rectangles
	var bossRect = new rectangle(lastBoss.x, lastBoss.x + 315, 
			lastBoss.y, lastBoss.y + 197);
	var playerRect = new rectangle(heliSprite.x, heliSprite.x + 150, 
			heliSprite.y, heliSprite.y + 46);

	if(intersectRect(bossRect, playerRect) == true) {
		console.log("Boss enemy and player collided");
		return true;
	}
	return false;
}
/*---------------------------------------------------------------------------*/
/* HELPER FUNCTION
Detect whether player bullet bounding box coincides with that of a
minion enemy's bounding box
Returns true player's bullet's bounds and enemy's bounds coincide 
 */
function playerBulletAndMinionEnemyBoundsCoincide(bulletIndex, minionIndex) {
	if((bulletArray[bulletIndex] == null) || (MinionArray[minionIndex] == null)) {
		return false;
	}

	// Create two rectangles
	var playerBulletRect = new rectangle(bulletArray[bulletIndex].x, bulletArray[bulletIndex].x + 34, 
			bulletArray[bulletIndex].y, bulletArray[bulletIndex].y + 13);
	var minionBulletRect = new rectangle(MinionArray[minionIndex].x, MinionArray[minionIndex].x + 31,
			MinionArray[minionIndex].y, MinionArray[minionIndex].y + 23);

	if(intersectRect(minionBulletRect, playerBulletRect) == true) {
		console.log("playerBulletAndMinionEnemyBoundsCollide() function returns true");
		return true;
	}
	return false;
}

/* Return true if a player bullet and a minion enemy collided
Return the colliding enemy if a player bullet and a minion enemy collided and null otherwise
 */
function playerBulletAndMinionEnemyCollided() {
	/* If x and y coordinates of both objects match or
  boundaries of objects overlap, return true */
	for(var i = 0; i < bulletArray.length; i++) {
		for(var j = 0; j < MinionArray.length; j++) {
			/*if((bulletArray[i] != null) && (MinionArray[j] != null) && (bulletArray[i].x == MinionArray[j].x) && (bulletArray[i].y == MinionArray[j].y)) {
        return MinionArray[j];
      }*/
			if(playerBulletAndMinionEnemyBoundsCoincide(i, j) == true) {
				console.log("Player bullet hit minion enemy");
				return true;
			}
		}
	}
	return null;
}
/*---------------------------------------------------------------------------*/
/* HELPER FUNCTION
Detect whether player bullet bounding box coincides with that of the
boss enemy's bounding box
Returns true player's bullet's bounds and boss's bounds coincide 
 */
function playerBulletAndBossEnemyBoundsCoincide() {
	if(MinionArray[minionIndex] == null) {
		return false;
	}
	/*// Create two rectangles
  var minionRect = new rectangle(MinionArray[minionIndex].x,
                                  MinionArray[minionIndex].x + 31,
                                  MinionArray[minionIndex].y,
                                  MinionArray[minionIndex].y + 23);
  var playerRect = new rectangle(heliSprite.x, heliSprite.x + 150, 
                                  heliSprite.y, heliSprite.y + 46);

  if(intersectRect(minionRect, playerRect) == true) {
    console.log("minionEnemyAndPlayerBoundsCoincide() function returns true");
    return true
  }*/
	return false;
}
/*---------------------------------------------------------------------------*/
/* Return true if a player bullet and the boss enemy collided
 */
function playerBulletAndBossEnemyCollided() {
	/* If x and y coordinates of both objects match or
  boundaries of objects overlap, return true */
	for(var i = 0; i < bulletArray.length; i++) {
		if((bulletArray[i] != null) && (bulletArray[i].x == lastBoss.x) && (bulletArray[i].y == lastBoss.y)) {
			return true;
		}
	}
	return false;
}
/*---------------------------------------------------------------------------*/
/* Return true if a player collided with the game screen boundary
 */
function playerCollidedWithWall() {
	/* If x and y coordinates of both objects match or
  boundaries of objects overlap, return true */
	if((heliSprite.x <= viewWidthX0) || ((heliSprite.x + 150) >= viewWidthX1) || 
			(heliSprite.y <= viewHeightY0) || ((heliSprite.y + 46) >= viewHeightY1)) {
		console.log("Player collided with wall");
		return true;
	}
	return false;
}
/*---------------------------------------------------------------------------*/
/* Check for collisions (will be periodically called within the 
game application)
 */
function checkForAndHandleCollisions() {
	// Check for player bullet collisions with minion enemies
	// Check for player collisions with minion enemies
	// Check for player bullet collisions with boss enemy
	// Check for player collisions with boss enemy
	//
	// Check for game boundary wall collisions


	var shotMinion = playerBulletAndMinionEnemyCollided();
	if(shotMinion != null) {
		// Weaken or kill minion enemy and increase player's score
		shotMinion.life--;
		score.total++;
	}
	if(minionEnemyAndPlayerCollided()) {
		// Weaken player (let minion keep going)
		console.log("Minion enemy collided with player");
		lifeBar.lives--;
	}
	/*if(playerBulletAndBossEnemyCollided()) {
    // Weaken boss enemy and increase player's score
    lastBoss.life--;
    score.total++;
  }
  if(bossEnemyAndPlayerCollided()) {
    // Weaken player (let boss keep going)
    lifeBar.lives--;
  }*/
	if(playerCollidedWithWall()) {
		// Disallow player movement in the specific direction(s) that
		// would take the player off screen

		if(heliSprite.x == viewWidthX0) {
			// Disallow left movements
			disallowLeft = true;
		}
		else {
			disallowLeft = false;
		}

		if((heliSprite.x + 150) == viewWidthX1) {
			// Disallow right movements
			disallowRight = true;
		}
		else {
			disallowRight = false;
		}

		if(heliSprite.y == viewHeightY0) {
			// Disallow up movements
			disallowUp = true;
		}
		else {
			disallowUp = false;
		}

		if((heliSprite.y + 46) == viewHeightY1) {
			// Disallow down movements
			disallowDown = true;
		}
		else {
			disallowDown = false;
		}
	}
	else {
		disallowLeft = false;
		disallowRight = false;
		disallowUp = false;
		disallowDown = false;
	}
}