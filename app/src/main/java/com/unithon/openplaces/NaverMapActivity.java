package com.unithon.openplaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unithon.openplaces.model.Constant;
import com.unithon.openplaces.model.LocalResponse;
import com.unithon.openplaces.model.SearchResponse;
import com.unithon.openplaces.network.AppController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverMapActivity extends AppCompatActivity {

    private EditText inputRegion, inputTitle;
    private Button btn, html_btn;
    private TextView tv_title, tv_tel, tv_address, tv_closeAt, tv_openAt, tv_status;
    private TextView searchCode, locationSearch;
    private String region, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        inputRegion = (EditText)findViewById(R.id.input_region);
        inputTitle = (EditText)findViewById(R.id.input_title);
        btn = (Button)findViewById(R.id.search_btn);
        html_btn = (Button)findViewById(R.id.html_btn);
        tv_title = (TextView)findViewById(R.id.title);
        tv_tel = (TextView)findViewById(R.id.tel);
        tv_address = (TextView)findViewById(R.id.address);
        tv_closeAt = (TextView)findViewById(R.id.closeAt);
        tv_openAt = (TextView)findViewById(R.id.openAt);
        tv_status = (TextView)findViewById(R.id.status);
        searchCode = (TextView)findViewById(R.id.search_code);
        locationSearch = (TextView)findViewById(R.id.location_search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region = inputRegion.getText().toString().trim();
                title = inputTitle.getText().toString().trim();
                Log.e("httpService()", String.valueOf(AppController.getHttpService()));
                Call<SearchResponse> searchResponseCall = AppController.getHttpService().search(region, title);
                searchResponseCall.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if(response.isSuccessful()) {
                            SearchResponse searchResponse = response.body();

                            tv_title.setText(searchResponse.getTitle());
                            tv_tel.setText(searchResponse.getTel());
                            tv_address.setText(searchResponse.getAddress());
                            tv_closeAt.setText(String.valueOf(searchResponse.getCloseAt()));
                            tv_openAt.setText(String.valueOf(searchResponse.getOpenAt()));
                            tv_status.setText(searchResponse.getStatus());
                            Log.e("테스트", String.valueOf(searchResponse.getImages().get(0)));
                            searchCode.setText(searchResponse.getImages().get(0).getThumbnail());
                            /*for(int i=0; i<searchResponse.getImages().size(); i++) {
                                searchResponse.getImages().get(i).getThumbnail();
                            }*/
                        } else {
                            Log.e("connection status", "req, res error");
                            Toast.makeText(NaverMapActivity.this, "req, res 에러", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.e("connection status", "fail");
                        Toast.makeText(NaverMapActivity.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        html_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Call<List<LocalResponse>> localResponseCall = AppController.getSearch().localSearch(Constant.GOOGLE_API_KEY, "-33.8669710, 151.1958750", 5000);
                localResponseCall.enqueue(new Callback<List<LocalResponse>>() {
                    @Override
                    public void onResponse(Call<List<LocalResponse>> call, Response<List<LocalResponse>> response) {
                        if(response.isSuccessful()) {
                            locationSearch.setText(String.valueOf(localResponseCall));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LocalResponse>> call, Throwable t) {
                        Log.e("connection status", "fail2");
                        Toast.makeText(NaverMapActivity.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
