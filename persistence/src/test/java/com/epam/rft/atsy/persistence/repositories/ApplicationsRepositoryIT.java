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
  public static final Pageable PAGE_REQUEST_ZERO_TWO = new PageRequest(0, 2);

  @Autowired
  private ApplicationsRepository repository;

  @Autowired
  private CandidateRepository candidateRepository;

  @Test
  public void findByCandidateEntityShouldNotFindApplicationForCandidateWithoutApplications() {
    // Given
    CandidateEntity candidateB = this.candidateRepository.findOne(CANDIDATE_B_ID);

    // When
    Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntity(candidateB, DEFAULT_PAGE_REQUEST);
    List<ApplicationEntity> result = pageResult.getContent();

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
        .name("direkt")
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name("Fejlesztő")
        .build();
    Date nearNow = currentDateMinus(5);

    // When
    Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntity(candidateEntityA, DEFAULT_PAGE_REQUEST);
    List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(result, notNullValue());
    assertThat(result.size(), is(1));

    assertApplicationEntity(result.get(0), candidateEntityA, expectedChannelEntity,
        expectedPositionEntity, nearNow);
  }

  @Test
  public void findByCandidateEntityShouldFindThreeApplicationForCandidateWithThreeApplication()
      throws ParseException {
    // Given
    CandidateEntity candidateEntityC = this.candidateRepository.findOne(CANDIDATE_C_ID);
    ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(2L)
        .name("profession hírdetés")
        .build();
    ChannelEntity expectedSecondChannelEntity = ChannelEntity.builder()
        .id(3L)
        .name("profession adatbázis")
        .build();
    ChannelEntity expectedThirdChannelEntity = ChannelEntity.builder()
        .id(4L)
        .name("facebook")
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name("Fejlesztő")
        .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expectedDate = simpleDateFormat.parse("2016-07-26 11:48:55");

    // When
    Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntity(candidateEntityC, DEFAULT_PAGE_REQUEST);
    List<ApplicationEntity> result = pageResult.getContent();

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
  public void findByCandidateEntityShouldFindAMaximumNumberOfApplicationsGivenInThePageRequest()
      throws ParseException {

    // Given
    CandidateEntity candidateEntityC = this.candidateRepository.findOne(CANDIDATE_C_ID);
    ChannelEntity expectedChannelEntity = ChannelEntity.builder()
        .id(2L)
        .name("profession hírdetés")
        .build();
    ChannelEntity expectedSecondChannelEntity = ChannelEntity.builder()
        .id(3L)
        .name("profession adatbázis")
        .build();
    PositionEntity expectedPositionEntity = PositionEntity.builder()
        .id(1L)
        .name("Fejlesztő")
        .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expectedDate = simpleDateFormat.parse("2016-07-26 11:48:55");

    // When
    Page<ApplicationEntity>
        pageResult =
        this.repository.findByCandidateEntity(candidateEntityC, PAGE_REQUEST_ZERO_TWO);
    List<ApplicationEntity> result = pageResult.getContent();

    // Then
    assertThat(result, notNullValue());
    assertThat(result.size(), is(2));
    assertThat(pageResult.getTotalPages(), is(2));

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
