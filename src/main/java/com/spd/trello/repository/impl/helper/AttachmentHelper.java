package com.spd.trello.repository.impl.helper;

import com.spd.trello.domain.Attachment;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

@Component
public class AttachmentHelper {

    private final JdbcTemplate jdbcTemplate;
    private RowMapper<Attachment> attachmentMapper = (ResultSet resultSet, int rowNum) -> {
        Attachment attachment = new Attachment();
        attachment.setId(UUID.fromString(resultSet.getString("id")));
        attachment.setName(resultSet.getString("name"));
        attachment.setLink(resultSet.getString("link"));
        attachment.setFile(writeBytesToFile(new File("db_files/" + attachment.getName() + ".jpg"),
                resultSet.getBytes("file")));

        UUID cardId = Optional.ofNullable(resultSet.getString("card_id"))
                .map(UUID::fromString).orElse(null);
        if (cardId != null) {
            Card card = new Card();
            card.setId(cardId);
            attachment.setCard(card);
        } else {
            Comment comment = new Comment();
            comment.setId(Optional.ofNullable(resultSet.getString("comment_id"))
                    .map(UUID::fromString).orElse(null));
            attachment.setComment(comment);
        }
        return attachment;
    };

    @Autowired
    public AttachmentHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Attachment create(Attachment attachment, Resource resource) {
        String sql = null;
        if (resource instanceof Card) {
            sql = "insert into attachments(id, link, name, file, card_id) values (?,?,?,?,?)";
        } else {
            sql = "insert into attachments(id, link, name, file, comment_id) values (?,?,?,?,?)";
        }
        return create(attachment, sql, resource.getId());
    }

    private Attachment create(Attachment attachment, String sql, UUID resourceId) {
        jdbcTemplate.update(sql,
                attachment.getId(),
                attachment.getLink(),
                attachment.getName(),
                fileToByte(attachment.getFile()),
                resourceId);
        return findById(attachment.getId());
    }

    public Attachment findById(UUID id) {
        return jdbcTemplate.queryForObject("select * from attachments where id = ?", attachmentMapper, id);
    }


    public boolean delete(UUID id) {
        return jdbcTemplate.update("delete from attachments where id = ?", id) == 1;
    }

    public byte[] fileToByte(File file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes1 = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int readNum; (readNum = fileInputStream.read(bytes1)) != -1; ) {
                byteArrayOutputStream.write(bytes1, 0, readNum);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private File writeBytesToFile(File file, byte[] bytes) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return file;
    }
}