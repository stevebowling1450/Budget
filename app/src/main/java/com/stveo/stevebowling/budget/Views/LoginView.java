package com.stveo.stevebowling.budget.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.Models.User;
import com.stveo.stevebowling.budget.Network.RestClient;
import com.stveo.stevebowling.budget.Network.UserStore;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Stages.BudgetListStage;
import com.stveo.stevebowling.budget.Stages.RegisterStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stevebowling on 10/31/16.
 */

public class LoginView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;


    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void showRegisterView() {
        Flow flow = BudgetApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }


    @OnClick(R.id.login_button)
    public void login(){
        InputMethodManager imm =(InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        String username= usernameField.getText().toString();
        String password= passwordField.getText().toString();

        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(context, R.string.provide_username,
                    Toast.LENGTH_LONG).show();
        }else {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            User user = new User(username, password);
            RestClient restClient = new RestClient();
            restClient.getApiService().login(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        User authUser= response.body();
                        UserStore.getInstance().setToken(authUser.getToken());
                        UserStore.getInstance().setTokenExpiration(authUser.getExpiration());

                        Flow flow= BudgetApplication.getMainFlow();
                        History newHistory = History.single(new BudgetListStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);

                    }else {
                        resetView();
                        Toast.makeText(context, R.string.login_failed + ": "+ response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void resetView(){
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);

    }
}
