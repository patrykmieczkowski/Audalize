package com.mieczkowskidev.audalize.model;

/**
 * Created by Patryk Mieczkowski on 2016-02-10.
 */
public class MediaFile {

    private String path;
    private String title;
    private boolean isChecked;

    public MediaFile() {
    }

    public MediaFile(String path, String title, boolean isChecked) {
        this.path = path;
        this.title = title;
        this.isChecked = isChecked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
