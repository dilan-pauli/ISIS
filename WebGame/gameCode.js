/*
 * These variables are used to get the document and context
 */
var troll;
var ctx;
var animRdy = true;

// counter to spawn the bullet. 
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
  //**************************************End Game Objects************************************/

//**************************************Start Game Code************************************/
function initialize(){
    troll = document.getElementById('canvas');
    ctx = troll.getContext("2d");
    initAnim();
    draw();
    moveHeli();
    TimetoSpawnMinion();
  //  spawnMinion();
    moveMinion();
    removeMinion();
  //spawn/delete Bullets  
    bulletShot();
    deleteBullet();
    
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
    if(40 in keyArray){heliSprite.y++} //down key
    if(38 in keyArray){heliSprite.y--}//up key
    if(39 in keyArray){heliSprite.x++}//right arrow
    if(37 in keyArray){heliSprite.x--}//left arrow
}

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
        return Math.floor((Math.random()*380)+1);
}

//***********************************End Minion Code**************************/
/*
 * This adds the key being pressed into the key array with its associated code
 */
window.onkeydown= function(event){
    keyArray[event.keyCode] = true;
}

var pew = new Audio();
pew.src = 'pew.wav';
window.onkeypress = function(event){
    if(event.keyCode == 32){
        createBullet();
        pew.play();
    }
}
/*
 * Delete the key in the array once the key is lifted up
 */
window.onkeyup= function(event){
    delete keyArray[event.keyCode];
}
//**************************************End Game Code*********************************/
//***********************************Initialization Code******************************/

/*
 *  This function is run once and then done. It initializes the images
 */

var song = new Audio();
song.src = 'helicopterttroll.wav';
function initAnim(){
if(animRdy == true){
    for(var i = 0; i <= 28; i++){
        frameArray[i] = (new Image());
        frameArray[i].src = 'background/background('+i+').jpg';
    }
  animRdy = false;
 }
 song.play();
 
}

    
//***********************************End Initialization Code***************************/