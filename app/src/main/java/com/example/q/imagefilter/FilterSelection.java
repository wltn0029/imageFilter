package com.example.q.imagefilter;

import hu.don.easylut.filter.Filter;

class FilterSelection {

    public String name;
    public Filter filter;

    public FilterSelection(String name, Filter filter) {
        this.name = name;
        this.filter = filter;
    }

}