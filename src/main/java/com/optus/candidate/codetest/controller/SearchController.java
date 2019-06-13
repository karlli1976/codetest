package com.optus.candidate.codetest.controller;

import com.optus.candidate.codetest.services.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {

  private Logger log = LoggerFactory.getLogger(SearchController.class);

  private final static String SEARCH_TEXT = "searchText";
  private final static String COUNTS = "counts";

  @Autowired
  CountService countService;

  //  @ResponseBody
  @PostMapping("/counter-api/search")
  public Map<String, ArrayList> count(@RequestBody Map<String, ArrayList<String>> searchText) {
    log.debug("SearchText[{}]", searchText);

    ArrayList<Map<String, Integer>> words = new ArrayList<>();

    for (String word : searchText.get(SEARCH_TEXT)) {
      HashMap<String, Integer> map = new HashMap<>();

      // bypass null word
      if (null == word || word.isEmpty()) {
        continue;
      }

      map.put(word, countService.getCount(word));
      words.add(map);
    }

    Map<String, ArrayList> rlt = new HashMap<>();
    rlt.put(COUNTS, words);

    return rlt;
  }
}
