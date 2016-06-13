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
package com.belatrixsf.allstars.ui.event;

import com.belatrixsf.allstars.entities.Event;
import com.belatrixsf.allstars.networking.retrofit.responses.EventListResponse;
import com.belatrixsf.allstars.networking.retrofit.responses.PaginatedResponse;
import com.belatrixsf.allstars.services.ServiceRequest;
import com.belatrixsf.allstars.services.contracts.EventService;
import com.belatrixsf.allstars.ui.common.AllStarsPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by icerrate on 13/06/2016.
 */
public class EventListPresenter extends AllStarsPresenter<EventListView> {

    private EventService eventService;
    private List<Event> events = new ArrayList<>();
    private PaginatedResponse eventsPaging = new PaginatedResponse();
    private ServiceRequest searchingServiceRequest;

    @Inject
    public EventListPresenter(EventListView view, EventService eventsService) {
        super(view);
        this.eventService = eventsService;
    }

    public void onEventClicked(Object object) {
        if (object != null && object instanceof Event) {
            Event event = (Event) object;
            view.goEventDetail(event.getId());
        }
    }

    public void callNextPage() {
        if (eventsPaging.getNext() != null) {
            getEventsInternal();
        }
    }

    public void getEvents() {
        view.resetList();
        if (events.isEmpty()) {
            getEventsInternal();
        } else {
            view.addEvents(events);
        }
    }

    private void getEventsInternal() {
        view.showProgressIndicator();
        searchingServiceRequest = eventService.getEventList(
                eventsPaging.getNextPage(),
                new PresenterCallback<EventListResponse>() {
                    @Override
                    public void onSuccess(EventListResponse eventListResponse) {
                        eventsPaging.setCount(eventListResponse.getCount());
                        eventsPaging.setNext(eventListResponse.getNext());
                        events.addAll(eventListResponse.getEventList());
                        view.addEvents(eventListResponse.getEventList());
                        view.hideProgressIndicator();
                    }
                });
    }

    @Override
    public void cancelRequests() {
        eventService.cancelAll();
    }

    private void reset() {
        events.clear();
        view.resetList();
        eventsPaging.reset();
    }

    // saving state stuff

    public void load(List<Event> events, PaginatedResponse contactsPaging) {
        if (events != null) {
            this.events.addAll(events);
        }
        this.eventsPaging = contactsPaging;
    }

    public PaginatedResponse getEventsPaging() {
        return eventsPaging;
    }

    public List<Event> getEventsSync() {
        return events;
    }

}