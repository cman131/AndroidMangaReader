package com.example.testapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

/**
 * allows the user to select the manga to read
 * @author Conor
 *
 */

public class OpenActivity extends Activity {
	
	private ArrayAdapter<String> choice;
	private String path = "/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open);
		// Show the Up button in the action bar.
		setupActionBar();
		try {
			FileInputStream fos = openFileInput("pathFile.yoho");
			path=streamToString(fos);
        	fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			try {
				path=streamToString(getAssets().open("pathFile.yoho"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		populate();
	}

	/**
	 * fills the spinner with the current manga folders in the selected manga directory
	 */
	public void populate(){
		ArrayList<String> temp = new ArrayList<String>();
		for(File i : new File(path).listFiles()){
			temp.add(i.getAbsolutePath());
		}
		choice = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temp);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		choice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(choice);
		spinner.setSelection(0);
	}
	
	/**
	 * converts an inputstream to a string
	 * @param is - the inputstream to be converted
	 * @return the resulting string
	 */
	public static String streamToString(InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
     super.onActivityResult(requestCode, resultCode, data);
     if(data!=null){
    	 if(requestCode==4854){
         	path=data.getStringExtra("ret");
         	populate();
     	}
     }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * finishes the activity and tells the mainview which manga to load
	 * @param view - the current view
	 */
	public void send(View view){
		Intent main = new Intent();
		main.putExtra("ret", (String)((Spinner)findViewById(R.id.spinner1)).getSelectedItem());
		setResult(RESULT_OK, main);
		finish();
	}

	/**
	 * opens the browser activity to let the user select a different manga directory
	 * @param view - the current view
	 */
	public void changeDir(View view){
		Intent intent = new Intent(this, BrowserActivity.class);
		intent.putExtra("pass", path);
		startActivityForResult(intent, 4854);
	}

}
