package com.example.testapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private ArrayList<String> listy = new ArrayList<String>();
	private ArrayList<String> chaps = new ArrayList<String>();
	private String path = "/";
	private int currentIndex = 42;
	private int curChap=84;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState != null) {
		   path=(String) savedInstanceState.get("path");
		   update();
		   curChap=(Integer) savedInstanceState.get("curChap");
		   chapChange();
		   currentIndex=(Integer) savedInstanceState.get("currentIndex");
		   setDatImage();
	   }
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		if (savedInstanceState != null) {
			   path=(String) savedInstanceState.get("path");
			   update();
			   curChap=(Integer) savedInstanceState.get("curChap");
			   chapChange();
			   currentIndex=(Integer) savedInstanceState.get("currentIndex");
			   setDatImage();
		 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.clear();
		menu.add("Open");
		menu.add("Jump");
		menu.add("<--");
		menu.add("-->");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		String name=(String) item.getTitle();
	    if (name.equals("Open")){
	        open();
	        return true;
	    }
	    else if (name.equals("Jump")){
	    	if (!"/".equals(path)){
	    		Intent intent = new Intent(this, JumpActivity.class);
	    		intent.putExtra("pass", path+","+Integer.toString(curChap)+","+Integer.toString(currentIndex));
	    		startActivityForResult(intent, 4757);
	    		return true;
	    	}
	    	else{
	    		return true;
	    	}
	    }
	    else if (name.equals("<--")){
	        previousImage();
	        return true;
	    }
	    else if (name.equals("-->")){
	        nextImage();
	        return true;
	    }
	    else {
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("path",path);
		outState.putInt("curChap",curChap);
		outState.putInt("currentIndex",currentIndex);
	}
	
	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
     super.onActivityResult(requestCode, resultCode, data);
     if (data!=null){
    	 if(requestCode==475765457){
         	path=data.getStringExtra("ret");
         	update();
     	}
     	else if (requestCode==4757) {
    	 	curChap=Integer.parseInt(data.getStringExtra("chap"));
    	 	chapChange();
    	 	currentIndex=Integer.parseInt(data.getStringExtra("page"));
    	 	setDatImage();
     	}
     }
    }
	
	public void open(View view){
		open();
	}
	
	public void open(){
		Intent intent = new Intent(this, OpenActivity.class);
		startActivityForResult(intent, 475765457);
	}
	
	private void update(){
		File[] chapy = ((new File(path)).listFiles());
		ArrayList<String> temp = new ArrayList<String>();
		for (File jj : chapy){
			temp.add(jj.getAbsolutePath().split("/")[jj.getAbsolutePath().split("/").length-1]);
		}
		for (String name : temp) {
			chaps.add(name);
		}
		int chapsy=chaps.size();
		chaps.clear();
		for (int j=0; j<chapsy; j++){
			chaps.add(Integer.toString(j));
		}
		curChap=0;
		currentIndex=0;
		chapChange();
		setDatImage();
	}
	
	private void chapChange(){
		listy.clear();
		File[] chapsy = (new File(path+"/"+chaps.get(curChap)).listFiles());
		ArrayList<String> temp = new ArrayList<String>();
		for (File i : chapsy){
			temp.add(i.getAbsolutePath().split("/")[i.getAbsolutePath().split("/").length-1]);
		}
		Collections.sort(temp);
		for (String name : temp) {
			  String cur = name;
			  if(cur.substring(cur.length()-3,cur.length()).equalsIgnoreCase("png") ||
					  cur.substring(cur.length()-3,cur.length()).equalsIgnoreCase("jpg") ||
					  cur.substring(cur.length()-4,cur.length()).equalsIgnoreCase("jpeg") ||
					  cur.substring(cur.length()-4,cur.length()).equalsIgnoreCase("tiff") ||
					  cur.substring(cur.length()-3,cur.length()).equalsIgnoreCase("bmp")){
				  	  listy.add(cur);
			  }
		}
	}
	
	private void setDatImage(){
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		if(listy.size()!=0){
		    Bitmap myBitmap;
			try {
				myBitmap = BitmapFactory.decodeStream(new BufferedInputStream(new FileInputStream(path+"/"+chaps.get(curChap)+"/"+listy.get(currentIndex))));
				image.setImageBitmap(myBitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void nextImage(View view){
		nextImage();
	}
	
	public void nextImage(){
		if (currentIndex+1<listy.size() && currentIndex+1>=0){
			currentIndex+=1;
			setDatImage();
		}
		else if (curChap+1<chaps.size() && currentIndex+1>=listy.size()){
			curChap+=1;
			chapChange();
			currentIndex=0;
			setDatImage();
		}
	}
	
	public void previousImage(View view){
		previousImage();
	}
	
	public void previousImage(){
		if (currentIndex-1>=0 && currentIndex-1<listy.size()){
			currentIndex-=1;
			setDatImage();
		}
		else if (curChap-1>0 && currentIndex-1<=0){
			curChap-=1;
			chapChange();
			currentIndex=listy.size()-1;
			setDatImage();
		}
	}
}
