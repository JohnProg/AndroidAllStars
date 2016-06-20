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
package com.belatrixsf.connect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gyosida on 4/12/16.
 */
public class Notification implements Parcelable {



    private String date;
    private String detail;
    @SerializedName("to_user")
    private Employee toUser;
    @SerializedName("from_user")
    private Employee fromUser;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Employee getToUser() {
        return toUser;
    }

    public void setToUser(Employee toUser) {
        this.toUser = toUser;
    }

    public Employee getFromUser() {
        return fromUser;
    }

    public void setFromUser(Employee fromUser) {
        this.fromUser = fromUser;
    }

    protected Notification(Parcel in) {
        date = in.readString();
        detail = in.readString();
        toUser = (Employee) in.readValue(Employee.class.getClassLoader());
        fromUser = (Employee) in.readValue(Employee.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(detail);
        dest.writeValue(toUser);
        dest.writeValue(fromUser);
    }

    @SuppressWarnings("unused")
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

}
