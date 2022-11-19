package com.wilo.springbootjpamysql.app.util.pagination;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageRender<T> {

    private String url;
    private Page<T> page;

    private int totalPage;
    private int numElementByPage;
    private int pageActual;

    private List<PageItem> pageItems;


    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pageItems = new ArrayList<PageItem>();

        numElementByPage = page.getSize();
        totalPage = page.getTotalPages();
        pageActual = page.getNumber() + 1;

        int desde, hasta;
        if (totalPage <= numElementByPage) {
            desde = 1;
            hasta = totalPage;
        } else {
            if (pageActual <= numElementByPage / 2) {
                desde = 1;
                hasta = numElementByPage;
            } else if (pageActual >= totalPage - numElementByPage / 2) {
                desde = totalPage - numElementByPage + 1;
                hasta = numElementByPage;
            } else {
                desde = pageActual - numElementByPage / 2;
                hasta = numElementByPage;
            }
        }

        for (int i = 0; i < hasta; i++) {
            pageItems.add(new PageItem(desde + i, pageActual == desde + i));
        }
    }

    public String getUrl() {
        return url;
    }

    public Page<T> getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getNumElementByPage() {
        return numElementByPage;
    }

    public int getPageActual() {
        return pageActual;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }
}


