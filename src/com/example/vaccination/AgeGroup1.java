package com.example.vaccination;

import java.util.LinkedList;

import org.apache.http.entity.StringEntity;
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
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AgeGroup1 extends ActionBarActivity implements OnClickListener{

	String id, vaccine_data;
	CheckBox checkbox1;
	CheckBox checkbox2;
	CheckBox checkbox3;
	CheckBox checkbox4;
	CheckBox checkbox5;
	CheckBox checkbox6;
	CheckBox checkbox7;
	
	LinkedList<String> checkedIDS;
	LinkedList<Integer> checkedNoIds;
	
	RelativeLayout updateView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_age_group1);
		
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
		
		Intent intent = getIntent();
		
		this.id = intent.getStringExtra("identity");
		this.vaccine_data = intent.getStringExtra("vaccine_data");
		
		try
		{
			JSONArray vaccines = new JSONArray(this.vaccine_data);
			for(Integer i = 0; i < vaccines.length(); i++)
			{
				int resID = getResources().getIdentifier(vaccines.getString(i), "id", "com.example.vaccination");
				RelativeLayout now = (RelativeLayout) AgeGroup1.this.findViewById(resID);
				CheckBox current_cb = (CheckBox) now.getChildAt(3);
				current_cb.setEnabled(false);
			}
		}
		catch(Exception e)
		{
			Log.e("array_conversion", e.toString());
		}
		
		this.checkbox1 = (CheckBox)this.findViewById(R.id.checkBox1);
		this.checkbox2 = (CheckBox)this.findViewById(R.id.checkBox2);
		this.checkbox3 = (CheckBox)this.findViewById(R.id.checkBox3);
		this.checkbox4 = (CheckBox)this.findViewById(R.id.checkBox4);
		this.checkbox5 = (CheckBox)this.findViewById(R.id.checkBox5);
		this.checkbox6 = (CheckBox)this.findViewById(R.id.checkBox6);
		this.checkbox7 = (CheckBox)this.findViewById(R.id.checkBox7);
		
		this.checkbox1.setOnClickListener(this);
		this.checkbox2.setOnClickListener(this);
		this.checkbox3.setOnClickListener(this);
		this.checkbox4.setOnClickListener(this);
		this.checkbox5.setOnClickListener(this);
		this.checkbox6.setOnClickListener(this);
		this.checkbox7.setOnClickListener(this);
		
		this.checkedIDS = new LinkedList<>();
		this.checkedNoIds = new LinkedList<>();
		
		this.updateView = (RelativeLayout)this.findViewById(R.id.updateView);
		this.updateView.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.age_group1, menu);
		return true;
	}

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
		if(view == this.updateView)
		{
			new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					try
					{
						
						JSONObject message = new JSONObject();
						
						JSONArray ids = new JSONArray();
						for(String id : AgeGroup1.this.checkedIDS)
						{
							ids.put(id);
						}
						
						message.put("identity", AgeGroup1.this.id);
						message.put("ids", ids);
						
						String r = new String(Utils.post(Utils.host + "/update_view/", new StringEntity(message.toString())));
						JSONObject response = new JSONObject(r);
						
						if(response.getBoolean("success"))
						{
							AgeGroup1.this.runOnUiThread(new Runnable() 
							{
								@Override
								public void run() 
								{
									for(Integer currentId : AgeGroup1.this.checkedNoIds)
									{
										RelativeLayout now = (RelativeLayout) AgeGroup1.this.findViewById(currentId);
										CheckBox current_cb = (CheckBox) now.getChildAt(3);
										current_cb.setEnabled(false);
									}
								}
							});
						}
					}
					catch(Exception e)
					{
						Log.e("updateView", e.toString());
					}
				}
			}).start();
		}
		else if(((CheckBox) view).isChecked() || !((CheckBox) view).isChecked())
		{
			if(view == this.checkbox1)
			{
				if(!this.checkedIDS.contains("bcg_d1"))
				{
					this.checkedIDS.add("bcg_d1");
					this.checkedNoIds.add(((View)view.getParent()).getId());
				}
				else
				{
					this.checkedIDS.remove("bcg_d1");
					this.checkedNoIds.remove(((View)view.getParent()).getId());
				}
			}
			if(view == this.checkbox2)
			{
				if(!this.checkedIDS.contains("hepb_d1"))
				{
					this.checkedIDS.add("hepb_d1");
					this.checkedNoIds.add(((View)view.getParent()).getId());
				}
				else
				{
					this.checkedIDS.remove("hepb_d1");
					this.checkedNoIds.remove(((View)view.getParent()).getId());
				}
			}
		}
	}
}
