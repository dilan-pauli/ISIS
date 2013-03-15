/*
 * These variables are used to get the document and context
 */
var troll;
var ctx;

//an array that will hold the keys being pressed at that time
var keyArray= {};

//************************************Start Image Objects***************************************/
/*
 * Background Image
 */
var imgRdy = false;
var img = new Image();
  img.onload = function(){
    imgRdy = true;
  };
  img.src = 'background.jpg';
  
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
  bullet.src = '';
  
  
 //**************************************End Image Objects***********************************/
 //**************************************Start Game Objects**********************************/
  /*
   * An object class to control the Main character
   */
  var heliSprite = new Object(); 
  heliSprite.x = 0;
  heliSprite.y = 250;
  heliSprite.gun = 5;
  heliSprite.speed = 256;
  
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
  }
  /*
   * An object class to control the enemy minions
   * Starting position, direction, speed and life will need to be tracked
   */
  var enemyMinion = new Object();
  enemyMinion.x = 1100;
  enemyMinion.y = 260;
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
  
var MinionArray=new Array(); 
var count = 0; // counter to spawn the bullet. 
  //**************************************End Game Objects************************************/

//**************************************Start Game Code************************************/
function initialize(){
    troll = document.getElementById('canvas');
    ctx = troll.getContext("2d");
    draw();
    moveCar();
    TimetoSpawnMinion();
  //  spawnMinion();
    moveMinion();
    removeMinion();
}

function draw(){
  if(imgRdy == true){
      ctx.drawImage(img, 0, 0);
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
 * This adds the key being pressed into the key array with its associated code
 */
window.onkeydown= function(event){
    keyArray[event.keyCode] = true;
}
/*
 * Delete the key in the array once the key is lifted up
 */
window.onkeyup= function(event){
    delete keyArray[event.keyCode];
}
/*
 *	a function that will spawn the enemy bullet. 
 */
function spawnMinion(){
    for (i = 0; i <=5 ; i++)
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
    for ( i =0; i <= MinionArray.length; i++)
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
    for (i = 0; i <= MinionArray.length; i++)
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
        return Math.floor((Math.random()*500)+1);
}


/*
 * A function that will move the car when a key is pressed
 */
function moveCar(){
    if(40 in keyArray){heliSprite.y++} //down key
    if(38 in keyArray){heliSprite.y--}//up key
    if(39 in keyArray){heliSprite.x++}//right arrow
    if(37 in keyArray){heliSprite.x--}//left arrow
 
}

//**************************************End Game Code************************************/