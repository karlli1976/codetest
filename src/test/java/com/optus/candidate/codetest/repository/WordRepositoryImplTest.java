package com.optus.candidate.codetest.repository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WordRepository.class, WordRepositoryImpl.class})
@ContextConfiguration(classes = {WordRepository.class, WordRepositoryImpl.class})
class WordRepositoryImplTest {

  @Autowired
  WordRepository wordRepo;

  @Test
  void getSampleText_fileNotFound_throws() throws Exception {
    // test normal situation
    assertNotNull("sample not null", wordRepo.getSampleText());

    // test sample file not exist
    ReflectionTestUtils.setField((WordRepositoryImpl) wordRepo, "sampleFile", "none");
    ReflectionTestUtils.setField((WordRepositoryImpl) wordRepo, "sampleText", null);
    assertThrows(IOException.class, () -> {
      wordRepo.getSampleText();
    });
  }

}