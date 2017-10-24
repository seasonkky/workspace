package com.nexgo.xtms.data.entity;

/**
 * Created by zhouxie on 2017/9/26.
 */

public class LanguageModel {
    private int index;
    private String name;
    private String tag;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "LanguageModel{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", selected=" + selected +
                '}';
    }
}
