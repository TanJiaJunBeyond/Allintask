package com.allintask.lingdao.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TanJiaJun on 2018/7/11.
 */

public class GetResumeCompleteRateBean implements Parcelable {

    public Integer resumeCompleteRate;

    public GetResumeCompleteRateBean() {

    }

    protected GetResumeCompleteRateBean(Parcel in) {
        if (in.readByte() == 0) {
            resumeCompleteRate = null;
        } else {
            resumeCompleteRate = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (resumeCompleteRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(resumeCompleteRate);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetResumeCompleteRateBean> CREATOR = new Creator<GetResumeCompleteRateBean>() {
        @Override
        public GetResumeCompleteRateBean createFromParcel(Parcel in) {
            return new GetResumeCompleteRateBean(in);
        }

        @Override
        public GetResumeCompleteRateBean[] newArray(int size) {
            return new GetResumeCompleteRateBean[size];
        }
    };

}
