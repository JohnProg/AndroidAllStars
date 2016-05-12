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

package com.belatrixsf.allstars.services;

import android.os.NetworkOnMainThreadException;

import com.belatrixsf.allstars.networking.retrofit.RetrofitCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;

/**
 * @author Carlos Piñan
 */
public abstract class AllStarsService {

    private static List<Call> callList;
    public static boolean cancelling = false;

    protected static void enqueue(Call call, RetrofitCallback callback) {
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
        call.enqueue(callback);
    }

    public static void cancel() {
        if (callList != null && !callList.isEmpty()) {
            cancelling = true;
            try {
                for (Call call : callList) {
                    call.cancel();
                }
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
            callList.clear();
        }
    }
}
