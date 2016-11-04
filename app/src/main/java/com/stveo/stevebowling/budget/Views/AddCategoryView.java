package com.stveo.stevebowling.budget.Views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stveo.stevebowling.budget.Models.Category;
import com.stveo.stevebowling.budget.Network.RestClient;
import com.stveo.stevebowling.budget.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stevebowling on 11/1/16.
 */

public class AddCategoryView extends LinearLayout {
private Context context;

    @Bind(R.id.name_field)
    EditText nameField;

    @Bind(R.id.amount_field)
    EditText amountField;

    @Bind(R.id.add_category_button)
    FloatingActionButton addButton;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    public AddCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_category_button)
    public void addCategory(){
        InputMethodManager imm= (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameField.getWindowToken(),0);
        imm.hideSoftInputFromWindow(amountField.getWindowToken(),0);

        String name = nameField.getText().toString();
        String amount = amountField.getText().toString();
        Double amountDouble;

        try {
            amountDouble = Double.parseDouble(amount);
        }catch (NumberFormatException e){
            Toast.makeText(context, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()|| amount.isEmpty()){
            Toast.makeText(context, R.string.fill_in_all
                    , Toast.LENGTH_SHORT).show();
        }else if (amountDouble<0){
            Toast.makeText(context,R.string.invalid_amount , Toast.LENGTH_SHORT).show();
        }else {
            addButton.setEnabled(true);
            progressBar.setVisibility(GONE);

            Category category= new Category(name, amountDouble);
            RestClient restClient = new RestClient();
            restClient.getApiService().addCategory(category).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, "Category Added", Toast.LENGTH_SHORT).show();
                        nameField.setText("");
                        amountField.setText("");

                    }else {
                        resetView();
                        Toast.makeText(context, "Category Add Failed "+": "+response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context, "Category Add Failed ", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void resetView(){
        addButton.setEnabled(true);
        progressBar.setVisibility(GONE);

    }
}
