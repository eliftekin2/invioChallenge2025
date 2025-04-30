package com.eliftekin.inviochallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityListModel {
    @SerializedName("totalPage")
    @Expose
    private int totalPage;

    @SerializedName("data")
    @Expose
    private List<DataList> dataList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }
}
