package com.kapusniak.tomasz.service;

import org.springframework.stereotype.Service;

@Service
public class PagingService {

    public static final Integer PAGE_SIZE = 2;

    public Integer validatePageNumber(Integer page) {
        Integer pageNumber;
        if (page == null || page < 0) {
            pageNumber = 0;
        } else {
            pageNumber = page;
        }
        return pageNumber;
    }
}
