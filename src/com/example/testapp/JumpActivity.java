package com.example.testapp;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class JumpActivity extends Activity {

	private int page = 0;
	private int chap = 0;
	private String path="";
	private ArrayAdapter<String> chapBack;
	private ArrayAdapter<String> pageBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jump);
		// Show the Up button in the action bar.
		setupActionBar();
		String[] info = this.getIntent().getStringExtra("pass").split(",");
		path=info[0];
		chap=Integer.parseInt(info[1]);
		page=Integer.parseInt(info[2]);
		int chapsy = 0;
		chapsy = (new File(path)).listFiles().length;
		String chaps="";
		for (int j=0; j<chapsy; j++){
			if (j!=chapsy-1){
				chaps+=Integer.toString(j)+",";
			}
			else{
				chaps+=Integer.toString(j);
			}
		}
		chapBack = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chaps.split(","));
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		chapBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(chapBack);
		spinner.setSelection(chap);
		fillPages();
		spinner.setOnItemSelectedListener(new SpinnerActivity());
	}

	private void fillPages() {
		File[] chapsy = (new File(path+"/"+((Spinner)findViewById(R.id.spinner2)).getSelectedItem()).listFiles());
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=0;i<chapsy.length; i++){
			temp.add(Integer.toString(i));
		}
		pageBack = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temp);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		pageBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(pageBack);
		spinner.setSelection(page);
	}
	
	public class SpinnerActivity implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Spinner spin = (Spinner)findViewById(R.id.spinner2);
			page=0;
			chap=spin.getSelectedItemPosition();
			String[] info = getIntent().getStringExtra("pass").split(",");
			if(Integer.parseInt(info[1])==chap){
				page=Integer.parseInt(info[2]);
			}
			fillPages();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Spinner spin = (Spinner)findViewById(R.id.spinner2);
			spin.setSelection(0);
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
		getMenuInflater().inflate(R.menu.jump, menu);
		return true;
	}
	
	public void submit(View view){
		Spinner spin = (Spinner)findViewById(R.id.spinner2);
		Spinner spin2 = (Spinner)findViewById(R.id.spinner1);
		Intent main = new Intent();
		main.putExtra("chap", Integer.toString(spin.getSelectedItemPosition()));
		main.putExtra("page", Integer.toString(spin2.getSelectedItemPosition()));
		setResult(RESULT_OK, main);
		finish();
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

}
