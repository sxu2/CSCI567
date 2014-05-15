package csci567.speed;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {
	 MediaPlayer mp1,move;
	 gameloop gameLoopThread;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//for no title
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new GameView(this));		
	}
	
	public class GameView extends SurfaceView{
	      Bitmap bmp,pause;
	      Bitmap background,gas,note1,note2;
	      Bitmap run1;
	      Bitmap run2;
	      Bitmap run3;
	      Bitmap redcar;
	      Bitmap purplecar;
	      Bitmap bluecar;
	      Bitmap greencar;
	      Bitmap cyancar;
	      Bitmap goldcar;
	      Bitmap off,on;
	      Bitmap exit;
	      private SurfaceHolder holder;
	      //private game loop gameLoopThread;
	      private int x=0,z=0,delay=0,getx,gety,sound=1;
	      int show=0,sy,sx;
	      int cspeed=0,gspeed=0,gameover=0;
	      int score=0,health=300,reset=0;
	      int timer;
	      int pausecount=0,volume;
	      int lane=0;
	      float cur_sx;
	      Random r = new Random();
  		  int car1lane  = r.nextInt(4-1)+1;
  		  int car1color = r.nextInt(6-1)+1;
  		  int car2lane  = r.nextInt(4-1)+1;
  		  int car2color = r.nextInt(6-1)+1;
  		  int car3lane  = r.nextInt(4-1)+1;
  		  int car3color = r.nextInt(6-1)+1;
  		  int i3, i4, i5;
  		  int gaslane = r.nextInt(4-1)+1;
	      
	      
	      @SuppressLint("NewApi")
		public GameView(Context context) 
	      {
	    	  super(context);
	    	  
	    	  gameLoopThread = new gameloop(this);
	    	  holder = getHolder();

	             holder.addCallback(new SurfaceHolder.Callback() {
				@Override
                public void surfaceDestroyed(SurfaceHolder holder) 
                {
					 //for stop the game
					gameLoopThread.setRunning(false);
					gameLoopThread.getThreadGroup().interrupt();
	             }
                
                @SuppressLint("WrongCall")
				@Override
                public void surfaceCreated(SurfaceHolder holder) 
                {
                	  gameLoopThread.setRunning(true);
                	  gameLoopThread.start();
                	  
	             }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) 
	                    {
	                    }
	             });
	             
	             //getting the screen size 
	             DisplayMetrics display = this.getResources().getDisplayMetrics();
					
					sy = display.widthPixels;
					sx = display.heightPixels;
					cspeed=sy/2;
					gspeed=sy/2;
					z=-sy;
					lane = 2;
					i3 = (int) (-1*sy/3);
					i4 = (int) (-2*sy/3);
					i5 = (int) (-sy);
	    	  background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
	    	  redcar=BitmapFactory.decodeResource(getResources(), R.drawable.redcar);
	    	  bluecar=BitmapFactory.decodeResource(getResources(), R.drawable.bluecar);
	    	  cyancar=BitmapFactory.decodeResource(getResources(), R.drawable.cyancar);
	    	  goldcar=BitmapFactory.decodeResource(getResources(), R.drawable.goldcar);
	    	  greencar=BitmapFactory.decodeResource(getResources(), R.drawable.greencar);
	    	  purplecar=BitmapFactory.decodeResource(getResources(), R.drawable.purplecar);
	    	  
	    	  off=BitmapFactory.decodeResource(getResources(), R.drawable.off);
	    	  on=BitmapFactory.decodeResource(getResources(), R.drawable.on);
	    	  exit=BitmapFactory.decodeResource(getResources(), R.drawable.exit);
	    	  gas=BitmapFactory.decodeResource(getResources(), R.drawable.gas);
	    	  note1=BitmapFactory.decodeResource(getResources(), R.drawable.note1);
	    	  pause=BitmapFactory.decodeResource(getResources(), R.drawable.pause);
	    	  note2=BitmapFactory.decodeResource(getResources(), R.drawable.note2);
	    	  
	    	  off=Bitmap.createScaledBitmap(off, 25,25, true);
	    	  exit=Bitmap.createScaledBitmap(exit, 25,25, true);
	    	  on=Bitmap.createScaledBitmap(on, 25,25, true);
	    	  pause=Bitmap.createScaledBitmap(pause, 25,25, true);
	    	  note2=Bitmap.createScaledBitmap(note2, sy,sx, true);
	    	  
	    	  background=Bitmap.createScaledBitmap(background, 2*sy,sx, true);
	    	  //health dec
	    	  note1=Bitmap.createScaledBitmap(note1, sy,sx, true);
	    	  
	    	  mp1=MediaPlayer.create(Game.this,R.raw.game);
	    	  move=MediaPlayer.create(Game.this,R.raw.move);
	      }
	      
	      // on touch method
	      
	      @Override
			public boolean onTouchEvent(MotionEvent event) {
				
	    	  	if(event.getAction()==MotionEvent.ACTION_DOWN)
	    	  	{
	    	  		show=1;
	    	  		
	    	  		getx=(int) event.getX();
	    	  		gety=(int) event.getY();
	    	  		//lane change
	    	  		if(gety>sx/2){
	    	  			if(lane > 1)
	    	  				lane -=1;
	    	  		}
	    	  		else{
	    	  			if(lane < 3)
	    	  				lane +=1;
	    	  		}
	    	  		//exit
	    	  		if(getx<25&&gety<25)
	    	  		{
	    	  			System.exit(0);
	    	  	
	    	  		}
	    	  		//sound off
	    	  		if(gety>25&&gety<60)
	    	  		{
	    	  			if(getx<25)
	    	  			{
	    	  				sound=0;
	    	  				mp1.stop();
	    	  			}
	    	  		}
	    	  		//sound on
	    	  		if(gety>61&&gety<90)
	    	  		{
	    	  			if(getx<25)
	    	  			{
	    	  				sound=1;
	    	  			
	    	  			}
	    	  		}
	    	  		// restart game
	    	  		if(getx>91&&gety<25)
	    	  		{
	    	  			if(health<=0)
	    	  			{
	    	  				gameLoopThread.setPause(0);
							health=300;
							score=0;
	    	  			
	    	  			}
	    	  		}
	    	  		//pause game
	    	  		if(getx<(25)&&gety>(sx-25)&&pausecount==0)
	    	  		{
	    	  			
	    	  			gameLoopThread.setPause(1);
	    	  			mp1.stop();
	    	  			pausecount=1;
	    	  		}
	    	  		else if(getx<(25)&&gety>(sx-25)&&pausecount==1)
	    	  		{
	    	  			gameLoopThread.setPause(0);
	    	  			mp1.start();
	    	  			pausecount=0;
	    	  		}
	    	  	}
		  		
		  		return true;
			}
			
	      
	        @SuppressLint("WrongCall")
			@Override
		      protected void onDraw(Canvas canvas) 
		      {
		   
	    	  //volume 
	        	SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
	        	volume=pref.getInt("vloume", 0);
	        	if(volume==0)
	        	{
	        		sound=0;
	        	}
	  	    
	    	  	canvas.drawColor(Color.BLACK);
	    	  	
	    	  	//background moving
		    	z=z+30;
		    	if(z>0)
		    	{
		    		z=-sy;
		    		canvas.drawBitmap(background, z, 0, null);
		    		
		    	}
		    	else
		    	{
		    		canvas.drawBitmap(background, z, 0, null);	
		    	}
		    	
		    	//player
		    	if (health != 0)
    		 	{
		    		timer++;
		    		if(timer%5==0)
		    			health-=8;
    		 		score++;
    		 	}
		    	
		    		 x+=5;
		    		 if(x==20)
		    		 {
		    			 x=5;
		    		 }
		    		 
		    		  if(show==0)
		    		  {
		    			  
		    			  switch(lane){
		    			  case 1:
		    				  cur_sx=(float) (7.8*sx/12);
		    				  break;
		    			  case 2:
		    				  cur_sx=(float) (5.23*sx/12);
		    				  break;
		    			  case 3:
		    				  cur_sx=(float) (2.8*sx/12);
		    				  break;
		    			  default:
		    				  break;
		    			  }
		    			canvas.drawBitmap(redcar, 15*sy/18, cur_sx, null);
		    			  //gas hit
	    				  if(gspeed>(sy-50) && gaslane == lane)
	    				  {
	    					  gspeed=sy;
	    					  if(health >=275)
	    						  health = 300;
	    					  else
	    						  health+=25;
	    					  canvas.drawBitmap(note1, 0, 0, null);
	    				  }
	    				  
	    				  //car hit
	    				  if(car1lane == lane && i3>(sy-20) && i3<sy)
	    				  {
	    					  health-=35;
	    				  }
	    				  if(car2lane == lane && i4>(sy-20) && i4<sy)
	    				  {
	    					  health-=35;
	    				  }
	    				  if(car3lane == lane && i5>(sy-20) && i5<sy)
	    				  {
	    					  health-=35;
	    				  }
			    			  
		    		  }
		    		  
		    		  
		    		  // for switch lane
			    	 if(show==1)
			    	 {
			    		 if(sound==1)
			    		 {
			    			 move.start();
			    		 }
			    		 
			    		 	switch(lane){
			    		 	case 1:
			    		 		canvas.drawBitmap(redcar, 15*sy/18, cur_sx, null);
			    		 		break;
			    		 	case 2:
			    		 		canvas.drawBitmap(redcar, 15*sy/18, cur_sx, null);
			    		 		break;
			    		 	case 3:
			    		 		canvas.drawBitmap(redcar, 15*sy/18, cur_sx, null);
			    		 		break;
			    		 	default:
			    		 		break;
			    		 	}
		    				  
		    				  //score
			    		 	
		    				  if(cspeed<=sy/8&&cspeed>=sy/16)
		    				  {
		    					  cspeed=sy/2;
		    					  score+=10;
		    				  }
			    		// move-hold
			    		 delay+=1;
			    		 if(delay==3)
			    		 {
			    		 show=0;
			    		 delay=0;
			    		 }
			    	 }
		    		  //for cars
		    		  //7.8*sx/12
   				      //5.23*sx/12
   				      //2.8*sx/12
		    		  i3=i3+22;
		    		  i4=i4+22;
		    		  i5=i5+22;
		    		  
		    		  if(i5>sy)
		    		  {
				    		car1lane = r.nextInt(4-1)+1;
				    		car1color = r.nextInt(6-1)+1;
				    		i3 = -1*(r.nextInt(1*sy/3-0)+0);
				    		switch(car1lane){
				    		case 1:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (2.8*sx/12), null);
				    			break;
				    		default:
				    			break;
				    		}
				    	}
		    		  else
				    	{
				    		switch(car1lane){
				    		case 1:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (7.8*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (5.23*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car1color==1)
				    				canvas.drawBitmap(purplecar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==2)
				    				canvas.drawBitmap(greencar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==3)
				    				canvas.drawBitmap(cyancar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==4)
				    				canvas.drawBitmap(bluecar, i3, (float) (2.8*sx/12), null);
				    			else if (car1color==5)
				    				canvas.drawBitmap(goldcar, i3, (float) (2.8*sx/12), null);
				    		}
				    	}
		    		  if(i5>sy)
		    		  {
				    		car2lane = r.nextInt(4-1)+1;
				    		car2color = r.nextInt(6-1)+1;
				    		i4 = -1*(r.nextInt(2*sy/3-1*sy/3)+1*sy/3);
				    		switch(car2lane){
				    		case 1:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (2.8*sx/12), null);
				    			break;
				    		default:
				    			break;
				    		}
				    	}
		    		  else
				    	{
				    		switch(car2lane){
				    		case 1:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (7.8*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (5.23*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car2color==1)
				    				canvas.drawBitmap(purplecar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==2)
				    				canvas.drawBitmap(greencar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==3)
				    				canvas.drawBitmap(cyancar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==4)
				    				canvas.drawBitmap(bluecar, i4, (float) (2.8*sx/12), null);
				    			else if (car2color==5)
				    				canvas.drawBitmap(goldcar, i4, (float) (2.8*sx/12), null);
				    		}
				    	}
		    		  if(i5>sy)
		    		  {
				    		car3lane = r.nextInt(4-1)+1;
				    		car3color = r.nextInt(6-1)+1;
				    		i5 = -1*(r.nextInt(sy-2*sy/3)+2*sy/3);
				    		switch(car3lane){
				    		case 1:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (2.8*sx/12), null);
				    			break;
				    		default:
				    			break;
				    		}
				    	}
				    	else
				    	{
				    		switch(car3lane){
				    		case 1:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (7.8*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (5.23*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			if (car3color==1)
				    				canvas.drawBitmap(purplecar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==2)
				    				canvas.drawBitmap(greencar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==3)
				    				canvas.drawBitmap(cyancar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==4)
				    				canvas.drawBitmap(bluecar, i5, (float) (2.8*sx/12), null);
				    			else if (car3color==5)
				    				canvas.drawBitmap(goldcar, i5, (float) (2.8*sx/12), null);
				    		}
				    	}
				    	
				    	//gas
			    		 gspeed=gspeed+28;
			    		 switch(gaslane){
				    		case 1:
				    			canvas.drawBitmap(gas, gspeed, (float) (7.8*sx/12), null);
				    			break;
				    		case 2:
				    			canvas.drawBitmap(gas, gspeed, (float) (5.23*sx/12), null);
				    			break;
				    		case 3:
				    			canvas.drawBitmap(gas, gspeed, (float) (2.8*sx/12), null);
				    			break;
				    		default:
				    			break;
				    		}
			    		 //canvas.drawBitmap(gas, gspeed, 15*sx/18, null);
			    		 if(gspeed>sy)
			    		 {
			    			 gaslane = r.nextInt(4-1)+1;
			    			 gspeed=0;
			    		 }
			    		 
			    		 
				    	//score
				    	 	Paint paint = new Paint();
				    	    paint.setColor(Color.GREEN);
				    	    paint.setAntiAlias(true);
				    	    paint.setFakeBoldText(true);
				    	    paint.setTextSize(15);
				    	    paint.setTextAlign(Align.RIGHT);
				    	    canvas.drawText("Score :"+score, 3*sy/4, 20, paint);
		    		  	//exit
				    	    canvas.drawBitmap(exit, 0, 0, null);
				    	    canvas.drawBitmap(off, 0, 30, null);
					    	canvas.drawBitmap(on, 0, 60, null);
					    	  if(sound==1)
				    		  {
				    		  mp1.start();
				    		  mp1.setLooping(true);
				    		  }
					    	  else
					    	  {
					    		 mp1.stop();
					    	  }
		    		  //health
					    Paint myPaint = new Paint();
					    if (health >= 150)
					    	myPaint.setColor(Color.GREEN);
					    else if(health < 150 && health >= 50)
					    	myPaint.setColor(Color.YELLOW);
					    else if(health < 50)
					    	myPaint.setColor(Color.RED);
					     myPaint.setStrokeWidth(10);
					     myPaint.setAntiAlias(true);
					     myPaint.setFakeBoldText(true);
					    canvas.drawText("Health :"+health, 0, (sx/8)-5, myPaint);
					    canvas.drawRect(sy, sx/8, sy-health, sx/8+10, myPaint);
					    
					  //game over
					    if(health<=0)
					    {
					    	gameover=1;
					    	mp1.stop();
					    	canvas.drawText("GAMEOVER OVER", sy/2, sx/2, myPaint);
					    	canvas.drawText("YOUR SCORE : "+score, sy/2, sx/4, myPaint);
					    	canvas.drawText("Restart", 91, 25, myPaint);
					    	gameLoopThread.setPause(1);
					    	canvas.drawBitmap(background, sy, sx, null);
					    }
					   // restart
					    
						if(reset==1)
						{
							gameLoopThread.setPause(0);
							health = 300;
							score=0;
						}
						
						canvas.drawBitmap(pause, 0, (sx-25), null);
		    	  }
		    
		      }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	
	

}