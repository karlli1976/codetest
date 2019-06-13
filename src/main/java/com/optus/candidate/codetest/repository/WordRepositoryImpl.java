package com.optus.candidate.codetest.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Repository
@PropertySource("classpath:application.properties")
public class WordRepositoryImpl implements WordRepository {

  private Logger log = LoggerFactory.getLogger(WordRepositoryImpl.class);

  @Value("${sample.file}")
  private String sampleFile;

  private String sampleText;

  /**
   * Return sample text thru classpath resource
   *
   * @return null if sample text file not found
   */
  @Override
  public String getSampleText() {
    String rlt = null;

    try {
      if (null == sampleText) {
        File resource = new ClassPathResource(sampleFile).getFile();
        sampleText = new String(Files.readAllBytes(resource.toPath()));
      }

      rlt = sampleText;
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    finally {
      return rlt;
    }

  }


}
