package com.optus.candidate.codetest.model;

public class TopWord {
  private String word;
  private int occurrence;

  public TopWord(String word, int occurrence) {
    this.word = word;
    this.occurrence = occurrence;
  }

  public String getWord() {
    return word;
  }

  public int getOccurrence() {
    return occurrence;
  }

  public void incOccurrence() {
    this.occurrence++;
  }

  @Override
  public String toString() {
    return "TopWord{" +
      "word='" + word + '\'' +
      ", occurrence=" + occurrence +
      '}';
  }
}
