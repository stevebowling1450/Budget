package com.stveo.stevebowling.budget.Stages;

import android.app.Application;

import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Riggers.SlideRigger;

/**
 * Created by stevebowling on 10/31/16.
 */

public class LoginStage extends IndexedStage {
    private final SlideRigger rigger;

    public LoginStage(Application context){
        super(LoginStage.class.getName());
        this.rigger=new SlideRigger(context);
    }

    public LoginStage(){
        this(BudgetApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
