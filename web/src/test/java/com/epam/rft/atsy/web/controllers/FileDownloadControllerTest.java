package com.epam.rft.atsy.web.controllers;

import com.epam.rft.atsy.service.CandidateService;
import com.epam.rft.atsy.service.domain.CandidateDTO;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class FileDownloadControllerTest extends AbstractControllerTest {

  private static final String UPLOAD_LOCATION_VARIABLE_NAME = "uploadLocation";
  private static final String CATALINA_BASE = "${catalina.base}";
  private static final String CV_TEST_FOLDER_NAME = "cv_test_folder";

  private static final String CV_TEST_FOLDER_LOCATION_PATH =
      System.getProperty(CATALINA_BASE) + File.separator + CV_TEST_FOLDER_NAME;
  private static final File FOLDER = new File(CV_TEST_FOLDER_LOCATION_PATH);


  private static final String CANDIDATE_ID_PARAM_NAME = "candidateId";
  private static final String CANDIDATE_ID_PARAM_VALUE = "1L";

  private static final long CANDIDATE_ID = 1L;
  private static final String CANDIDATE_NAME = "Candidate A";
  private static final String CANDIDATE_CV_FILENAME = "file.pdf";
  private static final long FILE_SIZE_HUNDRED_BYTE = 100;

  private CandidateDTO candidateDTOWithoutCVFile =
      CandidateDTO.builder().id(CANDIDATE_ID).name(CANDIDATE_NAME).cvFilename(null).build();
  private CandidateDTO candidateDTOWithCVFile =
      CandidateDTO.builder().id(CANDIDATE_ID).name(CANDIDATE_NAME).cvFilename(CANDIDATE_CV_FILENAME).build();

  @Mock
  private CandidateService candidateService;

  @Spy
  @InjectMocks
  private FileDownloadController fileDownloadController;

  @Override
  protected Object[] controllersUnderTest() {
    return new Object[]{fileDownloadController};
  }

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(fileDownloadController, UPLOAD_LOCATION_VARIABLE_NAME, CV_TEST_FOLDER_LOCATION_PATH);
  }

  @BeforeClass
  public static void doBeforeClass() throws IOException {
    FileUtils.forceMkdir(FOLDER);
  }

  @Before
  public void setup() {
    ReflectionTestUtils.setField(fileDownloadController, UPLOAD_LOCATION_VARIABLE_NAME, CV_TEST_FOLDER_LOCATION_PATH);
  }

  @After
  public void tearDown() throws IOException {
    FileUtils.cleanDirectory(FOLDER);
  }

  @AfterClass
  public static void doAfterClass() throws IOException {
    FileUtils.forceDelete(FOLDER);
  }

  @Test
  public void emptyTestMethod() {

  }

}
