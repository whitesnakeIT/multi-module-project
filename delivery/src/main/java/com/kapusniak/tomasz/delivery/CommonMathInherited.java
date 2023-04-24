package com.kapusniak.tomasz.delivery;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class CommonMathInherited {
    public static void main(String[] args) {
        long factorial = CombinatoricsUtils.factorial(3);
        System.out.println(factorial);
    }
}
