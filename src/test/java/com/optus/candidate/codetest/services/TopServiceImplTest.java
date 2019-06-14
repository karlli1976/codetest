package com.optus.candidate.codetest.services;

import com.optus.candidate.codetest.repository.WordRepository;
import com.optus.candidate.codetest.repository.WordRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TopServiceImplTest {

  @Autowired
  TopService topService;

  @Autowired
  WordRepository wordRepo;

  @Test
  void getTop_withValidParam_isOK() {
    int top = 10;
    String expectedStr =
      "VEL|17\n" +
      "EGET|17\n" +
      "SED|16\n" +
      "IN|15\n" +
      "ET|14\n" +
      "UT|13\n" +
      "EU|13\n" +
      "METUS|12\n" +
      "ID|12\n" +
      "AC|12\n";
    
    assertEquals(expectedStr, topService.getTop(top));
  }


  @Test
  void getTop_withNegativeParam_isFail() {
    int top = -5;
    assertNull(topService.getTop(top));
  }

  @Test
  void getTop_withZeroParam_isFail() {
    int top = 0;
    assertNull(topService.getTop(top));
  }

  @Test
  void getTop_withUpperLimitParam_isOK() {
    int top = Integer.MAX_VALUE;
    assertNotNull(topService.getTop(top));
  }

  @Test
  void getTop_withSampleTextNotFound_isFail() {
    int top = 5;

    ReflectionTestUtils.setField((WordRepositoryImpl) wordRepo, "sampleFile", "none");

    assertNull(topService.getTop(top));
  }

  @Test
  void getTop_withBlankSampleText_isOK() {
    int top = 5;

    ReflectionTestUtils.setField((WordRepositoryImpl) wordRepo, "sampleText", "   ");
    assertEquals("", topService.getTop(top));
  }
}