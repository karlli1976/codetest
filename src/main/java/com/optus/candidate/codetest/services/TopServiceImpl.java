package com.optus.candidate.codetest.services;

import com.optus.candidate.codetest.model.TopWord;
import com.optus.candidate.codetest.repository.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TopServiceImpl implements TopService {

  private Logger log = LoggerFactory.getLogger(TopServiceImpl.class);

  @Autowired
  WordRepository wordRepo;

  // for sample text => sample word
  private String[] words = null;

  // to count occurrence
  private String[] noDupWords = null;

  /**
   * Return the top N texts, which has the highest counts (case-insensitive) in the
   * sample text.
   * <p>
   * Return null if topCount is invalid (0/negative) or sample text not found or empty
   *
   * @param topCount the number of top-word
   * @return String csv
   */
  @Override
  public String getTop(int topCount) {
    log.debug("start: topCount[{}]", topCount);

    if (topCount <= 0) {
      log.debug("end: result[null]");

      return null;
    }

    StringBuffer rlt = new StringBuffer();

    ConcurrentMap<String, Integer> wordPool = null;
    try {
      // perform word count statistic
      wordPool = Stream.of(
        wordRepo
          .getSampleText()
          .toUpperCase()
          .split("\\W+"))
        .collect(
          Collectors.toConcurrentMap(x -> x, x -> 1, Integer::sum));

      // sort word pool by value with limit
      wordPool
        .entrySet()
        .stream()
        .sorted(
          Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(topCount)
        .forEachOrdered(
          x -> rlt.append(
            String.format("%s|%d\n",
              x.getKey(),
              x.getValue())
          ));
    } catch (IOException ioe){
      log.error(ioe.getMessage());
      log.debug("end: result[null]");

      return null;
    }

    log.debug("end: result[{}]", rlt.toString());

    return rlt.toString();
  }


  /**
   * Return TopWord object to indicate word-occurrence (case-sensitive) in sample text.
   *
   * @param word word to count
   * @return null if word is null/empty/blank, otherwise TopWord object.
   */
  private TopWord countOccurrence(String word) {
    log.debug("start: word[{}]", word);

    if (null == word || word.trim().isEmpty()) {
      log.debug("end: rlt[null]");
      return null;
    }

    TopWord rlt = new TopWord(word, 0);
    for (String sampleWord : words) {
//      log.debug("sampleWord[{}]", sampleWord);

      if (sampleWord.equalsIgnoreCase(word)) {
        rlt.incOccurrence();
      }
    }

    log.debug("end: rlt[{}]", rlt);

    return rlt;
  }
}
