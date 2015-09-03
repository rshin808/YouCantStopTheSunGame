/**
 * YouCantStopTheSunGame
 * @author Reed Shinsato
 */

/**
 * IMAGES
 * rainbow-nebula.jpg can be found at phandroid.com/2014/05/10/android-wallpaper-space/
 * Sun-icon.png can be found at http://www.iconarchive.com/show/solar-system-icons-by-dan-wiersma/Sun-icon.html
 */

/** Libraries */
import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main Driver.
 */
public class YouCantStopTheSunGame
{
  int XBOARD = 800;
  int YBOARD = 600;
  int WPADDLE = 20;
  double VPADDLE = 2;
  int HPADDLE = 150;
  int DBALL = 40;
  int ABALL = 3;
  int SPACER = 2;
  double FRICTION = 0.1;
  boolean contactLeft = false;
  boolean contactRight = false;
  double increasePad = VPADDLE;
  double rotBallVelocity;
  Color p = new Color(1f, 0f, 0f, 0.0001f);
  int TIME = 100;
  EZImage EZBall;
  EZImage EZPaddle1;
  EZImage EZPaddle2;
  
  /**Contains the greeting text.*/
  EZText greetingText;
  
  //EZRectangle variables for the player paddles.
  EZRectangle p1Paddle, p2Paddle;
  
  //EZRectangle for the ball and two ints to control x and y movement.
  EZCircle ball;
  Random randomGenerator = new Random();
  int acceleration = ABALL;
  double xBallVelocity, yBallVelocity;
  
  //EZText to show the score and two ints to track the score.
  EZText score, score1, score2;
  int p1Score, p2Score;
  
  /**All programs start in main.*/
  public static void main(String[] args) {
    YouCantStopTheSunGame game = new YouCantStopTheSunGame();
    game.run();
  }
  
  class TimerRotate extends TimerTask{
	  @Override
	  public void run(){
		  rotate();
	  }
  }
  
  public void restartGame(){
	    //Circle using EZ assigning it to the ball variable and initialize the velocity variables.
	    int xrandomInteger = 0;
	    int yrandomInteger = 0;
	    int rotrandomInteger = 0;
	    xBallVelocity = xrandomInteger;
	    yBallVelocity = yrandomInteger;
	    rotBallVelocity = rotrandomInteger;
	    p1Paddle.translateTo(WPADDLE, (YBOARD / 2));
	    p2Paddle.translateTo((XBOARD - WPADDLE), (YBOARD / 2));
	    while(Math.abs(xBallVelocity) < (VPADDLE + 1)){
	    	xrandomInteger = randomGenerator.nextInt(2 * acceleration + 1);
	    	xBallVelocity = acceleration - xrandomInteger;
	    }

	    while(Math.abs(yBallVelocity) < (VPADDLE + 1)){
	    	yrandomInteger = randomGenerator.nextInt(2 * acceleration + 1);
	        yBallVelocity = acceleration - yrandomInteger;
	    }
	    while(Math.abs(rotBallVelocity) < 5){
	    	rotrandomInteger = randomGenerator.nextInt(30);
	    	rotBallVelocity = (double)rotrandomInteger - 15.0;;
	    }
	    increasePad = VPADDLE;
  }
  /**
   * Run the program.
   */
  public void run() {
    EZ.initialize(XBOARD, YBOARD); // Starts as 800x600 sized window.
    EZ.addImage("rainbow-nebula.jpg", 0, 0); // Set Background image
    
    initializeVariables(); //After window setup, initialize game variables.
    Timer timer = new Timer();
    timer.schedule(new TimerRotate(), 0, TIME);

    while(true){
      EZ.refreshScreen(); //Redraws the visual elements on the window. Without this call there will be no visual changes.
      
      updatePaddlePosition(); //Update the paddles based upon key status.
      
      updateBallPosition(); //Updates the ball.
            
      bounceBallOffPaddleIfTouching(); //detects collision between ball and paddle, applies rebound.
      
    } //end updater loop
  } //end run method
 
