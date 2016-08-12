package com.epam.rft.atsy.web.controllers;

import com.epam.rft.atsy.service.StateFlowService;
import com.epam.rft.atsy.service.StateService;
import com.epam.rft.atsy.service.StatesHistoryService;
import com.epam.rft.atsy.service.domain.states.StateDTO;
import com.epam.rft.atsy.service.domain.states.StateHistoryDTO;
import com.epam.rft.atsy.web.StateHistoryViewRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Manages the page which shows the state history of a certain application of a certain candidate
 * and allows the user to put the application into a new state.
 */
@Controller
@RequestMapping(value = "/secure/application_state")
public class ApplicationStateController {

  private static final String VIEW_NAME = "application_state";
  private static final String APPLICATION_STATE = "candidate.table.state.";
  private static final String STATES_OBJECT_KEY = "states";
  private static final String STATE_FLOW_OBJECT_KEY = "stateflows";

  private static final String DATE_FORMAT_CONSTANT = "yyyy-MM-dd HH:mm:ss";

  private final static Type
      STATE_HISTORY_VIEW_REPRESENTATION_LIST_TYPE =
      new TypeToken<List<StateHistoryViewRepresentation>>() {
      }.getType();

  @Resource
  private StatesHistoryService statesHistoryService;

  @Resource
  private StateFlowService stateFlowService;

  @Resource
  private StateService stateService;

  @Resource
  private MessageSource messageSource;

  @Resource
  private ModelMapper modelMapper;


  /**
   * Creates the application state page and fills it with all the state information of the given
   * application.
   * @param applicationId determines which application is viewed on the page
   * @param clickedState fsdfsdfdffdfa
   * @param locale used to determine which language should be used on the page
   * @return a ModelAndView object which contains all the state information of the given application
   * and the name of the page that manages the application states
   */
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView loadPage(@RequestParam Long applicationId,
                               @RequestParam(required = false, name = "state") String clickedState,
                               Locale locale) {
    ModelAndView modelAndView = new ModelAndView(VIEW_NAME);
    modelAndView.addObject("applicationId", applicationId);

    List<StateHistoryViewRepresentation>
        stateHistoryViewRepresentations =
        modelMapper.map(statesHistoryService.getStateHistoriesByApplicationId(applicationId),
            STATE_HISTORY_VIEW_REPRESENTATION_LIST_TYPE);

    if (clickedState != null) {
      StateDTO clickedStateDTO = stateService.getStateDtoByName(clickedState);

      stateHistoryViewRepresentations.add(0, StateHistoryViewRepresentation.builder()
          .stateId(clickedStateDTO.getId())
          .stateName(clickedStateDTO.getName())
          .build());
    }

    for (StateHistoryViewRepresentation stateHistoryViewRepresentation : stateHistoryViewRepresentations) {
      String stateType = stateHistoryViewRepresentation.getStateName();
      stateType =
          messageSource
              .getMessage(APPLICATION_STATE + stateHistoryViewRepresentation.getStateName(),
                  new Object[]{stateType}, locale);
      stateHistoryViewRepresentation.setStateFullName(stateType);
    }

    modelAndView.addObject(STATE_FLOW_OBJECT_KEY, stateFlowService.getStateFlowDTOByFromStateDTO(
        new StateDTO(stateHistoryViewRepresentations.get(0).getStateId(),
            stateHistoryViewRepresentations.get(0).getStateName())));
    modelAndView.addObject(STATES_OBJECT_KEY, stateHistoryViewRepresentations);
    return modelAndView;
  }

  /**
   * This method is used to save new states or update the information of the latest state.
   * @param applicationId identifier of the application whose states are viewed and edited
   * @param stateHistoryViewRepresentation this attribute contains all the state information of the
   * given application
   * @return a ModelAndView object filled with the data to stay on the same page and view the states
   * of the same application, but including the latest modification
   */
  @RequestMapping(method = RequestMethod.POST)
  public ModelAndView saveOrUpdate(@RequestParam Long applicationId,
                                   @Valid @ModelAttribute StateHistoryViewRepresentation stateHistoryViewRepresentation) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_CONSTANT);

    StateHistoryDTO
        stateHistoryDTO = null;

    try {
      stateHistoryDTO = StateHistoryDTO.builder()
          .id(stateHistoryViewRepresentation.getId())
          .languageSkill(stateHistoryViewRepresentation.getLanguageSkill())
          .description(stateHistoryViewRepresentation.getDescription())
          .result(stateHistoryViewRepresentation.getResult())
          .offeredMoney(stateHistoryViewRepresentation.getOfferedMoney())
          .claim(stateHistoryViewRepresentation.getClaim())
          .feedbackDate(stateHistoryViewRepresentation.getFeedbackDate() != null
              && !stateHistoryViewRepresentation.getFeedbackDate().isEmpty() ? simpleDateFormat
              .parse(stateHistoryViewRepresentation.getFeedbackDate()) : null)
          .dayOfStart(stateHistoryViewRepresentation.getDayOfStart() != null
              && !stateHistoryViewRepresentation.getDayOfStart().isEmpty() ? simpleDateFormat
              .parse(stateHistoryViewRepresentation.getDayOfStart()) : null)
          .creationDate(stateHistoryViewRepresentation.getCreationDate() != null
              && !stateHistoryViewRepresentation.getCreationDate().isEmpty() ? simpleDateFormat
              .parse(stateHistoryViewRepresentation.getCreationDate()) : null)
          .stateDTO(StateDTO.builder().id(stateHistoryViewRepresentation.getStateId())
              .name(stateHistoryViewRepresentation.getStateName()).build())
          .build();

    } catch (ParseException e) {
      e.printStackTrace();
    }

    statesHistoryService.saveStateHistory(stateHistoryDTO, applicationId);

    return new ModelAndView("redirect:/secure/application_state?applicationId=" + applicationId);
  }
}
