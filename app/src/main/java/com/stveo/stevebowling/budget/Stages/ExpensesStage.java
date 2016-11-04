package com.stveo.stevebowling.budget.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Riggers.SlideRigger;
import com.stveo.stevebowling.budget.Views.ExpensesView;

/**
 * Created by stevebowling on 11/2/16.
 */

public class ExpensesStage extends IndexedStage {

    private final SlideRigger rigger;
    private final int categoryId;

    public ExpensesStage(Application context, int categoryId) {
        super(ExpensesStage.class.getName());
        this.rigger = new SlideRigger(context);
        this.categoryId = categoryId;
        addComponents(new DataComponent());
    }

    public ExpensesStage(int categoryId) {
        this(BudgetApplication.getInstance(), categoryId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.expenses_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    @Override
    public boolean isModal() {
        return false;
    }

    private class DataComponent implements Component{
        @Override
        public void afterSetUp(Stage stage, boolean isInitializing) {
            ExpensesView expensesView= (ExpensesView)stage.getView();
            expensesView.setCategoryId(categoryId);
        }

        @Override
        public void beforeTearDown(Stage stage, boolean isFinishing) {

        }
    }
}
