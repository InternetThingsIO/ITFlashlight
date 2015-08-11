package io.internetthings.flashlight;

import java.io.IOException;

import io.internetthings.flashlight.R;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
/*
 * Authors: Jason Maderski and George Sapp
 * 
 * 
 * 
 */
public class MainActivity extends Activity {
	
	private String website = "http://www.internetthings.io";
	
	private RelativeLayout layout;
	private ImageButton webLinkButton;
	private Camera camera;
	private SurfaceTexture texture;
	private TextView textColor;
	
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Keep Screen ON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Dims screen when turned on
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0.01f;
        getWindow().setAttributes(lp);

        setFonts();
	}
	
	//starts / stops flashlight and changes background color
	public void clickLayout(View v){
		
		layout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		
		checksFlag();
	}
	
	public void checksFlag(){
				
		if(flag){
			darkredBackground();			
			turnOffFlash();
			flag = false;
		}
		else{					
			darkgreenBackground();			
			turnOnFlash();
			flag = true;
		}	
		
	}
	
	public void darkredBackground(){
		layout.setBackgroundColor(getResources().getColor(R.color.darkred));
		textColor = (TextView)findViewById(R.id.TextView0ff);
		textColor.setText(R.string.on);
		textColor.setTextSize(23);
		textColor.setTextColor(getResources().getColor(R.color.green));
		
		textColor = (TextView)findViewById(R.id.textViewTAP);
		textColor.setTextColor(getResources().getColor(R.color.red));
		
		textColor = (TextView)findViewById(R.id.textViewToTurn);
		textColor.setTextSize(23);
		textColor.setTextColor(getResources().getColor(R.color.red));
		
		textColor = (TextView)findViewById(R.id.textViewScreen);
		textColor.setTextColor(getResources().getColor(R.color.red));
	}
	
	public void darkgreenBackground(){
		layout.setBackgroundColor(getResources().getColor(R.color.black));
		textColor = (TextView)findViewById(R.id.TextView0ff);
		textColor.setText(R.string.off);
		textColor.setTextSize(22);
		textColor.setTextColor(getResources().getColor(R.color.red));
		
		textColor = (TextView)findViewById(R.id.textViewTAP);
		textColor.setTextColor(getResources().getColor(R.color.green));
		
		textColor = (TextView)findViewById(R.id.textViewToTurn);
		textColor.setTextSize(21);
		textColor.setTextColor(getResources().getColor(R.color.green));
		
		textColor = (TextView)findViewById(R.id.textViewScreen);
		textColor.setTextColor(getResources().getColor(R.color.green));
	}
		
	//Opens WEBSITE
	public void openWebsite(View v){
		Uri url = Uri.parse(website);
		Intent intent = new Intent(Intent.ACTION_VIEW, url);
		startActivity(intent);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	private void getCamera() {
		Context context = this;
		PackageManager pm = context.getPackageManager();
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(getApplicationContext(), "Device has no camera!",Toast.LENGTH_LONG).show();
			return;
		}
		
		try{
			killCamera();
			camera = Camera.open();
			texture = new SurfaceTexture(0);
			camera.setPreviewTexture(texture);
			camera.startPreview();
			
		}
		catch(IOException ex){
			
		}
			
		
	}
	
	//Turns ON FLASH
	private void turnOnFlash() {
		if(camera != null){
			Parameters p;		
			p = camera.getParameters();		
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
			//Toast.makeText(getApplicationContext(), "FLASH ON",Toast.LENGTH_SHORT).show();
			flag = true;
		}
	}
	
	//Turns OFF FLASH
	private void turnOffFlash(){
				
		if(camera != null){
			Parameters p;
			p = camera.getParameters();
		
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(p);
						
			flag = false;
			
		}
		
		
	}
	
	
	protected void onDestory(){
		super.onDestroy();
		killCamera();
	}
	
	//When Program is cached, release the camera if camera != null
	protected void onPause(){
		super.onPause();		
		killCamera();
		//Toast.makeText(getApplicationContext(), "Pause",Toast.LENGTH_SHORT).show();
	}
	
	protected void onRestart(){
		super.onRestart();
		
	}
	
	protected void onStart(){
		super.onStart();
		getCamera();
		if(flag == true){
			turnOnFlash();
			//Toast.makeText(getApplicationContext(), "Resume Flash" ,Toast.LENGTH_SHORT).show();
		}
		else{
			darkgreenBackground();			
			turnOnFlash();
			flag = true;
		}
		
		//Toast.makeText(getApplicationContext(), String.valueOf(flag) ,Toast.LENGTH_SHORT).show();
		
					
	}
	
	protected void onStop(){
		super.onStop();
		killCamera();
	}
	
	private void killCamera(){
		
		if (camera != null) {
			camera.stopPreview();
	        camera.release();
	        camera = null;	     
		}
	}
	
	private void setFonts(){
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/TitilliumText800wt.otf");
	    TextView myTextView = (TextView) findViewById(R.id.textViewTAP);
	    myTextView.setTypeface(myTypeface);
	    
	    myTypeface = Typeface.createFromAsset(getAssets(), "fonts/TitilliumText800wt.otf");
	    myTextView = (TextView) findViewById(R.id.textViewScreen);
	    myTextView.setTypeface(myTypeface);
	    
	    myTypeface = Typeface.createFromAsset(getAssets(), "fonts/TitilliumText800wt.otf");
	    myTextView = (TextView) findViewById(R.id.textViewToTurn);
	    myTextView.setTypeface(myTypeface);
	    
	    myTypeface = Typeface.createFromAsset(getAssets(), "fonts/TitilliumText800wt.otf");
	    myTextView = (TextView) findViewById(R.id.TextView0ff);
	    myTextView.setTypeface(myTypeface);
	    
	}
	

}