package com.kapusniak.tomasz.infrastructure;

import org.apache.commons.lang3.tuple.Triple;

public class CommonLang3Inherited {

    public static void main(String[] args) {
        Triple<String, String, String> stringTriple = Triple.of("s1", "s2", "s3");
        System.out.println(stringTriple);
    }
}
