package com.stveo.stevebowling.budget.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stveo.stevebowling.budget.BudgetApplication;
import com.stveo.stevebowling.budget.Components.Utils;
import com.stveo.stevebowling.budget.Models.Category;
import com.stveo.stevebowling.budget.R;
import com.stveo.stevebowling.budget.Stages.ExpensesStage;

import java.util.ArrayList;
import java.util.Iterator;
import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;

/**
 * Created by stevebowling on 11/2/16.
 */

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.CategoryHolder> {

    public ArrayList<Category> categories;
    private Context context;

    public BudgetListAdapter(ArrayList<Category>categories,
                             Context context){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.budget_list_item, parent, false);
        return new CategoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(BudgetListAdapter.CategoryHolder holder, int position) {
        if (position < categories.size()) {
            Category category = categories.get(position);
            holder.bindCategory(category);
        } else {
            Double total = 0.0;
            Iterator<Category> iterator = categories.iterator();
            while (iterator.hasNext()) {
                total += iterator.next().getAmount();
            }
            Category category = new Category(context.getString(R.string.total), total);
            holder.bindCategory(category);
        }
    }

    @Override// first thing called by RecyclerView
    public int getItemCount() {
        return categories.size() +1;
    }
        // dealing with the xml page
    class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @Bind(R.id.category_textview)
        TextView categoryTextView;

        @Bind(R.id.amount_textview)
        TextView amountTextView;


        public CategoryHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

            //puts data in the UI
        public void bindCategory(Category category){
            categoryTextView.setText(category.getName());
            amountTextView.setText(Utils.formatDouble(category.getAmount()));
            if (category.getAmount() < 0){
                amountTextView.setTextColor(context.getResources()
                .getColor(R.color.negativeColor));
            }else {
                amountTextView.setTextColor(context.getResources()
                    .getColor(R.color.positiveColor));
            }
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition()< categories.size()){
                Category category= categories.get(getAdapterPosition());
                Flow flow = BudgetApplication.getMainFlow();
                History newHistory = flow.getHistory().buildUpon()
                        .push(new ExpensesStage(category.getId()))
                        .build();
                flow.setHistory(newHistory, Flow.Direction.FORWARD);
            }

        }
    }
}
