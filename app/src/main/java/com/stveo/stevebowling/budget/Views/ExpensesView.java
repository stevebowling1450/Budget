package com.stveo.stevebowling.budget.Views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stveo.stevebowling.budget.Adapters.ExpenseAdapter;
import com.stveo.stevebowling.budget.MainActivity;
import com.stveo.stevebowling.budget.Models.Expense;
import com.stveo.stevebowling.budget.Network.RestClient;
import com.stveo.stevebowling.budget.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stevebowling on 11/2/16.
 */

public class ExpensesView extends LinearLayout {
    private Context context;

    @Bind(R.id.amount_field)
    EditText amountField;

    @Bind(R.id.date_field)
    EditText dateField;

    @Bind(R.id.note_field)
    EditText noteField;

    @Bind(R.id.add_expenses_button)
    FloatingActionButton addExpensesButton;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int categoryId;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    private RestClient restClient= new RestClient();

    private ExpenseAdapter expenseAdapter;

    public ExpensesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        dateField.setText(dateFormatter.format(calendar.getTime()));

        ((MainActivity)context).showMenuItem(false);
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;

        restClient = new RestClient();


        //initialize recyclerView and expenses adapter
        expenseAdapter = new ExpenseAdapter(new ArrayList<Expense>(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(expenseAdapter);
        // load expenses

        loadExpenses();
    }

    @OnClick(R.id.date_field)
    public void dateTapped(EditText editText){
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month,day);
                        dateField.setText(dateFormatter.format(calendar.getTime()));
                    }
                };

        datePickerDialog = new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
    @OnClick(R.id.add_expenses_button)
    public void addTapped(){
        Double amount;
        try {
            amount = Double.parseDouble(amountField.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(context, "Enter an Amount", Toast.LENGTH_SHORT).show();
            return;
        }
        Expense newExpense = new Expense(amount,categoryId,
                calendar.getTime(), noteField.getText().toString());
        restClient.getApiService().addExpenses(newExpense).enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful()){
                    loadExpenses();
                    amountField.setText("");
                    calendar = Calendar.getInstance();
                    dateField.setText(dateFormatter.format(calendar.getTime()));
                    noteField.setText("");
                    Toast.makeText(context, "Expense Crated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Expense Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                Toast.makeText(context, "Expense Failed", Toast.LENGTH_SHORT).show();
                return;

            }
        });
    }

    private void loadExpenses(){
        restClient.getApiService().getRecentExpense(categoryId).enqueue(new Callback<Expense[]>() {
            @Override
            public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                if (response.isSuccessful()){
                    expenseAdapter.expenses = new ArrayList<Expense>(Arrays.asList(response.body()));
                    expenseAdapter.notifyDataSetChanged();
                }else {

                }
            }

            @Override
            public void onFailure(Call<Expense[]> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }



}
