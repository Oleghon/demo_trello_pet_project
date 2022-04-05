package com.spd.trello.service;

import com.spd.trello.configuration.StorageProperties;
import com.spd.trello.domain.items.Attachment;
import com.spd.trello.service.attachment_helper.AttachmentDBService;
import com.spd.trello.service.attachment_helper.AttachmentLocalService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AttachmentService {

    private AttachmentDBService dbService;
    private AttachmentLocalService localService;
    private StorageProperties storageProperties;

    public AttachmentService(StorageProperties storageProperties, AttachmentDBService dbService, AttachmentLocalService localService) {
        this.storageProperties = storageProperties;
        this.dbService = dbService;
        this.localService = localService;
    }

    public Attachment save(MultipartFile file) throws IOException {
        if (storageProperties.getLocation().equals("db"))
            return dbService.load(file);
        return localService.load(file);
    }

    public Attachment readById(UUID id) {
        if (storageProperties.getLocation().equals("db"))
            return dbService.readById(id);
        return localService.readById(id);

    }
}
