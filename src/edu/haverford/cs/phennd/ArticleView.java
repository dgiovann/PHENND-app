package edu.haverford.cs.phennd;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.haverford.cs.phennd.SettingsView.TabListener;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;



public class ArticleView extends Activity {

	DataManager dm;
	
	class TabListener implements ActionBar.TabListener {
    	private Activity activity;
    	private int LaunchCode;
    	 
    	public TabListener(Activity activity, int launchCode) {
    		this.activity = activity;
    		this.LaunchCode = launchCode;
    	}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Do nothing for now
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		if (this.LaunchCode == 0) {
	                Intent intent = new Intent(this.activity, MainActivity.class);
		           	intent.putExtra("TabToLaunch", "Main");
    				startActivity(intent);
    		} else if (this.LaunchCode == 1) {
                Intent intent = new Intent(this.activity, MainActivity.class);
	           	intent.putExtra("TabToLaunch", "Favorites");
				startActivity(intent);
    		} else if (this.LaunchCode == 2) {
                Intent intent = new Intent(this.activity, MainActivity.class);
	           	intent.putExtra("TabToLaunch", "Tags");
				startActivity(intent);
    		} else if (this.LaunchCode == 3) {
    		}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// Do nothing for now
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_view);
		dm = DataManager.getDataManager(getBaseContext());
       
		ActionBar actionBar = getActionBar();
        actionBar.setTitle("Settings");
        actionBar.setSubtitle("PHENND Update");
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tabCategory = actionBar.newTab();
        tabCategory.setText("Categories").setIcon(R.drawable.shelficon).setTabListener(new TabListener(this,0));
        Tab tabFavorites = actionBar.newTab();
        tabFavorites.setText("Favorites").setIcon(R.drawable.staricon).setTabListener(new TabListener(this, 1));
        Tab tabTags = actionBar.newTab();
        tabTags.setText("Tags").setIcon(R.drawable.tagicon).setTabListener(new TabListener(this, 2));

        actionBar.addTab(tabCategory, 0, false);
        actionBar.addTab(tabFavorites, 1, false);
        actionBar.addTab(tabTags, 2, false);

		Bundle extras = getIntent().getExtras();
		final TextView articleTitle = (TextView) findViewById(R.id.textViewArticleTitle);
		final TextView articleText = (TextView)findViewById(R.id.textViewArticleText);
		final String articleName = extras.getString("Name");
		articleTitle.setText(articleName);	
		final ArticleData article = dm.getArticle(articleName);

		articleText.setText(article.getContent());
		articleText.setMovementMethod(LinkMovementMethod.getInstance());
		
		CheckBox favoritesBox = (CheckBox) findViewById(R.id.checkBox1);
		favoritesBox.setChecked(article.isFavorited());
		favoritesBox.setOnClickListener(new OnClickListener() {
			
			  @Override
			  public void onClick(View v) {
				    boolean checked = ((CheckBox) v).isChecked();
				    // Get an ArticleData object from DataManager; call setFavorited(boolean f) on the ArticleData with the value of checked
				    
				    
				    if (checked) {
				    	dm.addFavorite(article.getTitle());
				    }
 				    if (article.isFavorited() == true) {
				    	System.out.println("Article is favorited, do stuff here.");
				    } else {
				    	System.out.println("Article is unfavorited, do stuff here.");
				    }
		 
			  }
			});
		
		
		/**
		
		All of the maps stuff.......
		
		**/ 
/*		if (article.getEventLocation() != null) {
			String serviceString = Context.LOCATION_SERVICE;
			LocationManager locationManager = (LocationManager) getSystemService(serviceString);
			
			boolean enabledOnly = true;
			List<String> providers = locationManager.getProviders(enabledOnly);
			
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setSpeedRequired(false);
			criteria.setCostAllowed(true);
			criteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
			criteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
			criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
			criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);
			
			List<String> matchingProviders = locationManager.getProviders(criteria, false);
			
			String providerName = LocationManager.GPS_PROVIDER;
			LocationProvider gpsProvider = locationManager.getProvider(providerName);
			
			String locationOfInterest = article.getEventLocation();
			Geocoder fwdGeocoder = new Geocoder(this, Locale.US);
			List<Address> locations = null;
			try {
				locations = fwdGeocoder.getFromLocationName(locationOfInterest, 5);
			} catch (IOException e) {
				Log.e("LOCATION_PROVIDER", "IO Exception", e);
			}
			
			
			Uri locationUri = Uri.parse(locationOfInterest);
			Intent mapCall = new Intent(Intent.ACTION_VIEW, locationUri);
			startActivity(mapCall);
			
			
		} else { 
			// do nothing
		}*/
		
		
		
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
				startActivity(new Intent(this, SettingsView.class));		
	            return true;
	        case R.id.about_us:
				startActivity(new Intent(this, AboutUsView.class));		
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onFavoriteCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    /*
	    if (checked == true){
	    	System.out.println("Box is now checked!");
	    } else {
	    	System.out.println("Box is now unchecked!");
	    }
	    */
	    // Get an ArticleData object from DataManager; call setFavorited(boolean f) on the ArticleData with the value of checked
	    // ArticleData article = DataManager.getArticle(articleTitle);
	    // article.setFavorited(checked);
	    // if (article.isFavorited == true) {
	    // System.out.println("Article is favorited!")
	    // } else {
	    // System.out.println("Article is unfavorited!")
	    // }
	}
	    
}
