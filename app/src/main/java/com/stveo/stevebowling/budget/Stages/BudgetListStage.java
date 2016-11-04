package com.stveo.stevebowling.budget.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Riggers.SlideRigger;

/**
 * Created by stevebowling on 10/31/16.
 */

public class BudgetListStage extends IndexedStage {
    private final SlideRigger rigger;

    public BudgetListStage(Application context){
        super(BudgetListStage.class.getName());
        this.rigger=new SlideRigger(context);
    }

    public BudgetListStage(){
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.budget_list_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