  public void initializeVariables() {
    greetingText = EZ.addText(EZ.getWindowWidth()/2, 20, "You Can't Stop The Sun!", Color.WHITE, 20);
    score = EZ.addText(EZ.getWindowWidth() / 2, 50, "::Score::", Color.WHITE, 40);
    score1 = EZ.addText(EZ.getWindowWidth() / 2 - 100, 50, Integer.toString(p1Score), Color.WHITE, 40);
    score2 = EZ.addText(EZ.getWindowWidth() / 2 + 100, 50, Integer.toString(p2Score), Color.WHITE, 40);
    //Rectangles using EZ assigning them to the paddle variables.
    p1Paddle = EZ.addRectangle(WPADDLE, (YBOARD / 2), WPADDLE, HPADDLE, p, true);
    EZPaddle1 = EZ.addImage("Paddle.png", p1Paddle.getXCenter(), p1Paddle.getYCenter());
    EZPaddle1.scaleBy(0.2);
    p2Paddle = EZ.addRectangle((XBOARD - WPADDLE), (YBOARD / 2), WPADDLE, HPADDLE, p, true);
    EZPaddle2 = EZ.addImage("Paddle.png", p2Paddle.getXCenter(), p2Paddle.getYCenter());
    EZPaddle2.scaleBy(0.2);
    
    //Initialize the scores to 0, then create the text using EZ and assign them to corresponding variables.
    ball = EZ.addCircle((XBOARD / 2), (YBOARD / 2), DBALL, DBALL, p, true);
    EZBall = EZ.addImage("Sun-icon.png", ball.getXCenter(), ball.getYCenter());
    EZBall.scaleBy(0.2);
    restartGame();
    p1Score = 0;
    p2Score = 0;
  } //end initializeVariables method
  
  public void rotate(){
	  EZBall.rotateBy(rotBallVelocity);
  }
  
  /**Updates the position of the paddles based upon key status.
   * */
  public void updatePaddlePosition() {
    //Check for key status and move the left paddle.
	if(!contactLeft){
		if(EZInteraction.isKeyDown('w')){
			p1Paddle.translateBy(0, -increasePad);
    	}
    	if(EZInteraction.isKeyDown('s')){
    		p1Paddle.translateBy(0, increasePad);
    	}
    	EZPaddle1.translateTo(p1Paddle.getXCenter(), p1Paddle.getYCenter());
	}
    //Check for key status and move the right paddle.
	if(!contactRight){
	    if(EZInteraction.isKeyDown('i')){
	    	p2Paddle.translateBy(0, -increasePad);
	    }
	    if(EZInteraction.isKeyDown('k')){
	    	p2Paddle.translateBy(0, increasePad);
	    }
	    EZPaddle2.translateTo(p2Paddle.getXCenter(), p2Paddle.getYCenter());
	}
    //Keep both paddles on the window by checking their center y coordinate.
	
    if((p1Paddle.getYCenter() - (HPADDLE / 2) - SPACER) < 0){
    	p1Paddle.translateTo(p1Paddle.getXCenter(), ((HPADDLE / 2) + SPACER));
    }
    if((p1Paddle.getYCenter() + (HPADDLE / 2) + SPACER) > YBOARD){
    	p1Paddle.translateTo(p1Paddle.getXCenter(), (YBOARD - (HPADDLE / 2) - SPACER));
    }
    if((p2Paddle.getYCenter() - (HPADDLE / 2) - SPACER) < 0){
    	p2Paddle.translateTo(p2Paddle.getXCenter(), ((HPADDLE / 2) + SPACER));
    }
    if((p2Paddle.getYCenter() + (HPADDLE / 2) + SPACER) > YBOARD){
    	p2Paddle.translateTo(p2Paddle.getXCenter(), (YBOARD - (HPADDLE / 2) - SPACER));
    }
  } //end updatePaddlePosition method
  
