package com.minaq.view;

import java.util.ArrayList;

import com.minaq.game.CurrentContext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CanvasPanel extends SurfaceView implements SurfaceHolder.Callback{
	private CanvasThread canvasThread;
	public CanvasPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		//canvasThread = new CanvasThread(getHolder(), this);
		setFocusable(true);
		
		
	}
	
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	private boolean firstRun = true;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		canvasThread = new CanvasThread(getHolder(), this);
		if(firstRun || lifes == 0){
			initBricks();
		}
		initBall();
		initPaddle();
		canvasThread.setRunning(true);
		canvasThread.start();
		firstRun = false;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(canvasThread.isRunning()){
			boolean retry = true;
			canvasThread.setRunning(false);
			while(retry){
				try{
					canvasThread.join();
					retry = false;
				}catch(InterruptedException e){
					//try again indefinitely
				}
			}
		}
		
		
	}
	
	private int x = 60;
	private int y = 250;
	private int dx = 2;
	private int dy = 3;
	private int radius = 25;
	private int offset = 150; //top of screen offset
	
	public void initBall(){
	
		x = 60;
		y = 250;
		dx = 2;
		dy = 4;
		radius = 25;
		offset = 150; //top of screen offset
	}
	
	private int paddlex =  CurrentContext.getDeviceWidth() / 2;
	private int oldpaddlex = CurrentContext.getDeviceWidth() / 2;;
	private int paddleh =  50;
	private int paddlew =  150;
	private int moveSpeed = 20;
	private int lifes = 3;
	
	public void initPaddle(){
		paddlex =  CurrentContext.getDeviceWidth() / 2;
		oldpaddlex = CurrentContext.getDeviceWidth() / 2;;
		paddleh =  50;
		paddlew =  150;
		moveSpeed = 20;	
	}
	
	
	private int[][] bricks;
	private int nRows;
	private int nCols;
	private int brickWidth;
	private int brickHeight;
	private int padding;
	private int rowHeight;
	private int colWidth;
	
	public void initBricks(){
		nRows = 5;
		nCols = 4;
		brickWidth = (CurrentContext.getDeviceWidth() / nCols) - 1;
		brickHeight = 25;
		padding = 1;
		rowHeight = brickHeight + padding;
		colWidth = brickWidth + padding; 
		bricks = new int[5][5];
		for(int i = 0;i < nRows; i++){
			for(int j = 0; j < nCols; j++){
				bricks[i][j] = 1;
			}
		}
	}
	
	public void moveRight(){
		oldpaddlex = paddlex;
		paddlex += moveSpeed;
	}
	
	public void moveLeft(){
		oldpaddlex = paddlex;
		paddlex -= moveSpeed;
	}
	
	@Override
	public void onDraw(Canvas c){
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		c.drawRect(0, 0, CurrentContext.getDeviceWidth(), CurrentContext.getDeviceHeight(), paint);
		
//		paint.setColor(Color.BLACK);
//		c.drawRect(0, CurrentContext.getDeviceHeight()-offset-5, CurrentContext.getDeviceWidth(), CurrentContext.getDeviceHeight()-offset+paddleh, paint);
		
		//paint bricks
		
		for(int i = 0;i < nRows; i++){
			for(int j = 0; j < nCols; j++){
				if(bricks[i][j] == 1){
					paint.setColor(Color.CYAN);
					c.drawRect((j * (brickWidth + padding)) + padding, (i * (brickHeight + padding)) + padding, 
							(j * (brickWidth + padding)) + padding + brickWidth, (i * (brickHeight + padding)) + padding + brickHeight, paint) ;
				}
				
			}
		}
		//paint paddle
		paint.setColor(Color.BLUE);
		c.drawRect(paddlex, CurrentContext.getDeviceHeight()-offset-5, paddlex + paddlew, CurrentContext.getDeviceHeight()-offset+paddleh, paint);
		

		
		
		
		if(y < (nRows * rowHeight)){
			// have we hit a brick
			int row = (int) Math.floor(y / rowHeight);
			int col = (int) Math.floor(x / colWidth);
			if(row >= 0 && col >= 0 && bricks[row][col] == 1){
				dy = -dy;
				bricks[row][col] = 0;
			}
		}
		
		paint.setColor(Color.RED);
		c.drawCircle(x, y, radius, paint);
		if (x + dx > CurrentContext.getDeviceWidth() - radius || x + dx - radius < 0){
			dx = -dx;
			Log.d("BreakOutClone", "Negated X: x = " + x + ": y = " + y + ": dx = " + dx + ": dy = " + dy);
		}
//		if (y + dy > CurrentContext.getDeviceHeight() - offset - radius || y + dy - radius < 0)		
//	    	dy = -dy;
		if (y + dy - radius < 0)	{
		    dy = -dy;
		    Log.d("BreakOutClone", "Top - Negated Y: x = " + x + ": y = " + y + ": dx = " + dx + ": dy = " + dy);
		}else if(y + dy > CurrentContext.getDeviceHeight() - offset - radius && x > paddlex && x < paddlex + paddlew){
			//if(x > paddlex && x < paddlex + paddlew){
				dy = -dy;
				Log.d("BreakOutClone", "Paddle - Negated Y: x = " + x + ": y = " + y + ": dx = " + dx + ": dy = " + dy);
			//}
		}else if(y + dy > CurrentContext.getDeviceHeight()){
			// game over
			canvasThread.setRunning(false);
			lifes = lifes - 1;
			return;
		}
		x += dx;
		y += dy;
		
		
		
		
	}
	
	public class CanvasThread extends Thread{
		private SurfaceHolder surfaceHolder;
		private CanvasPanel panel;
		private boolean running = false;
		
		public CanvasThread(SurfaceHolder sh, CanvasPanel p){
			this.surfaceHolder = sh;
			this.panel = p;
		}
		
		public void setRunning(boolean r){
			this.running = r;
		}
		
		public boolean isRunning(){
			return this.running;
		}
		
		@Override
		public void run(){
			Canvas c;
			while(running){
				c = null;
				try{
					c = this.surfaceHolder.lockCanvas();
					synchronized(this.surfaceHolder){
						this.panel.onDraw(c);
					}
				}finally{
					// do this to leave the surface in a consistent state on an exception
					if(c != null){
						this.surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
			Thread.currentThread().interrupt();
			
		}
	}



	
}
