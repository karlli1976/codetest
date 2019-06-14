package com.optus.candidate.codetest.services;

import com.optus.candidate.codetest.repository.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CountServiceImpl implements CountService {

  private Logger log = LoggerFactory.getLogger(CountServiceImpl.class);

  @Autowired
  private WordRepository wordRepo;

  @Override
  /**
   * Return the occurrence of input word (case-insensitive).
   *
   * Return 0 if input is empty (null/"") or sample text error, otherwise the occurrence.
   */
  public int getCount(String word) {
    log.debug("start: word[{}]", word);

    int rlt = 0;

    if (null == word || word.isEmpty()) {
      log.debug("end: rlt[{}]", rlt);

      return rlt;
    }

    try {
      String sample = wordRepo.getSampleText();
      if (sample != null) {
        rlt = sample.toUpperCase().split(word.toUpperCase()).length - 1;
      }
    } catch (IOException ioe) {
      log.error(ioe.getMessage());

      rlt = 0;
    }

    log.debug("end: rlt[{}]", rlt);

    return rlt;
  }
}
