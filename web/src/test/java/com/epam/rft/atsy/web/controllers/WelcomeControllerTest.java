package com.epam.rft.atsy.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

public class WelcomeControllerTest extends AbstractControllerTest {
  private static final String VIEW_NAME = "candidates";

  private static final String REQUEST_URL = "/secure/welcome";

  @Override
  protected Object[] controllersUnderTest() {
    return new Object[]{new WelcomeController()};
  }

  @Test
  public void loadPageShouldRenderCandidatesView() throws Exception {
    mockMvc.perform(get(REQUEST_URL))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_NAME))
        .andExpect(forwardedUrl(VIEW_PREFIX + VIEW_NAME + VIEW_SUFFIX));
  }
}
