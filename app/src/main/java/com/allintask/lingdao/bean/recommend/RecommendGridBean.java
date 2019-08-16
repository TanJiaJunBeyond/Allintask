package com.allintask.lingdao.bean.recommend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TanJiaJun on 2018/6/4.
 */

public class RecommendGridBean implements Parcelable {

    public int categoryId;
    public String categoryName;
    public String homeIconUrl;

    public RecommendGridBean() {

    }

    protected RecommendGridBean(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
        homeIconUrl = in.readString();
    }

    public static final Creator<RecommendGridBean> CREATOR = new Creator<RecommendGridBean>() {
        @Override
        public RecommendGridBean createFromParcel(Parcel in) {
            return new RecommendGridBean(in);
        }

        @Override
        public RecommendGridBean[] newArray(int size) {
            return new RecommendGridBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
        dest.writeString(homeIconUrl);
    }

}
