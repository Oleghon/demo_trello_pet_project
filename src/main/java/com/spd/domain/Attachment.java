package com.spd.domain;

import java.io.File;

public class Attachment {
    private String link;
    private String name;
    private File file;

    public Attachment() {}

    public Attachment(String link, String name, File file) {
        this.link = link;
        this.name = name;
        this.file = file;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