  /**Updates the position of the ball based upon velocity.
   * */
  public void updateBallPosition() {
    //Step 2C - Update the ball position
    ball.translateBy(xBallVelocity, yBallVelocity);
    EZBall.translateTo(ball.getXCenter(), ball.getYCenter());
    //Step 2D - Bounce the ball off the sides of the window.
    if((ball.getYCenter() - (DBALL / 2) - 1) < 0){ //Top
    	ball.translateTo(ball.getXCenter(), ((DBALL / 2) + 1));
    	yBallVelocity *= -1;
    	double friction = Math.abs(rotBallVelocity) * FRICTION;
    	if(rotBallVelocity < 0){
    		if(xBallVelocity < 0){
    			xBallVelocity += (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity += (int)friction;
                }
    		}
    		else{
    			xBallVelocity += (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity += (int)friction;
                }
    		}
    		rotBallVelocity += (int)friction;
    	}
    	else{
    		if(xBallVelocity < 0){
    			xBallVelocity -= (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity -= (int)friction;
                }
    		}
    		else{
    			xBallVelocity -= (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity -= (int)friction;
                }
    		}
    		rotBallVelocity -= (int)friction;
    	}

    }
    if((ball.getYCenter() + (DBALL / 2) + 1) > YBOARD){//Bot
    	ball.translateTo(ball.getXCenter(), (YBOARD - (DBALL / 2)) - 1);
    	yBallVelocity *= -1;
    	double friction = Math.abs(rotBallVelocity) * FRICTION;
    	if(rotBallVelocity < 0){
    		if(xBallVelocity < 0){
    			xBallVelocity -= (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity -= (int)friction;
                }
    		}
    		else{
    			xBallVelocity -= (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity -= (int)friction;
                }
    		}
    		rotBallVelocity += (int)friction;

    	}
    	else{
    		if(xBallVelocity < 0){
    			xBallVelocity += (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity += (int)friction;
                }
    		}
    		else{
    			xBallVelocity += (int)friction;
                if(xBallVelocity == 0){
                	xBallVelocity += (int)friction;
                }
    		}
    		rotBallVelocity -= (int)friction;
    	}
    }
    if((ball.getXCenter() - (DBALL / 2) - 1) < 0){
    	restartGame();
    	ball.translateTo((XBOARD / 2), (YBOARD / 2));
    	p2Score++;
    	score2.setMsg(Integer.toString(p2Score));
    }
    if((ball.getXCenter() + (DBALL / 2) + 1) > XBOARD){
    	ball.translateTo((XBOARD / 2), (YBOARD / 2));
    	restartGame();
    	p1Score++;
    	score1.setMsg(Integer.toString(p1Score));
    }
  } //end updateBallPosition  
  
  /**This will first check if a ball is touching a paddle, then bounce off of the paddle if it was.
   * */
  public int [][] getBallPoints(){
	    // Return all the ball points
		double degree = 45.0;
		int c = ball.getWidth() / 2;
		int a = (int)(Math.cos(degree) * c);
		int b = (int)(Math.sin(degree) * c);
		int x = ball.getXCenter();
		int y = ball.getYCenter();
		int [][] ballPoints;
		ballPoints = new int[8][8];
		ballPoints[0][0] = x-a;
		ballPoints[0][1] = y-b;
		ballPoints[1][0] = x+a;
		ballPoints[1][1] = y-b;
		ballPoints[2][0] = x-a;
		ballPoints[2][1] = y+b;
		ballPoints[3][0] = x+a;
		ballPoints[3][1] = y+b;
		ballPoints[4][0] = x-c;
		ballPoints[4][1] = y;
		ballPoints[5][0] = x+c;
		ballPoints[5][1] = y;
		ballPoints[6][0] = x;
		ballPoints[6][1] = y-c;
		ballPoints[7][0] = x;
		ballPoints[7][1] = y+c;
		return ballPoints;
  }
  public void bounceBallOffPaddleIfTouching() {
    //Check for ball and paddle collision.
	int i;
	int [][] ballPoints;
	ballPoints = getBallPoints();
	if (ball.getXCenter() <= (XBOARD / 2)){
		for(i =0;i<8;i++){
			if(p1Paddle.isPointInElement(ballPoints[i][0], ballPoints[i][1])){
				contactLeft = true;
				ball.translateBy(-xBallVelocity, -yBallVelocity);
				increasePad += 1;
				increaseXBallVelocity();
				xBallVelocity *= -1;
				if((ball.getXCenter() - ball.getWidth()/2) < (p1Paddle.getXCenter() + p1Paddle.getWidth() / 2)){
					yBallVelocity *= -1;
				}
				break;
			}
			else{
				contactLeft = false;
			}
		}
	}
	else{
		for(i =0;i<8;i++){
			if(p2Paddle.isPointInElement(ballPoints[i][0], ballPoints[i][1])){
				contactRight = true;
				ball.translateBy(-xBallVelocity, -yBallVelocity);
				increasePad += 1;
				increaseXBallVelocity();
				xBallVelocity *= -1;
				if((ball.getXCenter() + ball.getWidth()/2) > (p2Paddle.getXCenter() - p2Paddle.getWidth() / 2)){
					yBallVelocity *= -1;
				}
				break;
			}
			else{
				contactRight = false;
			}
		}
	}
  } //end bounceBallOffPaddleIfTouching

  public void increaseXBallVelocity(){
	  if(xBallVelocity < 0){
		  xBallVelocity += -1;
	  }
	  else{
		  xBallVelocity += 1;
	  }
  }
  public void checkInPaddleArea(EZRectangle paddle, int [] points){
	  for(int i = 0; i < points.length; i++){
		  print("hi");
	  }
  }
  public void print(String s){
	  System.out.println(s);
  }
} //end class
