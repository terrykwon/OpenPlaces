package com.unithon.openplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by Noverish on 2016-07-30.
 */
public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AutoCompleteTextView searchText = (AutoCompleteTextView) findViewById(R.id.toolbar_search);
        String[] countries = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        searchText.setAdapter(adapter);
        searchText.setThreshold(1);

        // Set up transitions
        // Set up custom transitions
        Transition enterTrans = new Explode();
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        getWindow().setReturnTransition(returnTrans);
    }
}
