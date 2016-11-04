package com.stveo.stevebowling.budget.Network;

import com.stveo.stevebowling.budget.Models.Category;
import com.stveo.stevebowling.budget.Models.Expense;
import com.stveo.stevebowling.budget.Models.TestPost;
import com.stveo.stevebowling.budget.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by stevebowling on 10/31/16.
 */

public interface ApiService {
    @GET("/posts/{id}")
    Call<TestPost> getPost(@Path("id") Integer id);

    @POST("/posts")
    Call<TestPost> postPost(@Body TestPost post);

    @GET("/posts")
    Call<TestPost[]> getPosts();

    @POST("auth")
    Call<User>login(@Body User user);

    @POST("register")
    Call<User>register(@Body User user);

    @POST("api/category/createCategory")
    Call<Void> addCategory(@Body Category category);

    @GET("api/category/getCategory/year/{year}/month/{month}/day/{day}")
    Call<Category[]> getWeekCategories(@Path("year") Integer year,
                                       @Path("month") Integer month,
                                       @Path("day") Integer day);

    @GET("api/category/getCategory/year/{year}/month/{month}")
    Call<Category[]> getMonthCategories(@Path("year") Integer year,
                                       @Path("month") Integer month);

    @POST("api/expense/createExpense")
    Call<Expense> addExpenses(@Body Expense expense);

   @GET("api/expense/getExpenses/categoryId/{categoryId}")
    Call<Expense[]> getRecentExpense(@Path("categoryId") Integer categoryId);

    @GET("api/expense/getExpenses/year/{year}/month/{month}")
       Call<Expense[]> getMonthExpenses(@Path("month") Integer month,
                                        @Path("year") Integer year);
}
