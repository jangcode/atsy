package com.epam.rft.atsy.web.controllers;

import com.epam.rft.atsy.service.CandidateService;
import com.epam.rft.atsy.service.domain.CandidateDTO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by mates on 2015. 11. 25..
 */
public class CandidateCreationControllerTest {

    @InjectMocks
    private CandidateCreationController underTest;

    @Mock
    CandidateService service;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadCandidateTest(){
        //when
        ModelAndView model = underTest.loadCandidate();
        Map<String, Object> testMap = model.getModel();
        //then
        assertThat(model.getViewName(), is("candidate_create"));
        assertThat(testMap.containsKey("candidate"),is(true));
    }

    @Test
    public void loadCandidateTestSpecified(){
        //given
        Long candidateId=new Long(1);
        given(service.getCandidate(candidateId)).willReturn(new CandidateDTO("Candidate A", "candidate.a@atsy.com", "+36105555555", "Elegáns, kicsit furi", "google", (short)5));
        //when
        ModelAndView model = underTest.loadCandidate(candidateId);
        Map<String, Object> testMap = model.getModel();
        //then
        assertThat(model.getViewName(), is("candidate_create"));
        assertThat(testMap.containsKey("candidate"),is(true));
        assertThat(testMap.get("candidate"), is(service.getCandidate(candidateId)));
    }
}