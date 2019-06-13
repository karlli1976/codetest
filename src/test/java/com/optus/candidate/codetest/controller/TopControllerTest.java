package com.optus.candidate.codetest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@PropertySource("classpath:application.properties")
class TopControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Value("${top.count.min}")
  private int MIN_COUNT;

  @Value("${top.count.max}")
  private int MAX_COUNT;

//  @MockBean
//  TopService mockTopService;

  private final static String TOP_URL = "/counter-api/top";
  private final static String HTTP_BASIC = "Basic " + Base64Utils.encodeToString("optus:candidates".getBytes());


  @Test
  void top_withValidParam_isSuccessful() throws Exception {
    int topCount = MIN_COUNT + 1;

    mockMvc.perform(
      get(TOP_URL + "/" + topCount)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().string(containsString("|")));
  }

  @Test
  void top_withoutAuth_isUnauthorized() throws Exception {

    mockMvc.perform(
      get(TOP_URL + "/5")
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().isUnauthorized());
  }

  @Test
  void top_withBelowMinParam_isBadRequest() throws Exception {
    int topCount = MIN_COUNT - 1;

    mockMvc.perform(
      get(TOP_URL + "/" + topCount)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void top_withUponMaxParam_isBadRequest() throws Exception {
    int topCount = MAX_COUNT + 1;

    mockMvc.perform(
      get(TOP_URL + "/" + topCount)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void top_withoutParam_isNotFound() throws Exception {

    mockMvc.perform(
      get(TOP_URL)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  void top_withMinParam_isSuccessful() throws Exception {

    int topCount = MIN_COUNT;

    mockMvc.perform(
      get(TOP_URL + "/" + topCount)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().string(containsString("|")));
  }

  @Test
  void top_withMaxParam_isSuccessful() throws Exception {

    int topCount = MAX_COUNT;

    mockMvc.perform(
      get(TOP_URL + "/" + topCount)
        .header(HttpHeaders.AUTHORIZATION, HTTP_BASIC)
        .header(HttpHeaders.ACCEPT, "text/csv"))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().string(containsString("|")));
  }

}