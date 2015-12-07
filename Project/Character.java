public class Character implements Runnable, Constants{
	private String character;
	private int xPos;
	private int yPos;
    private int speed;
    private int grav;
    private int moveDir;
    private boolean move;
    private boolean life;
	Thread t;// = new Thread(this);

	public Character(String character){
		this.character = character;
		t = new Thread(this);
		t.start();
	}

	/*public Character(String character,int xPos, int yPos, int moveDir, int speed, int grav, boolean life){
		this.character = character;
		this.xPos = xPos;
		this.yPos = yPos;
		this.moveDir = moveDir;
		this.speed = speed;
		this.grav = grav;
		this.life = life;
		t = new Thread(this);
		t.start();
	}*/

	public void setCharacter(String character){
		this.character = character;
	}

	public void setxPos(int xPos){
		this.xPos = xPos;
	}

	public void setyPos(int yPos){
		this.yPos = yPos;
	}

	public void setSpeed(int speed){
		this.speed= speed;
	}

	public void setGrav(int grav){
		this.grav = grav;
	}

	public void setMoveLeft(){
		this.moveDir = LEFT;
	}

	public void setMoveRight(){
		this.moveDir = RIGHT;
	}

	public void setMove(boolean move){
		this.move = move;
	}

	public void setAlive(){
		this.life = true;
	}

	public void setDead(){
		this.life = false;
	}

	// ------------ getters --------------

	public String getCharacter(){
		return this.character;
	}

	public int getxPos(){
		return this.xPos;
	}

	public int getyPos(){
		return this.yPos;
	}

	public int getSpeed(){
		return this.speed;
	}

	public int getGrav(){
		return this.grav;
	}

	public int getMoveDir(){
		return this.moveDir;
	}

	public boolean isAlive(){
		return this.life;
	}

	public boolean isMove(){
		return this.move;
	}

	//------- movements

	public void moveRight(){
		moveDir = RIGHT;
		speed += 2;
		if(speed>10)
		    speed = 10;
		xPos+=speed;
	}

	public void moveLeft(){
		moveDir = LEFT;
		speed -= 1;
		if(speed>-10)
		    speed = -10;
		xPos+=speed;
	}

	public void moveStop(){
		speed = 0;
	}

	public void jump(){

	}

	public void run(){
		try{
			while(true){
				if(move){
					if(moveDir == LEFT){
						moveLeft();
					}

					else if(moveDir == RIGHT){
						moveRight();
					}
				}

				Thread.sleep(30); 
			}
		}catch(InterruptedException e){}
			
	}
}