package com.kapusniak.tomasz.application;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class CommonMath {
    public static void main(String[] args) {
        long factorial = CombinatoricsUtils.factorial(4);
        System.out.println(factorial);
    }
}
