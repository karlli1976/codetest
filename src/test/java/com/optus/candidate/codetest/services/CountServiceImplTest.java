package com.optus.candidate.codetest.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration
class CountServiceImplTest {

  @Autowired
  CountService countService;


  @Test
  void getCount_withWord_isOK() {
    String word = "Duis";
    assertEquals(11, countService.getCount(word));

    word = "Sed";
    assertEquals(16, countService.getCount(word));
  }

  @Test
  void getCount_withNumber_isOK() {
    String word = "123";
    assertEquals(2, countService.getCount(word));
  }

  @Test
  void getCount_withNull_isOK() {
    String word = null;
    assertEquals(0, countService.getCount(word));
  }

  @Test
  void getCount_withEmpty_isOK() {
    String word = "";
    assertEquals(0, countService.getCount(word));
  }


}