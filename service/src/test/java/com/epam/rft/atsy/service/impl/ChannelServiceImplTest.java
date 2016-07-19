package com.epam.rft.atsy.service.impl;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.epam.rft.atsy.persistence.entities.ChannelEntity;
import com.epam.rft.atsy.persistence.repositories.ChannelRepository;
import com.epam.rft.atsy.service.domain.ChannelDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceImplTest {
  private static final Long CHANNEL_ID_FACEBOOK = 1L;
  private static final String CHANNEL_NAME_FACEBOOK = "facebook";
  private static final Long CHANNEL_ID_DTK = 2L;
  private static final String CHANNEL_NAME_DTK = "dtk";
  private static final Long CHANNEL_ID_LINKEDIN = 3L;
  private static final String CHANNEL_NAME_LINKEDIN = "linkedin hírdetés";
  private static final List<ChannelEntity> EMPTY_CHANNEL_ENTITY_LIST = Collections.emptyList();
  private static final Collection<ChannelDTO> EMPTY_CHANNEL_DTO_LIST = Collections.emptyList();
  private static final Type CHANNEL_DTO_LIST_TYPE = new TypeToken<List<ChannelDTO>>() {
  }.getType();
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @InjectMocks
  private ChannelServiceImpl channelService;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  private ChannelRepository channelRepository;
  private ChannelDTO facebookDto;
  private ChannelEntity facebookEntity;
  private ChannelDTO dtkDto;
  private ChannelEntity dtkEntity;
  private ChannelDTO linkedinDto;
  private ChannelEntity linkedinEntity;
  private List<ChannelEntity> expectedChannelEntityList;
  private Collection<ChannelDTO> expectedChannelDTOList;

  @Before
  public void init() {
    facebookDto = ChannelDTO.builder().id(CHANNEL_ID_FACEBOOK).name(CHANNEL_NAME_FACEBOOK).build();
    facebookEntity =
        ChannelEntity.builder().id(CHANNEL_ID_FACEBOOK).name(CHANNEL_NAME_FACEBOOK).build();
    dtkDto = ChannelDTO.builder().id(CHANNEL_ID_DTK).name(CHANNEL_NAME_DTK).build();
    dtkEntity = ChannelEntity.builder().id(CHANNEL_ID_DTK).name(CHANNEL_NAME_DTK).build();
    linkedinDto = ChannelDTO.builder().id(CHANNEL_ID_LINKEDIN).name(CHANNEL_NAME_LINKEDIN).build();
    linkedinEntity =
        ChannelEntity.builder().id(CHANNEL_ID_LINKEDIN).name(CHANNEL_NAME_LINKEDIN).build();
    expectedChannelDTOList = Arrays.asList(facebookDto, dtkDto, linkedinDto);
    expectedChannelEntityList = Arrays.asList(facebookEntity, dtkEntity, linkedinEntity);
  }

  @Test
  public void getAllChannelsShouldReturnEmptyListWhenThereAreNoChannels() {
    // Given
    given(channelRepository.findAll()).willReturn(EMPTY_CHANNEL_ENTITY_LIST);
    given(modelMapper.map(EMPTY_CHANNEL_ENTITY_LIST, CHANNEL_DTO_LIST_TYPE))
        .willReturn(EMPTY_CHANNEL_DTO_LIST);

    // When
    Collection<ChannelDTO> channels = channelService.getAllChannels();

    // Then
    assertThat(channels, notNullValue());
    assertTrue(channels.isEmpty());

    then(channelRepository).should().findAll();
    then(modelMapper).should().map(EMPTY_CHANNEL_ENTITY_LIST, CHANNEL_DTO_LIST_TYPE);
  }


  @Test
  public void getAllChannelsShouldReturnSingleElementListWhenThereIsOneChannel() {
    // Given
    List<ChannelEntity> channels = Arrays.asList(facebookEntity);

    Collection<ChannelDTO> expected = Arrays.asList(facebookDto);

    given(channelRepository.findAll()).willReturn(channels);
    given(modelMapper.map(channels, CHANNEL_DTO_LIST_TYPE)).willReturn(expected);

    // When
    Collection<ChannelDTO> result = channelService.getAllChannels();

    // Then
    assertEquals(result, expected);

    then(channelRepository).should().findAll();
    then(modelMapper).should().map(channels, CHANNEL_DTO_LIST_TYPE);
  }

  @Test
  public void getAllChannelsShouldReturnThreeElementListWhenThereAreThreeChannels() {
    // Given
    given(channelRepository.findAll()).willReturn(expectedChannelEntityList);
    given(modelMapper.map(expectedChannelEntityList, CHANNEL_DTO_LIST_TYPE))
        .willReturn(expectedChannelDTOList);

    // When
    Collection<ChannelDTO> result = channelService.getAllChannels();

    // Then
    assertEquals(result, expectedChannelDTOList);

    then(channelRepository).should().findAll();
    then(modelMapper).should().map(expectedChannelEntityList, CHANNEL_DTO_LIST_TYPE);
  }

  @Test
  public void saveOrUpdateShouldThrowDREAfterCatchingConstraintViolationException()
      throws DuplicateRecordException {
    // Given
    given(modelMapper.map(facebookDto, ChannelEntity.class)).willReturn(facebookEntity);

    given(channelRepository.saveAndFlush(facebookEntity))
        .willThrow(ConstraintViolationException.class);

    expectedException.expect(DuplicateRecordException.class);
    expectedException.expectMessage(CoreMatchers.endsWith(CHANNEL_NAME_FACEBOOK));
    expectedException.expectCause(Matchers.isA(ConstraintViolationException.class));

    // When
    channelService.saveOrUpdate(facebookDto);
  }

  @Test
  public void saveOrUpdateShouldThrowDREAfterCatchingDataIntegrityViolationException()
      throws DuplicateRecordException {
    // Given
    given(modelMapper.map(facebookDto, ChannelEntity.class)).willReturn(facebookEntity);

    given(channelRepository.saveAndFlush(facebookEntity))
        .willThrow(DataIntegrityViolationException.class);

    expectedException.expect(DuplicateRecordException.class);
    expectedException.expectMessage(CoreMatchers.endsWith(CHANNEL_NAME_FACEBOOK));
    expectedException.expectCause(Matchers.isA(DataIntegrityViolationException.class));

    // When
    channelService.saveOrUpdate(facebookDto);
  }

  @Test
  public void saveOrUpdateShouldSaveValidChannelDTO() {
    // Given
    given(modelMapper.map(facebookDto, ChannelEntity.class)).willReturn(facebookEntity);

    given(channelRepository.saveAndFlush(facebookEntity)).willReturn(facebookEntity);

    // When
    channelService.saveOrUpdate(facebookDto);

    // Then
    then(channelRepository).should().saveAndFlush(facebookEntity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveOrUpdateShouldThrowIllegalArgumentExceptionWhenNullPassed() {
    // When
    channelService.saveOrUpdate(null);
  }


}
