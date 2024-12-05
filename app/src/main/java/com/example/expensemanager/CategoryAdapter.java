package com.example.expensemanager;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<ExpenseCategory> categories;

    public CategoryAdapter(List<ExpenseCategory> categories) {
        this.categories = categories;
        sortCategories();
    }

    private void sortCategories() {
        Collections.sort(categories, new Comparator<ExpenseCategory>() {
            @Override
            public int compare(ExpenseCategory c1, ExpenseCategory c2) {
                return Float.compare(c2.getPercentage(), c1.getPercentage());
            }
        });
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ExpenseCategory category = categories.get(position);

        GradientDrawable colorCircle = new GradientDrawable();
        colorCircle.setShape(GradientDrawable.OVAL);
        colorCircle.setColor(category.getColor());
        holder.colorIndicator.setBackground(colorCircle);

        holder.categoryName.setText(category.getName());
        holder.categoryPercentage.setText(category.getFormattedPercentage());
        holder.categoryAmount.setText(category.getFormattedAmount());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View colorIndicator;
        TextView categoryName;
        TextView categoryPercentage;
        TextView categoryAmount;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPercentage = itemView.findViewById(R.id.categoryPercentage);
            categoryAmount = itemView.findViewById(R.id.categoryAmount);
        }
    }
}

