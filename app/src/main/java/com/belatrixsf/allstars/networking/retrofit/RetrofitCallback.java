/* The MIT License (MIT)
* Copyright (c) 2016 BELATRIX
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.belatrixsf.allstars.networking.retrofit;

import android.util.Log;

import com.belatrixsf.allstars.utils.AllStarsCallback;
import com.belatrixsf.allstars.utils.ServiceError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.belatrixsf.allstars.utils.ServiceError.*;

/**
 * Created by gyosida on 4/12/16.
 */
public class RetrofitCallback<T> implements Callback<T> {

    private AllStarsCallback<T> callback;

    public RetrofitCallback(AllStarsCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            callback.onSuccess(response.body());
        } else {
            ServiceError serviceError = null;
            try {
                if (response.errorBody() != null) {
                    Gson gson = new Gson();
                    serviceError = gson.fromJson(response.errorBody().string(), ServiceError.class);
                    serviceError.setResponseCode(response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (serviceError == null) {
                serviceError = new ServiceError(UNKNOWN, "Be sure everything is fine.");
            }
            callback.onFailure(serviceError);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        // TODO handle exception
        Log.d("RetrofitCallback", "onFailure: " + t.getMessage());
        callback.onFailure(new ServiceError(UNKNOWN, "Unknown error"));
    }
}
