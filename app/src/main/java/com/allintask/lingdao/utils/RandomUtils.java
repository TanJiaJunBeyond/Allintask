package com.allintask.lingdao.utils;

import java.util.Random;

/**
 * Created by TanJiaJun on 2018/2/11.
 */

public class RandomUtils {

    public static int[] randomArray(int minNumber, int maxNumber, int n) {
        int len = maxNumber - minNumber + 1;

        if (minNumber > maxNumber || n > len) {
            return null;
        }

        int[] source = new int[len];

        for (int i = minNumber; i < minNumber + len; i++) {
            source[i - minNumber] = i;
        }

        int[] randomArray = new int[n];
        Random rd = new Random();
        int index;

        for (int i = 0; i < randomArray.length; i++) {
            index = Math.abs(rd.nextInt() % len--);
            randomArray[i] = source[index];
            source[index] = source[len];
        }
        return randomArray;
    }

}
