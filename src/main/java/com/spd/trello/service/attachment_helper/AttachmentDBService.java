package com.spd.trello.service.attachment_helper;

import com.spd.trello.domain.items.Attachment;
import com.spd.trello.repository_jpa.AttachmentRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class AttachmentDBService extends AbstractAttachmentService{

    public AttachmentDBService(AttachmentRepository repository) {
        super(repository);
    }

    @Override
    public Attachment load(MultipartFile file, UUID keyId) {
        Attachment attachment = convert(file, keyId);
        try {
            attachment.setFile(file.getBytes());
            return repository.save(attachment);
        } catch (IOException e) {
            throw new IllegalArgumentException("File not added to db", e);
        }
    }

}
