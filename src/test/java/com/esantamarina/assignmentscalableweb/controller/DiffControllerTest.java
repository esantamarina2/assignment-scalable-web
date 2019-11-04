package com.esantamarina.assignmentscalableweb.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DiffControllerTest {

    private static final String ROOT_DIFF_PATH = "/v1/diff";
    private static final String PUT_LEFT_DIFF_PATH = ROOT_DIFF_PATH + "/1/left";
    private static final String PUT_RIGHT_DIFF_PATH = ROOT_DIFF_PATH + "/1/right";
    private static final String GET_DIFF_PATH = ROOT_DIFF_PATH + "/1";

    private static final String EXAMPLE_BODY_1 = "{\"data\":\"AQ==\"}";
    private static final String EXAMPLE_BODY_2 = "{\"data\":\"BQ==\"}";
    private static final String BAD_REQUEST = "{\"data\":\"testing\"}";

    @Autowired
    private MockMvc mvc;

    /**
        Scenario: Insert diff left part with ID = 1
        Update diff left part with ID = 1
        Expected: OK Status
     */
    @Test
    public void creates_and_updates_left_part() throws Exception {
        create_and_updates_part(PUT_LEFT_DIFF_PATH);
    }

    /**
        Scenario: Insert diff right part with ID = 1
        Update diff right part with ID = 1
        Expected: OK Status
   */
    @Test
    public void creates_and_updates_right_part() throws Exception {
        create_and_updates_part(PUT_RIGHT_DIFF_PATH);
    }

    private void create_and_updates_part(String path) throws Exception {
        //create
        mvc.perform(put(path).contentType(APPLICATION_JSON).content(EXAMPLE_BODY_1))
                .andExpect(status().isOk());

        //update
        mvc.perform(put(path).contentType(APPLICATION_JSON).content(EXAMPLE_BODY_2))
                .andExpect(status().isOk());
    }

    /**
        Scenario: Get diff using ID = 1 after insert both parts
        Expected: Bytes difference were found
    */
    @Test
    public void get_diff() throws Exception {
        mvc.perform(put(PUT_LEFT_DIFF_PATH).contentType(APPLICATION_JSON).content(EXAMPLE_BODY_1))
                .andExpect(status().isOk());

        mvc.perform(put(PUT_RIGHT_DIFF_PATH).contentType(APPLICATION_JSON).content(EXAMPLE_BODY_2))
                .andExpect(status().isOk());

        mvc.perform(get(GET_DIFF_PATH).contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.left").value("AQ=="))
                .andExpect(jsonPath("$.right").value("BQ=="))
                .andExpect(jsonPath("$.diff").value("Left and Right have the same length, but 1 byte(s) are different"));
    }

    /**
        Scenario: Insert diff left part with ID = 1 and bad content
        Expected: Bad Request Status
    */
    @Test
    public void handle_bad_request() throws Exception {
        mvc.perform(put(PUT_LEFT_DIFF_PATH).contentType(APPLICATION_JSON).content(BAD_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    /**
        Scenario: Get diff using ID = 1 without insert both parts
        Expected: Pre condition failed
    */
    @Test
    public void handle_missing_part() throws Exception {
        mvc.perform(get(GET_DIFF_PATH).contentType(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }
}
