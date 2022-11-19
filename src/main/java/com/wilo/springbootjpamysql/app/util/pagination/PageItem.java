package com.wilo.springbootjpamysql.app.util.pagination;

public class PageItem {

    private final int num;
    private final boolean actual;

    public PageItem(int num, boolean actual) {
        this.num = num;
        this.actual = actual;
    }

    public int getNum() {
        return num;
    }

    public boolean isActual() {
        return actual;
    }
}
