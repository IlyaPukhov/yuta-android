package com.ilyap.yuta.ui.carousel;

import java.util.List;

public class ImageModel {
    private final int carouselNumber;
    private final List<List<Integer>> pagesList;

    public ImageModel(int carouselNumber, List<List<Integer>> pagesList) {
        this.carouselNumber = carouselNumber;
        this.pagesList = pagesList;
    }

    public int getCarouselNumber() {
        return carouselNumber;
    }

    public List<List<Integer>> getPagesList() {
        return pagesList;
    }
}