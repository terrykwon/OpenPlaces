package com.unithon.openplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Noverish on 2016-07-30.
 */
public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private LinearLayout mLlFood, mLlCoffee, mLlBeauty, mLlLaundry, mlLPrint, mlLEtc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        initResource(view);
        initEvent();
        return view;
    }

    public void initResource(View view) {
        mLlFood = (LinearLayout) view.findViewById(R.id.ll_food_search);
        mLlCoffee = (LinearLayout) view.findViewById(R.id.ll_coffee_search);
        mLlBeauty = (LinearLayout) view.findViewById(R.id.ll_beauty_search);
        mLlLaundry = (LinearLayout) view.findViewById(R.id.ll_laundry_search);
        mlLPrint = (LinearLayout) view.findViewById(R.id.ll_print_search);
        mlLEtc = (LinearLayout) view.findViewById(R.id.ll_etc_search);
    }

    public void initEvent() {
        mLlFood.setOnClickListener(new OnClick("음식점"));
        mLlCoffee.setOnClickListener(new OnClick("커피"));
        mLlBeauty.setOnClickListener(new OnClick("뷰티"));
        mLlLaundry.setOnClickListener(new OnClick("세탁소"));
        mlLPrint.setOnClickListener(new OnClick("프린트"));
        mlLEtc.setOnClickListener(new OnClick("기타"));
    }

    public class OnClick implements View.OnClickListener {
        private String title;

        public OnClick(String title) {
            this.title = title;
        }

        @Override
        public void onClick(View view) {
            // TODO

        }
    }
}
