package com.mycode.goran.flags;


import android.os.Parcel;
import android.os.Parcelable;

public class style implements Parcelable {
    public static final int EASY_MODE_NUM = 38; // NUMBER OF QUESTION IN EASY MODE
    public static final int MEDIUM_MODE_NUM = 58;
    public static final int HARD_MODE_NUM = 108;
    public  static final int HARDEST_MODE_NUM = 208;

    protected style(Parcel in) {
    }

    public static final Creator<style> CREATOR = new Creator<style>() {
        @Override
        public style createFromParcel(Parcel in) {
            return new style(in);
        }

        @Override
        public style[] newArray(int size) {
            return new style[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(flags);


    }

    public enum MODE{
        Easy,
        Medium,
        Hard,
        Hardest,
    }
}
