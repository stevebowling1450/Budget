package com.stveo.stevebowling.budget;

import android.app.Application;

import com.stveo.stevebowling.budget.Stages.BudgetListStage;
import com.stveo.stevebowling.budget.Views.BudgetListView;

import flow.Flow;
import flow.History;

/**
 * Created by stevebowling on 10/31/16.
 */

public class BudgetApplication extends Application {
    private static BudgetApplication application;
    public final Flow mainFlow=
            new Flow(History.single(new BudgetListStage()));

    public static final String API_BASE_URL="http://android301api.azurewebsites.net/";



    @Override
    public void onCreate() {
        super.onCreate();

        application=this;
    }
    public static  BudgetApplication getInstance(){
        return application;
    }
    public static Flow getMainFlow(){
        return getInstance().mainFlow;
    }
}
