package com.example.vaccination;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MasterActivity extends ActionBarActivity implements OnItemSelectedListener, android.view.View.OnClickListener{

	Spinner citySpinner;
	Spinner subCitySpinner;
	Spinner typesSpinner;
	Spinner villageSpinner;
	Button enterButton;
	private int mLastSpinnerPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master);

		this.citySpinner = (Spinner) this.findViewById(R.id.citySpinner);
		
		this.subCitySpinner = (Spinner) this.findViewById(R.id.subCitySpinner);
		
		this.typesSpinner = (Spinner)this.findViewById(R.id.typesSpinner);
		this.typesSpinner.setOnItemSelectedListener(this);

		this.villageSpinner = (Spinner)this.findViewById(R.id.villageSpinner);

		this.enterButton = (Button)this.findViewById(R.id.enterButton);
		this.enterButton.setOnClickListener(this);


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

		JSONParser parser = new JSONParser();

		new Utils(this.getApplicationContext());

		try
		{
			Object obj = parser.parse(new InputStreamReader(getAssets().open("districts.json")));

			JSONObject jsonObj = (JSONObject) obj;
			JSONArray cities = (JSONArray)jsonObj.get("cities");
			
			List<String> cityList = new ArrayList<String>();
			
			
			for (Object city : cities) 
			{
				cityList.add(city.toString());
			}

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, cityList);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			dataAdapter.notifyDataSetChanged();
			this.citySpinner.setAdapter(dataAdapter);
			this.citySpinner.setSelection(0,false);
			this.citySpinner.setOnItemSelectedListener(this);
		}
		catch(Exception e)
		{
			Log.e("jsonread", e.toString());
		}

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
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
//		if(mLastSpinnerPosition == position)
//		{
//			return;
//		}
//		
//		mLastSpinnerPosition = position;
		
		String city = String.valueOf(this.citySpinner.getSelectedItem());
		String subCity = String.valueOf(this.subCitySpinner.getSelectedItem());
		String healthType = String.valueOf(this.typesSpinner.getSelectedItem());
		String village = String.valueOf(this.villageSpinner.getSelectedItem());
		
		JSONParser parser = new JSONParser();

		try
		{
			Object obj = parser.parse(new InputStreamReader(getAssets().open(city +  ".json")));

			JSONObject jsonObj = (JSONObject) obj;
			JSONArray entries = (JSONArray)jsonObj.get("entries");
			String firstSubCity, firstHealthFacility, firstVillageName;
			
			if(parent == this.subCitySpinner)
			{
				firstSubCity = subCity;
				firstHealthFacility = ((JSONObject) entries.get(0)).get("Health Facility").toString();
				firstVillageName = ((JSONObject) entries.get(0)).get("Village Name").toString();
			}
			
			else if(parent == this.typesSpinner)
			{
				firstSubCity = subCity;
				firstHealthFacility = healthType;
				firstVillageName = ((JSONObject) entries.get(0)).get("Village Name").toString();
			}
			
			else if(parent == this.villageSpinner)
			{
				firstSubCity = subCity;
				firstHealthFacility = healthType;
				firstVillageName = village;
			}
			else
			{
				firstSubCity = ((JSONObject) entries.get(0)).get("SubDistrict Name").toString();
				firstHealthFacility = ((JSONObject) entries.get(0)).get("Health Facility").toString();
				firstVillageName = ((JSONObject) entries.get(0)).get("Village Name").toString();
			}

			List<String> subCityList = new ArrayList<String>();
			List<String> villageList = new ArrayList<String>();

			
			for (Object subDistricts : entries) 
			{
				JSONObject indJsonObj = (JSONObject) subDistricts;

				String subDistrict = indJsonObj.get("SubDistrict Name").toString();
				String villageName = indJsonObj.get("Village Name").toString();
				String healthFacility = indJsonObj.get("Health Facility").toString();

				if(!subCityList.contains(subDistrict))
				{
					subCityList.add(subDistrict);
				}
				if(!villageList.contains(villageName) && subDistrict.equals(firstSubCity) && healthFacility.equals(firstHealthFacility))
				{
					villageList.add(villageName);
				}
			}

			if(parent == this.subCitySpinner || parent == this.typesSpinner)
			{
				ArrayAdapter<String> villageDataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, villageList);
				villageDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				villageDataAdapter.notifyDataSetChanged();
				this.villageSpinner.setAdapter(villageDataAdapter);
				this.villageSpinner.setSelection(0,false);
				this.villageSpinner.setOnItemSelectedListener(this);
				
				if(this.subCitySpinner == parent) this.typesSpinner.setSelection(0);
			}
			else if(parent == this.citySpinner)
			{
				ArrayAdapter<String> subCityDataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, subCityList);
				subCityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				subCityDataAdapter.notifyDataSetChanged();
				this.subCitySpinner.setAdapter(subCityDataAdapter);
				this.subCitySpinner.setSelection(0, false);
				this.subCitySpinner.setOnItemSelectedListener(this);

				ArrayAdapter<String> villageDataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, villageList);
				villageDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				villageDataAdapter.notifyDataSetChanged();
				this.villageSpinner.setAdapter(villageDataAdapter);
				this.villageSpinner.setSelection(0, false);
				this.villageSpinner.setOnItemSelectedListener(this);
			}
		}
		catch(Exception e)
		{
			Log.e("jsonread", e.toString());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{

	}

	@Override
	public void onClick(View v) 
	{
		if(v == this.enterButton)
		{
			Intent myIntent = new Intent(MasterActivity.this, MainActivity.class);
			myIntent.putExtra("key", "test"); //Optional parameters
			MasterActivity.this.startActivity(myIntent);
		}
	}
}
