package com.example.vaccination;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends ActionBarActivity implements OnClickListener{
	
	EditText fullName;
	EditText identityNo;
	EditText dob;
	EditText phone;
	Spinner ageGroups;
	RadioGroup radioSexGroup;
	RadioButton radioSexButton;
	Button registerNewUser;
	String ageGroup, name, phone_number, identity, birthdate, sex;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		mTitleTextView.setText("Register");
		
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		this.fullName = (EditText)this.findViewById(R.id.fullname);
		this.identityNo = (EditText)this.findViewById(R.id.identity);
		this.dob = (EditText)this.findViewById(R.id.dob);
		this.phone = (EditText)this.findViewById(R.id.phone);
		this.ageGroups = (Spinner)this.findViewById(R.id.agegroups);
		this.radioSexGroup = (RadioGroup)this.findViewById(R.id.radioSex);
		
		this.registerNewUser = (Button)this.findViewById(R.id.registerNewUser);
		
		this.registerNewUser.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
		if(view == this.registerNewUser)
		{
			int selectedSex = radioSexGroup.getCheckedRadioButtonId();
			
			this.radioSexButton = (RadioButton)this.findViewById(selectedSex);
			
			this.name = this.fullName.getEditableText().toString();
			this.ageGroup = this.ageGroups.getSelectedItem().toString();
			this.phone_number = this.phone.getEditableText().toString();
			this.identity = this.identityNo.getEditableText().toString();
			this.birthdate = this.dob.getEditableText().toString();
			this.sex = this.radioSexButton.getText().toString();
			
			if(!this.name.isEmpty() && !this.identity.isEmpty() && !this.sex.isEmpty() && !this.birthdate.isEmpty() && !this.phone_number.isEmpty())
			{
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						try
						{
							JSONObject message = new JSONObject();
							
							message.put("name", Register.this.name);
							message.put("sex", Register.this.sex);
							message.put("agegroup", Register.this.ageGroup);
							message.put("dob", Register.this.birthdate);
							message.put("phone", Register.this.phone_number);
							message.put("identity", Register.this.identity);
							
							String r = new String(Utils.post(Utils.host + "/register_user/", new StringEntity(message.toString())));
							JSONObject response = new JSONObject(r);
							
							if(response.getBoolean("success"))
							{
								Register.this.runOnUiThread(new Runnable() 
								{
									@Override
									public void run() 
									{
										Context context = getApplicationContext();
										CharSequence text = "User Registered successfully!!";
										int duration = Toast.LENGTH_SHORT;

										Toast toast = Toast.makeText(context, text, duration);
										toast.show();
										
										Intent myIntent = new Intent(Register.this, MainActivity.class);
										Register.this.startActivity(myIntent);
									}
								});
							}
							else
							{
								Register.this.runOnUiThread(new Runnable() 
								{
									@Override
									public void run() 
									{
										Context context = getApplicationContext();
										CharSequence text = "Problem registering the user.\n Please try again.";
										int duration = Toast.LENGTH_SHORT;

										Toast toast = Toast.makeText(context, text, duration);
										toast.show();
									}
								});
							}
						}
						catch(Exception e)
						{
							Log.e("register_user", e.toString());
						}
					}
				}).start();
			}
			
		}
	}
}
