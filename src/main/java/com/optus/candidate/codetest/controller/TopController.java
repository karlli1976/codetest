package com.optus.candidate.codetest.controller;

import com.optus.candidate.codetest.services.TopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

@RestController
public class TopController {
  private Logger log = LoggerFactory.getLogger(TopController.class);

  @Autowired
  TopService topService;

  @Value("${top.count.min}")
  private int MIN_COUNT;

  @Value("${top.count.max}")
  private int MAX_COUNT;

  @RequestMapping(value = "/counter-api/top/{count}", produces = "text/csv")
  @ResponseBody
  public String top(@PathVariable("count") @NotBlank int count, HttpServletResponse response) {

    if (count < MIN_COUNT || MAX_COUNT < count) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    // from array to csv
    String csv = topService.getTop(count);

    if (null == csv) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    else
    {
      // set csv name
      String headerKey = "Content-Disposition";
      String headerValue = "attachment; filename=top.csv";
      response.setContentType("text/csv");
      response.setHeader(headerKey, headerValue);
    }

    return csv;
  }

}
