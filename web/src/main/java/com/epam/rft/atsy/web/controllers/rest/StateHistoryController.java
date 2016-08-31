package com.epam.rft.atsy.web.controllers.rest;

import com.epam.rft.atsy.service.ApplicationsService;
import com.epam.rft.atsy.service.ChannelService;
import com.epam.rft.atsy.service.PositionService;
import com.epam.rft.atsy.service.StatesHistoryService;
import com.epam.rft.atsy.service.domain.states.StateDTO;
import com.epam.rft.atsy.service.domain.states.StateHistoryDTO;
import com.epam.rft.atsy.web.FieldErrorResponseComposer;
import com.epam.rft.atsy.web.StateHistoryViewRepresentation;
import com.epam.rft.atsy.web.exceptionhandling.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/secure/application_state")
public class StateHistoryController {

  private static final String DATE_FORMAT_CONSTANT = "yyyy-MM-dd HH:mm";
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_CONSTANT);
  private static final String POSITION_NOT_FOUND_MESSAGE_KEY = "position.not.found.error.message";
  private static final String CHANNEL_NOT_FOUND_MESSAGE_KEY = "channel.not.found.error.message";
  private static final String FIELD_POSITION_NAME = "positionName";
  private static final String FIELD_CHANNEL_NAME = "channelName";

  @Autowired
  private StatesHistoryService statesHistoryService;

  @Autowired
  private ApplicationsService applicationsService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private PositionService positionService;

  @Autowired
  private FieldErrorResponseComposer fieldErrorResponseComposer;

  /**
   * This method is used to save new states or update the information of the latest state.
   *
   * @param applicationId                  identifier of the application whose states are viewed and edited
   * @param stateHistoryViewRepresentation this attribute contains all the state information of the
   *                                       given application
   * @return a ModelAndView object filled with the data to stay on the same page and view the states
   * of the same application, but including the latest modification
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity saveOrUpdate(@RequestParam Long applicationId,
                                     @Valid @RequestBody StateHistoryViewRepresentation stateHistoryViewRepresentation,
                                     BindingResult bindingResult, Locale locale) {

    StateHistoryDTO stateHistoryDTO;
    Long stateId = stateHistoryViewRepresentation.getStateId();

    if (stateId != null && stateId == 1) {
      validateNewState(stateHistoryViewRepresentation, bindingResult);
    }

    if (bindingResult.hasErrors()) {
      return fieldErrorResponseComposer.composeResponse(bindingResult);
    } else {
      try {
        stateHistoryDTO = StateHistoryDTO.builder()
            .id(stateHistoryViewRepresentation.getId())
            .candidateId(stateHistoryViewRepresentation.getCandidateId())
            .languageSkill(stateHistoryViewRepresentation.getLanguageSkill())
            .description(stateHistoryViewRepresentation.getDescription())
            .result(stateHistoryViewRepresentation.getResult())
            .offeredMoney(stateHistoryViewRepresentation.getOfferedMoney())
            .claim(stateHistoryViewRepresentation.getClaim())
            .feedbackDate(stateHistoryViewRepresentation.getFeedbackDate() != null
                && !stateHistoryViewRepresentation.getFeedbackDate().isEmpty() ? DATE_FORMAT
                .parse(stateHistoryViewRepresentation.getFeedbackDate()) : null)
            .dayOfStart(stateHistoryViewRepresentation.getDayOfStart())
            .creationDate(null)
            .stateDTO(StateDTO.builder().id(stateHistoryViewRepresentation.getStateId())
                .name(stateHistoryViewRepresentation.getStateName()).build())
            .recommendation(stateHistoryViewRepresentation.getRecommendation() != null ?
                stateHistoryViewRepresentation.getRecommendation() == 1 : null)
            .reviewerName(stateHistoryViewRepresentation.getReviewerName())
            .recommendedPositionLevel(stateHistoryViewRepresentation.getRecommendedPositionLevel())
            .build();
      } catch (ParseException e) {
        RestResponse restResponse = new RestResponse(e.getMessage());
        return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
      }

      stateHistoryDTO.setApplicationDTO(applicationsService.getApplicationDtoById(applicationId));
      if (stateId != null && stateId == 1) {
        stateHistoryDTO.getApplicationDTO().setChannelId(channelService.getChannelDtoByName(stateHistoryViewRepresentation.getChannelName()).getId());
        stateHistoryDTO.getApplicationDTO().setPositionId(positionService.getPositionDtoByName(stateHistoryViewRepresentation.getPositionName()).getId());
      }

      statesHistoryService.saveStateHistory(stateHistoryDTO);
      return new ResponseEntity<>(Collections.singletonMap("applicationId", applicationId), HttpStatus.OK);
    }
  }

  protected void validateNewState(StateHistoryViewRepresentation stateHistoryViewRepresentation, BindingResult bindingResult) {
      if (positionService.getPositionDtoByName(stateHistoryViewRepresentation.getPositionName()) == null) {
        bindingResult.rejectValue(FIELD_POSITION_NAME, null, POSITION_NOT_FOUND_MESSAGE_KEY);
      }
      if (channelService.getChannelDtoByName(stateHistoryViewRepresentation.getChannelName()) == null) {
        bindingResult.rejectValue(FIELD_CHANNEL_NAME, null, CHANNEL_NOT_FOUND_MESSAGE_KEY);
      }
  }
}