package com.kapusniak.tomasz.domain;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class CommonMath {
    public static void main(String[] args) {
        long factorial = CombinatoricsUtils.factorial(3);
        System.out.println(factorial);
    }
}
