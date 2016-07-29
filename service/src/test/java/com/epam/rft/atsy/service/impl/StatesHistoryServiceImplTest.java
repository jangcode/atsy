package com.epam.rft.atsy.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.epam.rft.atsy.persistence.entities.ApplicationEntity;
import com.epam.rft.atsy.persistence.entities.CandidateEntity;
import com.epam.rft.atsy.persistence.entities.PositionEntity;
import com.epam.rft.atsy.persistence.entities.StatesEntity;
import com.epam.rft.atsy.persistence.entities.StatesHistoryEntity;
import com.epam.rft.atsy.persistence.repositories.ApplicationsRepository;
import com.epam.rft.atsy.persistence.repositories.CandidateRepository;
import com.epam.rft.atsy.persistence.repositories.StatesHistoryRepository;
import com.epam.rft.atsy.persistence.repositories.StatesRepository;
import com.epam.rft.atsy.service.domain.CandidateApplicationDTO;
import com.epam.rft.atsy.service.domain.states.StateDTO;
import com.epam.rft.atsy.service.domain.states.StateHistoryDTO;
import com.epam.rft.atsy.service.domain.states.StateHistoryViewDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StatesHistoryServiceImplTest {

  private static final Long FIRST_ID = 1L;
  private static final Long SECOND_ID = 2L;
  private static final Long THIRD_ID = 3L;
  private static final Long FIVE_SECONDS = 5L;
  private static final Type
      STATE_HISTORY_VIEW_DTO_LIST_TYPE =
      new TypeToken<List<StateHistoryViewDTO>>() {
      }.getType();

  private static final String FIRST_POSITION_ENTITY_NAME = "First position";
  private static final String SECOND_POSITION_ENTITY_NAME = "Second position";
  private static final String THIRD_POSITION_ENTITY_NAME = "Third position";

  private static final String FIRST_STATE_TYPE_NAME = "First state";
  private static final String SECOND_STATE_TYPE_NAME = "Second state";
  private static final String THIRD_STATE_TYPE_NAME = "Third state";

  private static final String DATE_FORMAT_CONSTANT = "yyyy-MM-dd HH:mm:ss";
  private static final SimpleDateFormat
      SIMPLE_DATE_FORMAT =
      new SimpleDateFormat(DATE_FORMAT_CONSTANT);

  private static final ApplicationEntity
      APPLICATION_ENTITY_WITHOUT_STATE_HISTORY =
      ApplicationEntity.builder().id(FIRST_ID).build();

  private static final CandidateEntity
      CANDIDATE_ENTITY_WITHOUT_APPLICATIONS =
      CandidateEntity.builder().id(FIRST_ID).build();

  private static final List<ApplicationEntity>
      EMPTY_APPLICATION_ENTITY_LIST =
      Collections.emptyList();

  private static final List<StatesHistoryEntity> EMPTY_STATE_HISTORY_ENTITY_LIST = Collections.emptyList();

  private static final List<StateHistoryViewDTO>
      EMPTY_STATE_HISTORY_VIEW_DTO_LIST =
      Collections.emptyList();

  private final Date presentDate = currentDateMinusSeconds(FIVE_SECONDS);

  private final CandidateEntity
      firstCandidateEntity =
      CandidateEntity.builder().id(FIRST_ID).build();

  private final PositionEntity
      firstPositionEntity =
      PositionEntity.builder().id(FIRST_ID).name(FIRST_POSITION_ENTITY_NAME).build();

  private final PositionEntity
      secondPositionEntity =
      PositionEntity.builder().id(SECOND_ID).name(SECOND_POSITION_ENTITY_NAME).build();

  private final PositionEntity
      thirdPositionEntity =
      PositionEntity.builder().id(THIRD_ID).name(THIRD_POSITION_ENTITY_NAME).build();

  private final ApplicationEntity
      firstApplicationEntity =
      ApplicationEntity.builder().id(FIRST_ID).candidateEntity(firstCandidateEntity)
          .positionEntity(firstPositionEntity).creationDate(presentDate).build();

  private final ApplicationEntity
      secondApplicationEntity =
      ApplicationEntity.builder().id(SECOND_ID).candidateEntity(firstCandidateEntity)
          .positionEntity(secondPositionEntity).creationDate(presentDate).build();

  private final ApplicationEntity
      thirdApplicationEntity =
      ApplicationEntity.builder().id(THIRD_ID).candidateEntity(firstCandidateEntity)
          .positionEntity(thirdPositionEntity).creationDate(presentDate).build();

  private final StatesEntity
      firstStateEntity =
      StatesEntity.builder().id(FIRST_ID).name(FIRST_STATE_TYPE_NAME).build();

  private final StatesEntity
      secondStateEntity =
      StatesEntity.builder().id(SECOND_ID).name(SECOND_STATE_TYPE_NAME).build();

  private final StatesEntity
      thirdStateEntity =
      StatesEntity.builder().id(THIRD_ID).name(THIRD_STATE_TYPE_NAME).build();

  private final StatesHistoryEntity
      firstStatesHistoryEntity =
      StatesHistoryEntity.builder().id(FIRST_ID).applicationEntity(firstApplicationEntity)
          .statesEntity(firstStateEntity).creationDate(presentDate).build();

  private final StatesHistoryEntity
      secondStatesHistoryEntity =
      StatesHistoryEntity.builder().id(SECOND_ID).applicationEntity(secondApplicationEntity)
          .statesEntity(secondStateEntity).creationDate(presentDate).build();

  private final StatesHistoryEntity
      thirdStatesHistoryEntity =
      StatesHistoryEntity.builder().id(THIRD_ID).applicationEntity(thirdApplicationEntity)
          .statesEntity(thirdStateEntity).creationDate(presentDate).build();

  private final StatesHistoryEntity
      savedStatesHistoryEntity =
      StatesHistoryEntity.builder().id(FIRST_ID).applicationEntity(secondApplicationEntity)
          .statesEntity(firstStateEntity).creationDate(presentDate).build();

  private final StateHistoryViewDTO
      stateViewHistoryDTO =
      StateHistoryViewDTO.builder().id(FIRST_ID).build();

  private final StateDTO
      firstStateDTO =
      StateDTO.builder().id(FIRST_ID).name(FIRST_STATE_TYPE_NAME).build();

  private final StateHistoryDTO
      stateHistoryDTO =
      StateHistoryDTO.builder().id(FIRST_ID).stateDTO(firstStateDTO).build();

  private final List<ApplicationEntity>
      applicationEntityListWithSingleElement =
      Collections.singletonList(firstApplicationEntity);

  private final List<ApplicationEntity>
      applicationEntityListWithThreeElements =
      Arrays.asList(firstApplicationEntity, secondApplicationEntity, thirdApplicationEntity);

  private final List<StatesHistoryEntity>
      statesHistoryEntityListWithSingleElement =
      Collections.singletonList(firstStatesHistoryEntity);

  private final List<StatesHistoryEntity>
      statesHistoryEntityListWithThreeElements =
      Arrays.asList(firstStatesHistoryEntity, secondStatesHistoryEntity, thirdStatesHistoryEntity);

  private final List<StateHistoryViewDTO>
      stateViewHistoryDTOListWithSingleElement =
      Collections.singletonList(stateViewHistoryDTO);

  private final List<StateHistoryViewDTO>
      stateViewHistoryDTOListWithThreeElements =
      Arrays.asList(stateViewHistoryDTO, stateViewHistoryDTO, stateViewHistoryDTO);

  private List<CandidateApplicationDTO>
      candidateApplicationDTOListWithSingleElement =
      Collections.singletonList(
          CandidateApplicationDTO.builder().applicationId(FIRST_ID).stateType(FIRST_STATE_TYPE_NAME)
              .positionName(FIRST_POSITION_ENTITY_NAME).lastStateId(FIRST_ID)
              .creationDate(SIMPLE_DATE_FORMAT.format(presentDate))
              .modificationDate(SIMPLE_DATE_FORMAT.format(presentDate)).build()
      );

  private List<CandidateApplicationDTO>
      candidateApplicationDTOListWithThreeElements =
      Arrays.asList(
          CandidateApplicationDTO.builder().applicationId(FIRST_ID).stateType(FIRST_STATE_TYPE_NAME)
              .positionName(FIRST_POSITION_ENTITY_NAME).lastStateId(FIRST_ID)
              .creationDate(SIMPLE_DATE_FORMAT.format(presentDate))
              .modificationDate(SIMPLE_DATE_FORMAT.format(presentDate)).build(),

          CandidateApplicationDTO.builder().applicationId(SECOND_ID)
              .stateType(SECOND_STATE_TYPE_NAME).positionName(SECOND_POSITION_ENTITY_NAME)
              .lastStateId(SECOND_ID)
              .creationDate(SIMPLE_DATE_FORMAT.format(presentDate))
              .modificationDate(SIMPLE_DATE_FORMAT.format(presentDate)).build(),

          CandidateApplicationDTO.builder().applicationId(THIRD_ID).stateType(THIRD_STATE_TYPE_NAME)
              .positionName(THIRD_POSITION_ENTITY_NAME).lastStateId(THIRD_ID)
              .creationDate(SIMPLE_DATE_FORMAT.format(presentDate))
              .modificationDate(SIMPLE_DATE_FORMAT.format(presentDate)).build()
      );

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private StatesHistoryRepository statesHistoryRepository;

  @Mock
  private StatesRepository statesRepository;

  @Mock
  private ApplicationsRepository applicationsRepository;

  @Mock
  private CandidateRepository candidateRepository;

  @InjectMocks
  private StatesHistoryServiceImpl statesHistoryService;


  @Test(expected = IllegalArgumentException.class)
  public void getStatesByApplicationIdShouldThrowIllegalArgumentExceptionWhenApplicationIdIsNull() {
    // Given

    // When
    statesHistoryService.getStateHistoriesByApplicationId(null);

    // Then
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStatesByApplicationIdShouldThrowIllegalArgumentExceptionWhenApplicationIsNull() {
    // Given
    given(applicationsRepository.findOne(FIRST_ID)).willReturn(null);

    // When
    statesHistoryService.getStateHistoriesByApplicationId(FIRST_ID);

    // Then
  }

  @Test
  public void getStatesByApplicationIdShouldReturnAnEmptyList() {
    // Given
    given(applicationsRepository.findOne(FIRST_ID)).willReturn(
        APPLICATION_ENTITY_WITHOUT_STATE_HISTORY);
    given(statesHistoryRepository
        .findByApplicationEntityOrderByCreationDateDesc(APPLICATION_ENTITY_WITHOUT_STATE_HISTORY))
        .willReturn(EMPTY_STATE_HISTORY_ENTITY_LIST);
    given(modelMapper.map(EMPTY_STATE_HISTORY_ENTITY_LIST, STATE_HISTORY_VIEW_DTO_LIST_TYPE))
        .willReturn(EMPTY_STATE_HISTORY_VIEW_DTO_LIST);

    // When
    List<StateHistoryViewDTO>
        stateViewHistoryDTOList =
        statesHistoryService.getStateHistoriesByApplicationId(FIRST_ID);

    // Then
    assertThat(stateViewHistoryDTOList, notNullValue());
    assertThat(stateViewHistoryDTOList.isEmpty(), is(true));

    then(applicationsRepository).should().findOne(FIRST_ID);
    then(statesHistoryRepository).should()
        .findByApplicationEntityOrderByCreationDateDesc(APPLICATION_ENTITY_WITHOUT_STATE_HISTORY);
  }

  @Test
  public void getStatesByApplicationIdShouldReturnAListWithSingleElement() {
    // Given
    given(applicationsRepository.findOne(FIRST_ID)).willReturn(firstApplicationEntity);
    given(statesHistoryRepository
        .findByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity))
        .willReturn(statesHistoryEntityListWithSingleElement);
    given(modelMapper.map(statesHistoryEntityListWithSingleElement,
        STATE_HISTORY_VIEW_DTO_LIST_TYPE))
        .willReturn(stateViewHistoryDTOListWithSingleElement);

    // When
    List<StateHistoryViewDTO>
        stateViewHistoryDTOList =
        statesHistoryService.getStateHistoriesByApplicationId(FIRST_ID);

    // Then
    assertThat(stateViewHistoryDTOList, notNullValue());
    assertThat(stateViewHistoryDTOList.isEmpty(), is(false));
    assertThat(stateViewHistoryDTOList, equalTo(stateViewHistoryDTOListWithSingleElement));
    assertStateViewDtoListByCreationDateAsString(stateViewHistoryDTOList);

    then(applicationsRepository).should().findOne(FIRST_ID);
    then(statesHistoryRepository).should()
        .findByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity);
  }

  @Test
  public void getStatesByApplicationIdShouldReturnAListWithThreeElement() {
    // Given
    given(applicationsRepository.findOne(FIRST_ID)).willReturn(firstApplicationEntity);
    given(statesHistoryRepository
        .findByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity))
        .willReturn(statesHistoryEntityListWithThreeElements);
    given(modelMapper.map(statesHistoryEntityListWithThreeElements,
        STATE_HISTORY_VIEW_DTO_LIST_TYPE))
        .willReturn(stateViewHistoryDTOListWithThreeElements);

    // When
    List<StateHistoryViewDTO>
        stateViewHistoryDTOList =
        statesHistoryService.getStateHistoriesByApplicationId(FIRST_ID);

    // Then
    assertThat(stateViewHistoryDTOList, notNullValue());
    assertThat(stateViewHistoryDTOList.isEmpty(), is(false));
    assertThat(stateViewHistoryDTOList, equalTo(stateViewHistoryDTOListWithThreeElements));
    assertStateViewDtoListByCreationDateAsString(stateViewHistoryDTOList);

    then(applicationsRepository).should().findOne(FIRST_ID);
    then(statesHistoryRepository).should()
        .findByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveStateShouldThrowIllegalArgumentExceptionWhenApplicationIdIsNull() {
    // Given

    // When
    statesHistoryService.saveStateHistory(stateHistoryDTO, null);

    // Then
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveStateShouldThrowIllegalArgumentExceptionWhenStateIsNull() {
    // Given

    // When
    statesHistoryService.saveStateHistory(null, FIRST_ID);

    // Then
  }

  @Test
  public void saveStateShouldBeSuccessSaving() {
    // Given
    given(modelMapper.map(stateHistoryDTO, StatesHistoryEntity.class))
        .willReturn(firstStatesHistoryEntity);
    given(applicationsRepository.findOne(SECOND_ID)).willReturn(secondApplicationEntity);
    given(statesHistoryRepository.save(firstStatesHistoryEntity))
        .willReturn(firstStatesHistoryEntity);
    given(statesRepository.findOne(stateHistoryDTO.getStateDTO().getId())).willReturn(
        firstStateEntity);

    // When
    Long resultId = statesHistoryService.saveStateHistory(stateHistoryDTO, SECOND_ID);

    // Then
    assertStateEntityWhenSavingStateEntity(firstStatesHistoryEntity, savedStatesHistoryEntity);
    assertThat(resultId, notNullValue());
    assertThat(resultId, equalTo(FIRST_ID));

    then(applicationsRepository).should().findOne(SECOND_ID);
    then(statesHistoryRepository).should().save(firstStatesHistoryEntity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCandidateApplicationsByCandidateIdShouldThrowIllegalArgumentExceptionWhenCandidateIdIsNull() {
    // Given

    // When
    statesHistoryService.getCandidateApplicationsByCandidateId(null);

    // Then
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCandidateApplicationsByCandidateIdShouldThrowIllegalArgumentExceptionWhenCandidateEntityIsNull() {
    // Given
    given(candidateRepository.findOne(FIRST_ID)).willReturn(null);

    // When
    statesHistoryService.getCandidateApplicationsByCandidateId(FIRST_ID);

    // Then
  }

  @Test
  public void getCandidateApplicationsByCandidateIdShouldReturnAnEmptyList() {
    // Given
    given(candidateRepository.findOne(FIRST_ID)).willReturn(CANDIDATE_ENTITY_WITHOUT_APPLICATIONS);
    given(applicationsRepository.findByCandidateEntity(CANDIDATE_ENTITY_WITHOUT_APPLICATIONS))
        .willReturn(EMPTY_APPLICATION_ENTITY_LIST);

    // When
    Collection<CandidateApplicationDTO>
        candidateApplicationDTOCollection =
        statesHistoryService.getCandidateApplicationsByCandidateId(FIRST_ID);

    // Then
    assertThat(candidateApplicationDTOCollection, notNullValue());
    assertThat(candidateApplicationDTOCollection.isEmpty(), is(true));

    then(candidateRepository).should().findOne(FIRST_ID);
    then(applicationsRepository).should()
        .findByCandidateEntity(CANDIDATE_ENTITY_WITHOUT_APPLICATIONS);
  }

  @Test
  public void getCandidateApplicationsByCandidateIdShouldReturnAListWithSingleElement() {
    // Given
    given(candidateRepository.findOne(FIRST_ID)).willReturn(firstCandidateEntity);
    given(applicationsRepository.findByCandidateEntity(firstCandidateEntity))
        .willReturn(applicationEntityListWithSingleElement);
    given(statesHistoryRepository
        .findTopByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity))
        .willReturn(firstStatesHistoryEntity);

    // When
    Collection<CandidateApplicationDTO>
        candidateApplicationDTOCollection =
        statesHistoryService.getCandidateApplicationsByCandidateId(FIRST_ID);

    // Then
    assertCandidateApplicationDTOCollection(candidateApplicationDTOCollection,
        candidateApplicationDTOListWithSingleElement);

    then(candidateRepository).should().findOne(FIRST_ID);
    then(applicationsRepository).should().findByCandidateEntity(firstCandidateEntity);
    then(statesHistoryRepository).should()
        .findTopByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity);
  }

  @Test
  public void getCandidateApplicationsByCandidateShouldReturnAListWithThreeElements() {
    // Given
    given(candidateRepository.findOne(FIRST_ID)).willReturn(firstCandidateEntity);
    given(applicationsRepository.findByCandidateEntity(firstCandidateEntity))
        .willReturn(applicationEntityListWithThreeElements);
    given(statesHistoryRepository
        .findTopByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity))
        .willReturn(firstStatesHistoryEntity);
    given(statesHistoryRepository
        .findTopByApplicationEntityOrderByCreationDateDesc(secondApplicationEntity))
        .willReturn(secondStatesHistoryEntity);
    given(statesHistoryRepository
        .findTopByApplicationEntityOrderByCreationDateDesc(thirdApplicationEntity))
        .willReturn(thirdStatesHistoryEntity);

    // When
    Collection<CandidateApplicationDTO>
        candidateApplicationDTOCollection =
        statesHistoryService.getCandidateApplicationsByCandidateId(FIRST_ID);

    // Then
    assertCandidateApplicationDTOCollection(candidateApplicationDTOCollection,
        candidateApplicationDTOListWithThreeElements);

    then(candidateRepository).should().findOne(FIRST_ID);
    then(applicationsRepository).should().findByCandidateEntity(firstCandidateEntity);
    then(statesHistoryRepository).should()
        .findTopByApplicationEntityOrderByCreationDateDesc(firstApplicationEntity);
    then(statesHistoryRepository).should()
        .findTopByApplicationEntityOrderByCreationDateDesc(secondApplicationEntity);
    then(statesHistoryRepository).should()
        .findTopByApplicationEntityOrderByCreationDateDesc(thirdApplicationEntity);
  }

  private void assertStateViewDtoListByCreationDateAsString(
      List<StateHistoryViewDTO> stateViewHistoryDTOList) {
    for (StateHistoryViewDTO stateViewHistoryDTO : stateViewHistoryDTOList) {
      assertThat(stateViewHistoryDTO.getCreationDate(),
          equalTo(SIMPLE_DATE_FORMAT.format(presentDate)));
    }
  }

  private void assertCandidateApplicationDTOCollection(
      Collection<CandidateApplicationDTO> candidateApplicationDTOCollection,
      Collection<CandidateApplicationDTO> expectedCollection) {
    assertThat(candidateApplicationDTOCollection, notNullValue());
    assertThat(candidateApplicationDTOCollection.isEmpty(), is(false));
    assertThat(candidateApplicationDTOCollection, equalTo(expectedCollection));
  }

  private void assertStateEntityWhenSavingStateEntity(StatesHistoryEntity statesHistoryEntity,
                                                      StatesHistoryEntity expectedSavedStatesHistoryEntity) {
    assertThat(statesHistoryEntity, notNullValue());
    assertThat(statesHistoryEntity.getApplicationEntity(),
        equalTo(expectedSavedStatesHistoryEntity.getApplicationEntity()));
    assertThat(statesHistoryEntity.getCreationDate(),
        greaterThanOrEqualTo(expectedSavedStatesHistoryEntity.getCreationDate()));
  }

  private Date currentDateMinusSeconds(Long seconds) {
    return Date.from(ZonedDateTime.now().minusSeconds(seconds).toInstant());
  }
}