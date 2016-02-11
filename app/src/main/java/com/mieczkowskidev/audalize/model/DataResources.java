package com.mieczkowskidev.audalize.model;

import java.util.Date;

/**
 * Created by Patryk Mieczkowski on 2016-02-09
 */
public class DataResources {

    private int id;
    private String source;
    private String name;
    private String content;
//    private String toneAnalysis;
    private Date created;

    public DataResources() {
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

//    public String getToneAnalysis() {
//        return toneAnalysis;
//    }

    public Date getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "DataResources{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                '}';
    }
}
