package com.spd.trello.service.attachment_helper;

import com.spd.trello.domain.items.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CommonAttachmentService {

    Attachment load(MultipartFile file, UUID keyId);

    Attachment readById(UUID id);
}
