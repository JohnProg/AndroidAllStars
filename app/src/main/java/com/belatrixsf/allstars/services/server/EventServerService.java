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
package com.belatrixsf.allstars.services.server;

import com.belatrixsf.allstars.networking.retrofit.api.EventAPI;
import com.belatrixsf.allstars.networking.retrofit.responses.EventListResponse;
import com.belatrixsf.allstars.services.AllStarsBaseService;
import com.belatrixsf.allstars.services.ServerServiceRequest;
import com.belatrixsf.allstars.services.ServiceRequest;
import com.belatrixsf.allstars.services.contracts.EventService;
import com.belatrixsf.allstars.utils.AllStarsCallback;

import retrofit2.Call;


/**
 * Created by icerrate on 13/06/2016.
 */
public class EventServerService extends AllStarsBaseService implements EventService {

    private EventAPI eventAPI;

    public EventServerService(EventAPI eventAPI) {
        this.eventAPI = eventAPI;
    }

    @Override
    public ServiceRequest getEventList(Integer page, AllStarsCallback<EventListResponse> callback) {
        Call<EventListResponse> call = eventAPI.getEventList(page);
        ServiceRequest<EventListResponse> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
        return serviceRequest;
    }
}