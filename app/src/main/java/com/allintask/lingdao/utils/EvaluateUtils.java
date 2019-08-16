package com.allintask.lingdao.utils;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public class EvaluateUtils {

    public static String getEvaluation(float evaluateStar) {
        String evaluation = null;

        if (evaluateStar == 1.0F) {
            evaluation = "非常差";
        } else if (evaluateStar == 2.0F) {
            evaluation = "差";
        } else if (evaluateStar == 3.0F) {
            evaluation = "一般";
        } else if (evaluateStar == 4.0F) {
            evaluation = "好";
        } else if (evaluateStar == 5.0F) {
            evaluation = "非常好";
        }
        return evaluation;
    }

}
