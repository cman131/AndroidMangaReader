package com.example.testapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BrowserActivity extends Activity {

	private String currentPath="/";
	private ArrayList<String> items = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		// Show the Up button in the action bar.
		setupActionBar();
		getFiles();
	}
	
	public void up(View view){
		String[] temp = currentPath.split("/");
		String newString = "";
		for (int i=0; i<temp.length-1; i++){
			newString+=temp[i]+"/";
		}
		if (newString.equals("")){
			newString="/";
		}
		currentPath=newString;
		getFiles();
	}
	
	public void down(View view){
		Spinner listy = ((Spinner)findViewById(R.id.spinner1));
		if (items.size()!=0){
			currentPath=(String) listy.getSelectedItem();
			getFiles();
		}
	}
	
	public void submit(View view){
		Spinner listy = ((Spinner)findViewById(R.id.spinner1));
		if (items.size()!=0){
			currentPath=(String) listy.getSelectedItem();
			getFiles();
			Intent main = new Intent();
			main.putExtra("ret", currentPath);
		        try {
		        	FileOutputStream fos = openFileOutput("pathFile.yoho", Context.MODE_PRIVATE);
		        	fos.write(currentPath.getBytes());
		        	fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			setResult(RESULT_OK, main);
			finish();
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
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
	

	private void getFiles(){
		File[] files = new File(currentPath).listFiles();
    	items.clear();
    	if (files!=null){
    		for(File file : files){
        		if (file.isDirectory()){
        			items.add(file.getPath());
        		}
        	}
    	}
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        ((Spinner)findViewById(R.id.spinner1)).setAdapter(fileList);
        if(items.size()>0){
        	((Spinner)findViewById(R.id.spinner1)).setSelection(0);
        }
    }

}
