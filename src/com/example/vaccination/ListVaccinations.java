package com.example.vaccination;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListVaccinations extends ActionBarActivity {

	Intent intent;
	String name;
	String ageGroup;
	RelativeLayout ageGroup1;
	RelativeLayout ageGroup2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_vaccinations);
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		mTitleTextView.setText("Vaccination");
		
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		this.intent = getIntent();
		this.name = intent.getStringExtra("name");
		this.ageGroup = intent.getStringExtra("age_group");
		this.ageGroup1 = (RelativeLayout)this.findViewById(R.id.ageGroup1);
		this.ageGroup2 = (RelativeLayout)this.findViewById(R.id.ageGroup2);
		
		if(this.ageGroup.equalsIgnoreCase("19 - 21"))
		{
			this.ageGroup2.setVisibility(0);
		}
		else if(this.ageGroup.equalsIgnoreCase("0 - 18"))
		{
			this.ageGroup1.setVisibility(0);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_vaccinations, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuItem item= menu.findItem(R.id.action_settings);
	    item.setVisible(false);
	    super.onPrepareOptionsMenu(menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
