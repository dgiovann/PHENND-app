package edu.haverford.cs.phennd;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleListView extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_list_view);
		Bundle extras = getIntent().getExtras();
		String tagsOrCategories = extras.getString("TagsOrCategories");
		String metaInfo = extras.getString("MetaInfo");
		
        final Button mainViewButtonList = (Button) findViewById(R.id.buttonMainList);		

		
		mainViewButtonList.setOnClickListener(
                new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            startActivityForResult(intent, 0);
                        }
                });
        final Button tagsViewButtonList = (Button) findViewById(R.id.buttonTagsList);
		tagsViewButtonList.setOnClickListener(
                new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(v.getContext(), TagView.class);
                            startActivityForResult(intent, 0);
                        }
                });
	
		final ListView listOfStories = (ListView)findViewById(R.id.listViewList);

		listOfStories.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
                Intent intent = new Intent(v.getContext(), ArticleView.class);
            	String storyName = (String) listOfStories.getItemAtPosition(position);
            	intent.putExtra("Name", storyName);
            	startActivityForResult(intent, 0);
            }
        });
       
		List<String> articles = new ArrayList<String>();
		if (tagsOrCategories.equals("Tags")) {
        		articles = DataManager.getArticleTitlesForTag(metaInfo); // REPLACE THIS WITH SINGLETON ACCESS TO DATAMANAGER
        } else if (tagsOrCategories.equals("Categories")) {
        		articles = DataManager.getArticleTitlesForCategory(metaInfo); // REPLACE THIS WITH SINGLETON ACCESS TO DATAMANAGER
        }
      
        ListView listView = (ListView) findViewById(R.id.listViewList);		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);
		listView.setAdapter(adapter); 
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_favorites_view, menu);
		return true;
	}

}
