package com.spd.trello.domain;

import lombok.Data;
import java.io.File;

@Data
public class Attachment {
    private String link;
    private String name;
    private File file;
}
