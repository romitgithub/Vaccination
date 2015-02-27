package com.example.vaccination;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements android.view.View.OnClickListener {

	Button register;
	EditText searchUser;
	Button searchUserButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		
		new Utils(this.getApplicationContext());
		
		this.register = (Button)this.findViewById(R.id.register);
		this.register.setOnClickListener(this);
		
		this.searchUser = (EditText)this.findViewById(R.id.searchUser);
		this.searchUserButton = (Button)this.findViewById(R.id.searchUserButton);
		this.searchUserButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuItem item= menu.findItem(R.id.action_settings);
	    item.setVisible(false);
	    super.onPrepareOptionsMenu(menu);
	    
	    return true;
	}
	
	@Override
	public void onClick(View view) 	
	{		
		if(view == this.register)
		{
			Intent myIntent = new Intent(MainActivity.this, Register.class);
			myIntent.putExtra("key", "test"); //Optional parameters
			MainActivity.this.startActivity(myIntent);
		}
		else if(view == this.searchUserButton)
		{
			new Thread( new Runnable() 
			{
				@Override
				public void run() 
				{
					try
					{
						String userId = MainActivity.this.searchUser.getEditableText().toString();
						
						String r = new String(Utils.post(Utils.host + "/getUser/" + userId, null ));
						JSONObject response = new JSONObject(r);
						
						if(response.getBoolean("success"))
						{
							JSONObject result = response.getJSONObject("data");
							final String name = result.getString("name");
							final String dob = result.getString("dob");
							final String identity = result.getString("identity");
							final String ageGroup = result.getString("agegroup");
							final String phone = result.getString("phone");
							final JSONArray vaccines = result.getJSONArray("vaccine_data");
							
						MainActivity.this.runOnUiThread(new Runnable() 
						{
							@Override
							public void run() 
							{
								Intent myIntent2 = new Intent(MainActivity.this, ShowUser.class);
								myIntent2.putExtra("name", name);
								myIntent2.putExtra("dob", dob);
								myIntent2.putExtra("identity", identity);
								myIntent2.putExtra("age_group", ageGroup);
								myIntent2.putExtra("phone", phone);
								myIntent2.putExtra("vaccine_data", vaccines.toString());
								MainActivity.this.startActivity(myIntent2);
							}
						});
						}
					}
					catch(Exception e)
					{
						Log.e("searchUser", e.toString());
					}
				}
			}).start();
		}
		else
		{
			return;
		}
	}
}
