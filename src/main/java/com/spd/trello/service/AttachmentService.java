package com.spd.trello.service;

import com.spd.trello.domain.items.Attachment;
import com.spd.trello.service.attachment_helper.CommonAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AttachmentService {

    private CommonAttachmentService helperService;

    @Autowired
    public AttachmentService(@Qualifier(value = "localStorage") CommonAttachmentService helperService) {
        this.helperService = helperService;
    }

    public Attachment save(MultipartFile file) throws IOException {
        return helperService.load(file);
    }

    public Attachment readById(UUID id) {
        return helperService.readById(id);
    }
}
