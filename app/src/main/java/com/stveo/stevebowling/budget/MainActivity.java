package com.stveo.stevebowling.budget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.stveo.stevebowling.budget.Models.TestPost;
import com.stveo.stevebowling.budget.Network.RestClient;
import com.stveo.stevebowling.budget.Network.UserStore;
import com.stveo.stevebowling.budget.Stages.BudgetListStage;
import com.stveo.stevebowling.budget.Stages.CalendarListStage;
import com.stveo.stevebowling.budget.Stages.LoginStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
private String TAG = "MainActivity";
    private Flow flow;
    private ScreenplayDispatcher dispatcher;

    @Bind(R.id.container)
    RelativeLayout container;

    private Menu menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        flow=BudgetApplication.getMainFlow();
        dispatcher=new ScreenplayDispatcher(this, container);
        dispatcher.setUp(flow);

//        testCalls();




        if (UserStore.getInstance().getToken()==null ||
                UserStore.getInstance().getTokenExpiration() == null){
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory, Flow.Direction.REPLACE);
        }



    }

   // public void onClickLogOut
    // UserStore.getInstance().setToken(null);

    @Override
    public void onBackPressed() {
        if (!flow.goBack()){
            flow.removeDispatcher(dispatcher);
            flow.setHistory(History.single(new BudgetListStage()),
                    Flow.Direction.BACKWARD);
            super.onBackPressed();
        }
    }

    private void testCalls(){
        RestClient restClient = new RestClient();
        restClient.getApiService().getPost(5).enqueue(new Callback<TestPost>() {
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "GetPost - Title "+ response.body().getTitle()
                +"\nBody" + response.body().getBody());
            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "GetPost Failed");

            }
        });


        TestPost testPost = new TestPost(1, "test post Title", "test post Body");

        restClient.getApiService().postPost(testPost).enqueue(new Callback<TestPost>() {
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "postPost - Title "+ response.body().getTitle()
                        +"\nBody" + response.body().getBody());

            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "postPost Failed");

            }
        });


        restClient.getApiService().getPosts().enqueue(new Callback<TestPost[]>() {
            @Override
            public void onResponse(Call<TestPost[]> call, Response<TestPost[]> response) {
               Log.d(TAG, "Retrieved  "+ response.body().length+ "post");

            }

            @Override
            public void onFailure(Call<TestPost[]> call, Throwable t) {
                Log.d(TAG, "GetPost Failed");

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_calendar:
                Flow flow = BudgetApplication.getMainFlow();
                History newHistory = flow.getHistory().buildUpon()
                        .push(new CalendarListStage())
                        .build();
                flow.setHistory(newHistory, Flow.Direction.FORWARD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMenuItem (boolean show){
        if (menu != null){
            menu.findItem(R.id.open_calendar).setVisible(show);
        }
    }
}
