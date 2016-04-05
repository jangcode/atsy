package com.epam.rft.atsy.web.controllers.rest;

import com.epam.rft.atsy.service.ApplicationService;
import com.epam.rft.atsy.service.domain.CandidateApplicationDTO;
import com.epam.rft.atsy.service.domain.states.StateDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Locale;

@RestController
@RequestMapping(value = "/secure/applications_states/{stateId}")
public class ApplicationStatesController {

    @Resource
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<StateDTO> loadApplications(@PathVariable(value = "stateId") Long stateId, Locale locale) {
        Collection<StateDTO> applicationStates = applicationService.getStatesByStateId(stateId);
        return applicationStates;
    }
}
