package com.epam.rft.atsy.persistence.repositories;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.epam.rft.atsy.persistence.entities.ApplicationEntity;
import com.epam.rft.atsy.persistence.entities.CandidateEntity;
import com.epam.rft.atsy.persistence.entities.ChannelEntity;
import com.epam.rft.atsy.persistence.entities.PositionEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Sql("classpath:sql/application/application.sql")
public class ApplicationsRepositoryIT extends AbstractRepositoryIT {

  public static final long CANDIDATE_A_ID = 1L;
  public static final long CANDIDATE_B_ID = 2L;
  public static final long CANDIDATE_C_ID = 3L;

  public static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest(0, 10);
  public static final Pageable ZERO_TWO_PAGE_REQUEST = new PageRequest(0, 2);

  public static final String CHANNEL_NAME_DIRECT = "direkt";
  public static final String POSITION_NAME_DEVELOPER = "Fejlesztő";
  public static final String CHANNEL_NAME_PROFESSION_ADVERTISEMENT = "profession hírdetés";
  public static final String CHANNEL_NAME_PROFESSION_DATABASE = "profession adatbázis";
  public static final String CHANNEL_NAME_FACEBOOK = "facebook";
  public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_SAMPLE = "2016-07-26 11:48:55";

  @Autowired
  private ApplicationsRepository repository;

  @Autowired
  private CandidateRepository candidateRepository;

  @Test
  public void findByCandidateEntityShouldNotFindApplicationForCandidateWithoutApplications() {
    // Given
    CandidateEntity candidateB = this.candidateRepository.findOne(CANDIDATE_B_ID);

    // When
    List<ApplicationEntity> result = this.repository.findByCandidateEntity(candidateB);

    // Then
    assertThat(result, notNullValue());
    assertThat(result, empty());
  }

  @Test
  public void pageFindByCandidateEntityShouldNotFindApplicationForCandidateWithoutApplications() {
    // Given
    CandidateEntity candidateB = this.candidateRepository.findOne(CANDIDATE_B_ID);

    // When
    final Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntityAndDeletedFalse(candidateB, DEFAULT_PAGE_REQUEST);
    final List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(result, notNullValue());
    assertThat(result, empty());
  }

