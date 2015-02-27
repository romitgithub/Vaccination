package com.example.vaccination;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowUser extends ActionBarActivity implements android.view.View.OnClickListener{

	TextView fullName;
	TextView identity;
	TextView dob;
	TextView phone;
	TextView ageGroup;
	
	String name, id, birthdate, agegroup, phone_no, vaccine_data;
	
	Button showDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_user);
		
		Intent intent = getIntent();
		
		this.name = intent.getStringExtra("name");
		this.id = intent.getStringExtra("identity");
		this.birthdate= intent.getStringExtra("dob");
		this.agegroup = intent.getStringExtra("age_group");
		this.phone_no = intent.getStringExtra("phone");
		this.vaccine_data = intent.getStringExtra("vaccine_data");
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		mTitleTextView.setText(this.name);
		
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		this.fullName = (TextView)this.findViewById(R.id.fullName);
		this.identity = (TextView)this.findViewById(R.id.identity);
		this.dob = (TextView)this.findViewById(R.id.dob);
		this.phone = (TextView)this.findViewById(R.id.phone);
		this.ageGroup = (TextView)this.findViewById(R.id.ageGroup);
		
		this.showDetails = (Button)this.findViewById(R.id.showDetails);
		this.showDetails.setOnClickListener(this);
		
		this.fullName.setText(name);
		this.identity.setText(id);
		this.dob.setText(birthdate);
		this.phone.setText(phone_no);
		this.ageGroup.setText(agegroup);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_user, menu);
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

	@Override
	public void onClick(View view) 
	{
		if(view == this.showDetails)
		{
			if(this.agegroup.equalsIgnoreCase("0 - 18"))
			{
				Intent myIntent = new Intent(ShowUser.this, AgeGroup1.class);
				myIntent.putExtra("identity", this.id);
				myIntent.putExtra("vaccine_data", this.vaccine_data);
				ShowUser.this.startActivity(myIntent);
			}
		}
	}
}
