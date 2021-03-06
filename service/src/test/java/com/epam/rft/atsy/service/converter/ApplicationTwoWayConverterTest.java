package com.epam.rft.atsy.service.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import com.epam.rft.atsy.persistence.entities.ApplicationEntity;
import com.epam.rft.atsy.persistence.entities.CandidateEntity;
import com.epam.rft.atsy.persistence.entities.ChannelEntity;
import com.epam.rft.atsy.persistence.entities.PositionEntity;
import com.epam.rft.atsy.persistence.repositories.CandidateRepository;
import com.epam.rft.atsy.persistence.repositories.ChannelRepository;
import com.epam.rft.atsy.persistence.repositories.PositionRepository;
import com.epam.rft.atsy.service.converter.impl.ApplicationTwoWayConverter;
import com.epam.rft.atsy.service.domain.ApplicationDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTwoWayConverterTest {

  private static final Long APPLICATION_ID = 1L;
  private static final Date APPLICATION_CREATION_DATE = new Date();

  private static final Long CANDIDATE_ID = 1L;
  private static final String CANDIDATE_DESCRIPTION = "Candidate description";
  private static final String CANDIDATE_EMAIL = "candidate@atsy.aa";
  private static final Short CANDIDATE_LANGUAGE_SKILL = 6;
  private static final String CANDIDATE_NAME = "Candidate D";
  private static final String CANDIDATE_PHONE_NUMBER = "+55555555555";
  private static final String CANDIDATE_REFERRER = "google";

  private static final Long POSITION_ID = 1L;
  private static final String POSITION_NAME = "Developer";

  private static final long CHANNEL_ID = 1L;
  private static final String CHANNEL_NAME = "google";

  private CandidateEntity candidateEntity = CandidateEntity.builder()
      .id(CANDIDATE_ID)
      .description(CANDIDATE_DESCRIPTION)
      .email(CANDIDATE_EMAIL)
      .languageSkill(CANDIDATE_LANGUAGE_SKILL)
      .name(CANDIDATE_NAME)
      .phone(CANDIDATE_PHONE_NUMBER)
      .referer(CANDIDATE_REFERRER)
      .build();

  private PositionEntity positionEntity = PositionEntity.builder()
      .id(POSITION_ID)
      .name(POSITION_NAME)
      .build();

  private ChannelEntity channelEntity = ChannelEntity.builder()
      .id(CHANNEL_ID)
      .name(CHANNEL_NAME)
      .build();

  private ApplicationEntity applicationEntity = ApplicationEntity.builder()
      .id(APPLICATION_ID)
      .creationDate(APPLICATION_CREATION_DATE)
      .candidateEntity(candidateEntity)
      .positionEntity(positionEntity)
      .channelEntity(channelEntity)
      .deleted(false)
      .build();

  private ApplicationDTO applicationDTO = ApplicationDTO.builder()
      .id(APPLICATION_ID)
      .creationDate(APPLICATION_CREATION_DATE)
      .candidateId(CANDIDATE_ID)
      .positionId(POSITION_ID)
      .channelId(CHANNEL_ID)
      .deleted(false)
      .build();

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private ChannelRepository channelRepository;

  private ApplicationTwoWayConverter applicationTwoWayConverter;

  @Before
  public void setUp() {
    applicationTwoWayConverter =
        new ApplicationTwoWayConverter(candidateRepository, positionRepository, channelRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void firstTypeToSecondTypeShouldThrowIllegalArgumentExceptionWhenSourceIsNull() {
    //Given

    //When
    applicationTwoWayConverter.firstTypeToSecondType(null);

    //Then
  }

  @Test
  public void firstTypeToSecondTypeShouldReturnCorrectApplicationDTO() {
    //Given

    //When
    ApplicationDTO result = applicationTwoWayConverter.firstTypeToSecondType(applicationEntity);

    //Then
    assertThat(result, notNullValue());
    assertThat(result, is(applicationDTO));
  }

  @Test(expected = IllegalArgumentException.class)
  public void secondTypeToFirstTypeShouldThrowIllegalArgumentExceptionWhenSourceIsNull() {
    //Given

    //When
    applicationTwoWayConverter.firstTypeToSecondType(null);

    //Then
  }

  @Test
  public void secondTypeToFirstTypeShouldReturnCorrectApplicationEntity() {
    //Given
    given(candidateRepository.findOne(CANDIDATE_ID)).willReturn(candidateEntity);
    given(positionRepository.findOne(POSITION_ID)).willReturn(positionEntity);
    given(channelRepository.findOne(CHANNEL_ID)).willReturn(channelEntity);

    //When
    ApplicationEntity result = applicationTwoWayConverter.secondTypeToFirstType(applicationDTO);

    //Then
    assertThat(result, notNullValue());
    assertThat(result, is(applicationEntity));
  }

}
