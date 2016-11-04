package com.stveo.stevebowling.budget.Network;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by stevebowling on 10/31/16.
 */

//sets your token so you can get your request?
// checks to see if your request has a token , if not , sticks one in.
public class SessionRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request= chain.request();

        if (UserStore.getInstance().getToken()!=null){
            Request.Builder builder=  request.newBuilder();
                    builder.header("Authorization", "Bearer "+
                    UserStore.getInstance().getToken());
            request = builder.build();

        }
        return chain.proceed(request);
    }
}