  @Test
  public void findByCandidateEntityShouldFindSingleApplicationForCandidateWithSingleApplication() {
    // Given
    CandidateEntity candidateEntityA = this.candidateRepository.findOne(CANDIDATE_A_ID);
    ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(1L)
        .name(CHANNEL_NAME_DIRECT)
        .deleted(false)
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name(POSITION_NAME_DEVELOPER)
        .deleted(false)
        .build();
    Date nearNow = currentDateMinus(5);

    // When
    List<ApplicationEntity> result = this.repository.findByCandidateEntity(candidateEntityA);

    // Then
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));

    assertApplicationEntity(result.get(0), candidateEntityA, expectedChannelEntity,
        expectedPositionEntity, nearNow);
  }

  @Test
  public void pageFindByCandidateEntityShouldFindSingleApplicationForCandidateWithSingleApplication() {
    // Given
    CandidateEntity candidateEntityA = this.candidateRepository.findOne(CANDIDATE_A_ID);
    ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(1L)
        .name(CHANNEL_NAME_DIRECT)
        .deleted(false)
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name(POSITION_NAME_DEVELOPER)
        .deleted(false)
        .build();
    Date nearNow = currentDateMinus(5);

    // When
    final Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntityAndDeletedFalse(candidateEntityA, DEFAULT_PAGE_REQUEST);
    final List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(pageResult.getTotalPages(), is(1));
    assertThat(pageResult.getTotalElements(), is(1L));
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));

    assertApplicationEntity(result.get(0), candidateEntityA, expectedChannelEntity,
        expectedPositionEntity, nearNow);
  }

  @Test
  public void findByCandidateEntityShouldFindThreeApplicationForCandidateWithThreeApplication()
      throws ParseException {
    // Given
    final CandidateEntity candidateEntityC = this.candidateRepository.findOne(CANDIDATE_C_ID);
    final ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(2L)
        .name(CHANNEL_NAME_PROFESSION_ADVERTISEMENT)
        .deleted(false)
        .build();
    final ChannelEntity expectedSecondChannelEntity = ChannelEntity.builder()
        .id(3L)
        .name(CHANNEL_NAME_PROFESSION_DATABASE)
        .deleted(false)
        .build();
    final ChannelEntity expectedThirdChannelEntity = ChannelEntity.builder()
        .id(4L)
        .name(CHANNEL_NAME_FACEBOOK)
        .deleted(false)
        .build();
    final PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name(POSITION_NAME_DEVELOPER)
        .deleted(false)
        .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    Date expectedDate = simpleDateFormat.parse(DATE_SAMPLE);
    // When
    List<ApplicationEntity> result = this.repository.findByCandidateEntity(candidateEntityC);

    // Then
    assertThat(result, notNullValue());
    assertThat(result.size(), is(3));

    assertApplicationEntity(result.get(0), candidateEntityC, expectedChannelEntity,
        expectedPositionEntity, expectedDate);
    assertApplicationEntity(result.get(1), candidateEntityC, expectedSecondChannelEntity,
        expectedPositionEntity, expectedDate);
    assertApplicationEntity(result.get(2), candidateEntityC, expectedThirdChannelEntity,
        expectedPositionEntity, expectedDate);
  }

  @Test
  public void pageFindByCandidateEntityShouldFindThreeApplicationForCandidateWithThreeApplication()
      throws ParseException {
    // Given
    final CandidateEntity candidateEntityC = this.candidateRepository.findOne(CANDIDATE_C_ID);
    final ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(2L)
        .name(CHANNEL_NAME_PROFESSION_ADVERTISEMENT)
        .deleted(false)
        .build();
    final ChannelEntity expectedSecondChannelEntity = ChannelEntity.builder()
        .id(3L)
        .name(CHANNEL_NAME_PROFESSION_DATABASE)
        .deleted(false)
        .build();
    final ChannelEntity expectedThirdChannelEntity = ChannelEntity.builder()
        .id(4L)
        .name(CHANNEL_NAME_FACEBOOK)
        .deleted(false)
        .build();
    final PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name(POSITION_NAME_DEVELOPER)
        .deleted(false)
        .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    final Date expectedDate = simpleDateFormat.parse(DATE_SAMPLE);

    // When
    final Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntityAndDeletedFalse(candidateEntityC, DEFAULT_PAGE_REQUEST);
    final List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(pageResult.getTotalPages(), is(1));
    assertThat(pageResult.getTotalElements(), is(3L));
    assertThat(result, notNullValue());
    assertThat(result.size(), is(3));

    assertApplicationEntity(result.get(0), candidateEntityC, expectedChannelEntity,
        expectedPositionEntity, expectedDate);
    assertApplicationEntity(result.get(1), candidateEntityC, expectedSecondChannelEntity,
        expectedPositionEntity, expectedDate);
    assertApplicationEntity(result.get(2), candidateEntityC, expectedThirdChannelEntity,
        expectedPositionEntity, expectedDate);
  }

  @Test
  public void findByCandidateEntityShouldFindAMaximumNumberOfApplicationsGivenInThePageRequest()
      throws ParseException {
    // Given
    CandidateEntity candidateEntityC = this.candidateRepository.findOne(CANDIDATE_C_ID);
    ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(2L)
        .name(CHANNEL_NAME_PROFESSION_ADVERTISEMENT)
        .deleted(false)
        .build();
    ChannelEntity expectedSecondChannelEntity = ChannelEntity.builder()
        .id(3L)
        .name(CHANNEL_NAME_PROFESSION_DATABASE)
        .deleted(false)
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name(POSITION_NAME_DEVELOPER)
        .deleted(false)
        .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    Date expectedDate = simpleDateFormat.parse(DATE_SAMPLE);

    // When
    Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntityAndDeletedFalse(candidateEntityC, ZERO_TWO_PAGE_REQUEST);
    List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(pageResult.getTotalPages(), is(2));
    assertThat(pageResult.getTotalElements(), is(3L));
    assertThat(result, notNullValue());
    assertThat(result.size(), is(2));

    assertApplicationEntity(result.get(0), candidateEntityC, expectedChannelEntity,
        expectedPositionEntity, expectedDate);
    assertApplicationEntity(result.get(1), candidateEntityC, expectedSecondChannelEntity,
        expectedPositionEntity, expectedDate);

  }

  private void assertApplicationEntity(ApplicationEntity application,
                                       CandidateEntity expectedCandidateEntity,
                                       ChannelEntity expectedChannelEntity,
                                       PositionEntity expectedPositionEntity, Date threshold) {
    assertThat(application, notNullValue());

    assertThat(application.getCandidateEntity(), notNullValue());
    assertThat(application.getCandidateEntity(), is(expectedCandidateEntity));

    assertThat(application.getChannelEntity(), notNullValue());
    assertThat(application.getChannelEntity(), is(expectedChannelEntity));

    assertThat(application.getPositionEntity(), notNullValue());
    assertThat(application.getPositionEntity(), is(expectedPositionEntity));

    assertThat(application.getCreationDate(), notNullValue());
    assertThat(application.getCreationDate(), greaterThan(threshold));
  }

  private Date currentDateMinus(long seconds) {
    return Date.from(ZonedDateTime.now().minusSeconds(seconds).toInstant());
  }

}
