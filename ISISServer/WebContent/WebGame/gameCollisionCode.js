/* Note : Most variables have already been declared in Andrew's & Lau's files */

// Boundaries of game view
viewWidthX0 = 0;
viewWidthX1 = 1000;
viewHeightY0 = 0;
viewHeightY1 = 500;


// Flags to mark when to disallow movement outside the game view
var disallowUp = false;
var disallowDown = false;
var disallowLeft = false;
var disallowRight = false;

/* Return true if a minion enemy and the player collided
*/
function minionEnemyAndPlayerCollided() {
	/* If x and y coordinates of both objects match or
	boundaries of objects overlap, return true */
	for(var i = 0; i < MinionArray.length; i++) {
		if((MinionArray[i] != null) && (MinionArray[i].x == heliSprite.x) && (MinionArray[i].y == heliSprite.y)) {
			return true;
		}
	}
	return false;
}

/* Return true if the boss enemy and the player collided
*/
function bossEnemyAndPlayerCollided() {
	/* If x and y coordinates of both objects match or
	boundaries of objects overlap, return true */
	if((lastBoss.x == heliSprite.x) && (lastBoss.y == heliSprite.y)) {
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
			if((bulletArray[i] != null) && (bulletArray[i].x == MinionArray[j].x) && (bulletArray[i].y == MinionArray[j].y)) {
				return MinionArray[j];
			}
		}
	}
	return null;
}

/* Return true if a player bullet and the boss enemy collided
*/
function playerBulletAndBossEnemyCollided() {
	/* If x and y coordinates of both objects match or
	boundaries of objects overlap, return true */
	for(var i = 0; i < bulletArray.length; i++) {
		if((bulletArray[i].x == lastBoss.x) && (bulletArray[i].y == lastBoss.y)) {
			return true;
		}
	}
	return false;
}

/* Return true if a player collided with the game screen boundary
*/
function playerCollidedWithWall() {
	/* If x and y coordinates of both objects match or
	boundaries of objects overlap, return true */
	if((heliSprite.x == viewWidthX0) || (heliSprite.x == viewWidthX1) || 
		(heliSprite.y == viewHeightY0) || (heliSprite.y == viewHeightY1)) {
		return true;
	}
	return false;
}

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
		lifeBar.lives--;
	}
	if(playerBulletAndBossEnemyCollided()) {
		// Weaken boss enemy and increase player's score
		lastBoss.life--;
		score.total++;
	}
	if(bossEnemyAndPlayerCollided()) {
		// Weaken player (let boss keep going)
		lifeBar.lives--;
	}
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

		if(heliSprite.x == viewWidthX1) {
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

		if(heliSprite.y == viewHeightY1) {
			// Disallow down movements
			disallowDown = true;
		}
		else {
			disallowDown = false;
		}
	}
}