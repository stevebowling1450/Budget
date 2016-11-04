package com.stveo.stevebowling.budget.Stages;

import android.app.Application;

import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Riggers.SlideRigger;

/**
 * Created by stevebowling on 11/1/16.
 */

public class AddCategoryStage extends IndexedStage {
    private final SlideRigger rigger;

    public AddCategoryStage(Application context){
        super(AddCategoryStage.class.getName());
        this.rigger=new SlideRigger(context);
    }

    public AddCategoryStage(){
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_category_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
