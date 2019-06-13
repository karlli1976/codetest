package com.optus.candidate.codetest.controller;

import com.optus.candidate.codetest.services.CountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class SearchControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  CountService mockCountService;

  private final static String SEARCH_URL = "/counter-api/search";
  private final static String HTTP_BASIC = "Basic " + Base64Utils.encodeToString("optus:candidates".getBytes());

  @BeforeEach
  void setUp() throws Exception {
    when(mockCountService.getCount("abc")).thenReturn(888);
  }


  @Test
  void search_withoutAuth_isUnauthorized() throws Exception {
    String searchJson = "{\"searchText\":[\"Duis\", \"Sed\", \"\", \"Augue\", \"Pellentesque\", \"abc\"]}";

    mockMvc.perform(post(SEARCH_URL)
      .content(searchJson)
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isUnauthorized());
  }

  @Test
  void search_withValidParam_isSuccessful() throws Exception {
    String searchJson = "{\"searchText\":[\"notme\", \"\", \"Donec\", \"Augue\", \"Pellentesque\", \"abc\"]}";

    mockMvc.perform(
      post(SEARCH_URL)
        .content(searchJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(jsonPath("$.counts[-1:].abc", hasItem(888))
      );
  }

  @Test
  void search_withEmptyParam_isBadRequest() throws Exception {
    String searchJson = "{\"searchText\":\"\"}";

    mockMvc.perform(
      post(SEARCH_URL)
        .content(searchJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void search_withInvalidParam_isBadRequest() throws Exception {
    String searchJson = "{\"\":\"\"}";

    mockMvc.perform(
      post(SEARCH_URL)
        .content(searchJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

}