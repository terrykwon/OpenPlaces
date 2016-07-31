package com.unithon.openplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Noverish on 2016-07-30.
 */
public class SearchFragment extends Fragment {

    MapsActivity activity;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public void setActivty(MapsActivity activty) {
        this.activity = activty;
    }

    private ImageView mLlFood, mLlCoffee, mLlBeauty, mLlLaundry, mlLPrint, mlLEtc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        initResource(view);
        initEvent();
        return view;
    }

    public void initResource(View view) {
        Log.e("init","Resource");
        mLlFood = (ImageView) view.findViewById(R.id.category_food);
        mLlCoffee = (ImageView) view.findViewById(R.id.category_cafe);
        mLlBeauty = (ImageView) view.findViewById(R.id.category_beauty);
        mLlLaundry = (ImageView) view.findViewById(R.id.category_laundry);
        mlLPrint = (ImageView) view.findViewById(R.id.category_print);
        mlLEtc = (ImageView) view.findViewById(R.id.category_etc);
    }

    public void initEvent() {
        Log.e("init","Event");
        mLlFood.setOnClickListener(new OnClick("식당"));
        mLlCoffee.setOnClickListener(new OnClick("카페"));
        mLlBeauty.setOnClickListener(new OnClick("미용"));
        mLlLaundry.setOnClickListener(new OnClick("세탁소"));
        mlLPrint.setOnClickListener(new OnClick("인쇄소"));
        mlLEtc.setOnClickListener(new OnClick(""));
    }

    public class OnClick implements View.OnClickListener {
        private String title;

        public OnClick(String title) {
            this.title = title;
        }

        @Override
        public void onClick(View view) {
            // TODO
            Log.e("category click",title);
            activity.realSearch(title);
        }
    }
}
