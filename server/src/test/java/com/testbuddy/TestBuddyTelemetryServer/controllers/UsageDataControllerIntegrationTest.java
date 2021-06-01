package com.testbuddy.TestBuddyTelemetryServer.controllers;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import java.io.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsageDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String PATH = "/usagedata";
    private final String USER_ID = "123e4567-e89b-12d3-a456-426614174000";
    private final String ACTION_ID = "testAdd";
    private final LocalDateTime TIME = LocalDateTime.now();
    private final String HASH = "79054025255fb1a26e4bc422aef54eb4";

    private UsageDataDto usageDataDto;

    @BeforeEach
    public void setup() {
        ActionEventDto actionEventDto = new ActionEventDto(ACTION_ID, TIME);
        List<ActionEventDto> actionEventDtoList = new ArrayList<>();
        actionEventDtoList.add(actionEventDto);
        usageDataDto = new UsageDataDto(USER_ID, actionEventDtoList, HASH);
    }

    @Test
    public void testSuccessfulRequest() throws Exception {
        performRequest(PATH, usageDataDto, status().isOk());
    }

    /**
     * Turns an Java object to a JSON string.
     *
     * @param obj the object to parse.
     * @return a JSON string representing the object.
     * @throws IOException thrown in case the parsing fails.
     */
    public String toJsonString(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * Performs an HTTP request using MockMvc.
     *
     * @param path        the url path to make the request.
     * @param requestDto  the DTO to send.
     * @param expectation the expected returned HTTP status code.
     * @return the returned ResultActions object of the mockMvc request.
     * @throws Exception thrown in case something goes wrong with the request or the parsing of the DTO.
     */
    public ResultActions performRequest(String path, UsageDataDto requestDto,
                                        ResultMatcher expectation) throws Exception {
        return this.mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJsonString(requestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(expectation);
    }

}