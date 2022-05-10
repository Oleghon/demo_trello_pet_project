package com.spd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spd.trello.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class AbstractControllerTest<E extends Resource> {


    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper mapper = new ObjectMapper();

    public MvcResult create(String uri, E entity) throws Exception {
        String content = mapToJson(entity);
        return mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andReturn();
    }

    public MvcResult update(String uri, E entity) throws Exception {
        String content = mapToJson(entity);
        return mvc.perform(put(uri + "/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andReturn();
    }

    public MvcResult readById(String uri, UUID id) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(uri + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    public MvcResult getAll(String uri) throws Exception {
        return mvc.perform(get(uri + "?page=0"))
                .andReturn();
    }

    public MvcResult delete(String uri, UUID id) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.delete(uri + "/{id}", id))
                .andReturn();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    protected Object getValue(MvcResult mvcResult, String jsonPath) throws UnsupportedEncodingException {
        return JsonPath.read(mvcResult.getResponse().getContentAsString(), jsonPath);
    }

    protected E mapFromJson(String json, Class<E> clazz) throws JsonProcessingException {
        return this.mapper.readValue(json, clazz);
    }
}
