package com.aditya.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by amodi on 3/18/17.
 */

public class Filter implements Parcelable {

    public Date beginDate;
    public String sortOrder;
    public List<String> newsDesks;

    public Filter() {

    }

    public Filter(Date beginDate, String sortOrder, List<String> newsDesks) {
        this.beginDate = beginDate;
        this.sortOrder = sortOrder;
        this.newsDesks = newsDesks;
    }

    protected Filter(Parcel in) {
        beginDate = (Date)in.readSerializable();
        sortOrder = in.readString();
        newsDesks = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(beginDate);
        parcel.writeString(sortOrder);
        parcel.writeList(newsDesks);
    }

    public enum NewsDeskType {
        ARTS("ARTS"), SPORTS("SPORTS"), FASHION_N_STYLE("Fashion & Style");
        public String value;

        private NewsDeskType(String value) {
            this.value = value;
        }
    }

    public enum SortOrder {
        NEWEST(0), OLDEST(1);
        public int value;

        private SortOrder(int value) {
            this.value = value;
        }
    }
}
