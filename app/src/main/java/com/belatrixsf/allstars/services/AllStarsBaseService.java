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


import com.belatrixsf.allstars.services.contracts.AllStarsService;
import com.belatrixsf.allstars.utils.AllStarsCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Carlos Piñan
 */
public abstract class AllStarsBaseService implements AllStarsService {

    private List<ServiceRequest> serviceRequestList;

    protected <T> void enqueue(ServiceRequest<T> serviceRequest, AllStarsCallback<T> allStarsCallback) {
        if (serviceRequestList == null) {
            serviceRequestList = new ArrayList<>();
        }
        serviceRequestList.add(serviceRequest);
        serviceRequest.enqueue(allStarsCallback);
    }

    @Override
    public void cancelAll() {
        if (serviceRequestList != null && !serviceRequestList.isEmpty()) {
            for (ServiceRequest serviceRequest : serviceRequestList) {
                serviceRequest.cancel();
            }
            serviceRequestList.clear();
        }
    }

}