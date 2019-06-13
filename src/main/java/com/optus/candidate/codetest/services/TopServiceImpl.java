package com.optus.candidate.codetest.services;

import com.optus.candidate.codetest.model.TopWord;
import com.optus.candidate.codetest.repository.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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


    // init words
    if (null == words) {
      String sampleText = wordRepo.getSampleText();

      if (null == sampleText || sampleText.trim().isEmpty()) {
        // confirm sample text is valid
        log.error("sample text not found or empty");
        log.debug("end: result[null]");

        return null;
      }

      // case-insensitive
      sampleText = sampleText.trim().toUpperCase();
      Vector<String> vecWords = new Vector<>();
      Set<String> set = new HashSet<>();

      for (String w: sampleText.split("\\s+")) {
        if (null == w || w.trim().isEmpty()) {
          continue;
        }

        // remove non-alphanumeric char
        w = w.trim().replaceAll("[^A-Za-z0-9]", "");
        vecWords.add(w);
        set.add(w);
      }

      words = vecWords.toArray(new String[vecWords.size()]);

      // remove duplicated words
      noDupWords = set.toArray(new String[set.size()]);
    }

    Map<String, Integer> wordPool = new HashMap<>();
    for (String word : noDupWords) {
      if (!wordPool.containsKey(word)) {
        TopWord topWord = countOccurrence(word);

        if (topWord != null) {
          wordPool.put(word, topWord.getOccurrence());
        } else {
          log.error("word[{}] not found in sample file", word);
        }
      }
    }


    // sort word pool by value with limit
    wordPool.entrySet().stream()
      .sorted(
        Map.Entry.<String, Integer>comparingByValue().reversed())
      .limit(topCount)
      .forEachOrdered(
        x -> rlt.append(
          String.format("%s|%d\n",
            x.getKey(),
            x.getValue())
        ));

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
