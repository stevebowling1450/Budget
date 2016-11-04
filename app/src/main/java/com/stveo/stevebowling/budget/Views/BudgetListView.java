package com.stveo.stevebowling.budget.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stveo.stevebowling.budget.Adapters.BudgetListAdapter;
import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.MainActivity;
import com.stveo.stevebowling.budget.Models.Category;
import com.stveo.stevebowling.budget.Network.RestClient;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Stages.AddCategoryStage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
public class BudgetListView extends RelativeLayout {
        private Context context;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.week_button)
    Button weekButton;

    @Bind(R.id.month_button)
    Button monthButton;

    @Bind(R.id.date_textview)
    TextView dateTextView;

    @Bind(R.id.today_button)
    Button todayButton;

    private RestClient restClient;
    private BudgetListAdapter weekAdapter;
    private BudgetListAdapter monthAdapter;
    private Map<Date, ArrayList<Category>> weekCategories;
    private Map<Date, ArrayList<Category>> monthCategories;
    private Calendar currentDate;
    private static final int weekIndex = 0;
    private static final int monthIndex = 1;
    private int currentIndex = weekIndex;

    public BudgetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        restClient = new RestClient();
        weekCategories = new HashMap<>();
        monthCategories = new HashMap<>();

        weekAdapter = new BudgetListAdapter(new ArrayList<Category>(), context);
        monthAdapter = new BudgetListAdapter(new ArrayList<Category>(), context);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(weekAdapter);

        getNow();

        loadCategories();

        todayButton.setVisibility(GONE);
        dateTextView.setText(getDateRange());

        ((MainActivity)context).showMenuItem(true);
    }

    private void getNow(){
        currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.clear(Calendar.MINUTE);
        currentDate.clear(Calendar.SECOND);
        currentDate.clear(Calendar.MILLISECOND);
    }

    @OnClick(R.id.add_category_button)
    public void showAddCategoryView(){
        Flow flow = BudgetApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new AddCategoryStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.week_button)
    public void showWeekExpense(){
        weekButton.setAlpha(1.0f);
        monthButton.setAlpha(0.8f);

        currentIndex = weekIndex;

        recyclerView.setAdapter(weekAdapter);
        loadCategories();
        setupDateHeader();
    }
    @OnClick(R.id.month_button)
    public void showMonthExpense(){
        weekButton.setAlpha(0.8f);
        monthButton.setAlpha(1.0f);

        currentIndex = monthIndex;

        recyclerView.setAdapter(monthAdapter);
        loadCategories();
        setupDateHeader();
    }

    @OnClick(R.id.today_button)
    public void todayTapped(){
        getNow();
        setupDateHeader();

    }

    @OnClick(R.id.back_button)
    public void backTapped(){
        if (currentIndex == weekIndex){
            currentDate.add(Calendar.DAY_OF_YEAR,-7);
        }else {
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            currentDate = getFirstOfMonth();
            currentDate.add(Calendar.MONTH, -1);
            currentDate.add(Calendar.DAY_OF_YEAR,
                    Math.min(day -1, currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)-1));
        }
        setupDateHeader();
    }

    @OnClick(R.id.forward_button)
    public void forwardTapped(){
        if (currentIndex == weekIndex){
            currentDate.add(Calendar.DAY_OF_YEAR, 7);
        }else {
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            currentDate = getFirstOfMonth();
            currentDate.add(Calendar.MONTH, 1);
            currentDate.add(Calendar.DAY_OF_YEAR,
                    Math.min(day -1, currentDate. getActualMaximum(Calendar.DAY_OF_MONTH)-1));
        }
        setupDateHeader();
    }

    private Calendar getFirstOfWeek(){
        Calendar weekStart = (Calendar)currentDate.clone();
        weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());
        return weekStart;
    }

    private Calendar getFirstOfMonth(){
        Calendar monthStart = (Calendar)currentDate.clone();
        monthStart.set(Calendar.DAY_OF_MONTH,1);
        return monthStart;
    }

    private void updateWeek(ArrayList<Category> newCategories, Date week){
        weekAdapter.categories= newCategories;
        weekCategories.put(week, newCategories);
        weekAdapter.notifyDataSetChanged();
    }

    private void updateMonth( ArrayList<Category> newCategories, Date month){
        monthAdapter.categories= newCategories;
        monthCategories.put(month, newCategories);
        monthAdapter.notifyDataSetChanged();
    }

    private void loadCategories(){
        if (currentIndex== weekIndex){
            final Calendar weekStart = getFirstOfWeek();
            ArrayList<Category> weekExpenses = weekCategories.get(weekStart.getTime());
            if (weekExpenses != null){
                weekAdapter.categories = weekExpenses;
                weekAdapter.notifyDataSetChanged();
            }

            restClient.getApiService().getWeekCategories(weekStart.get(Calendar.YEAR),
            weekStart.get(Calendar.MONTH) + 1,
            weekStart.get(Calendar.DAY_OF_MONTH)).enqueue(new Callback<Category[]>() {
                @Override
                    public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                    if (response.isSuccessful()){
                        updateWeek(new ArrayList<>(Arrays.asList(response.body())),
                                weekStart.getTime());
                    }else {
                        //toast

                    }
                }

                @Override
                public void onFailure(Call<Category[]> call, Throwable t) {

                }
            });
        }else {
            final Calendar monthStart = getFirstOfMonth();
            ArrayList<Category> monthExpenses = monthCategories.get(monthStart.getTime());
            if (monthExpenses != null){
                monthAdapter.categories = monthExpenses;
                monthAdapter.notifyDataSetChanged();
            }

            restClient.getApiService().getMonthCategories(monthStart.get(Calendar.YEAR),
                   monthStart.get(Calendar.MONTH)+1).enqueue(new Callback<Category[]>() {
                @Override
                public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                    if (response.isSuccessful()){
                        updateMonth(new ArrayList<>(Arrays.asList(response.body())),
                                monthStart.getTime());
                }else {
                        //toast
                    }
                }

                @Override
                public void onFailure(Call<Category[]> call, Throwable t) {

                }
            });
        }
    }
    private String getDateRange(){
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.US);
        if (currentIndex == weekIndex){
            Calendar startDate= getFirstOfWeek();
            Calendar endDate = getFirstOfWeek();
            endDate.add(Calendar.DAY_OF_YEAR, 6);

            if (startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
                if (startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)) {
                    return formatter.format("%1$tb %1$td - %2$td, %1$tY", startDate, endDate).toString();
                } else {
                    return formatter.format("%1$tb %1$td - %2$tb %2$td, %1$tY", startDate, endDate).toString();
                }
            } else {
                return formatter.format("%1$tb %1$td, %1$tY - %2$tb %2$td, %2$tY", startDate, endDate).toString();
            }

        }else {
            return formatter.format("%1$tb %1$tY", currentDate).toString();
        }
    }

    private void setupDateHeader(){
        if (currentIndex == weekIndex){
            if (currentDate.get(Calendar.WEEK_OF_YEAR)== Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)){
                getNow();
                todayButton.setVisibility(GONE);
            }else {
                todayButton.setVisibility(VISIBLE);
            }
        }else {
                if (currentDate.get(Calendar.MONTH)== Calendar.getInstance().get(Calendar.MONTH)){
                    getNow();
                    todayButton.setVisibility(GONE);
                }else {
                    todayButton.setVisibility(VISIBLE);
                }
            }
        }
    }

