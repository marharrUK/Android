package com.minaq.game;

import java.util.ArrayList;
import java.util.List;

import com.minaq.view.CanvasPanel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class BreakOutCloneActivity extends Activity implements OnTouchListener {
    /** Called when the activity is first created. */
	private CanvasPanel cp;
	private Button leftPad;
	private Button rightPad;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.view.Display display = ((android.view.WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        CurrentContext.setInitialized(true);
        CurrentContext.setDeviceDimensions(display.getWidth(), display.getHeight());
        setContentView(R.layout.main);
        cp = (CanvasPanel) findViewById(R.id.gameCanvas);
        leftPad = (Button) findViewById(R.id.leftPad);
        rightPad = (Button) findViewById(R.id.rightPad);
        leftPad.setOnTouchListener(this);
        rightPad.setOnTouchListener(this);
        
        ArrayList<? extends Level1> myList;
        
        
        //finish();
        
    }
    
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflanter = getMenuInflater();
		inflanter.inflate(R.layout.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.exit:
					cp.surfaceDestroyed(cp.getHolder());
					finish();	
					break;
			case R.id.start:
					cp.surfaceCreated(cp.getHolder());
					break;
		}
		
		return true;
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		
		if(arg0.getId() == leftPad.getId()){
			cp.moveLeft();
		}else if(arg0.getId() == rightPad.getId()){
			cp.moveRight();
		}
		
		return false;
	}
	
	public class Level1{
		public int lev1Int;
	}
	
	public class Level2 extends Level1{
		
	}
	
	public class Level3 extends Level2{
		
	}
	
	
	
	
}
