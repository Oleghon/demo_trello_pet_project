package com.spd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spd.trello.domain.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AbstractControllerTest<E extends Resource> {


    @Autowired
    protected MockMvc mvc;

    public MvcResult create(String uri, E entity) throws Exception {
        String content = mapToJson(entity);
        return mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
               // .andExpect(Medi)
                .andReturn();
    }

    public MvcResult update(String uri, E entity) throws Exception {
        String content = mapToJson(entity);
        return mvc.perform(put(uri + "/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult readById(String uri, UUID id) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(uri + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult getAll(String uri) throws Exception {
        return mvc.perform(get(uri + "?page=0"))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult delete(String uri, UUID id) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.delete(uri + "/{id}", id))
                .andReturn();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected Object getValue(MvcResult mvcResult, String jsonPath) throws UnsupportedEncodingException {
        return JsonPath.read(mvcResult.getResponse().getContentAsString(), jsonPath);
    }

    protected E mapFromJson(String json, Class<E> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
