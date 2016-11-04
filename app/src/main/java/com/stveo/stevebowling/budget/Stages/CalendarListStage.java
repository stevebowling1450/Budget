package com.stveo.stevebowling.budget.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Riggers.VerticalSlideRigger;

/**
 * Created by stevebowling on 11/3/16.
 */

public class CalendarListStage extends IndexedStage {

    private final VerticalSlideRigger rigger;

    public CalendarListStage(Application context) {
        super(CalendarListStage.class.getName());
        this.rigger = new VerticalSlideRigger(context);
    }

    public CalendarListStage() {
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.calendar_list_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
