package com.brian.taotao.common.utils;

import java.util.Random;

public class IDUtil {

    public static long itemID() {
        long currentTimeMillis = System.currentTimeMillis();
        Random random = new Random();
        int i = random.nextInt(99);
        String str = currentTimeMillis + String.format("%02d", i);
        return new Long(str);
    }

    public static void main(String[] args) {
        System.out.println(IDUtil.itemID());
    }
}
