package com.epam.rft.atsy.web.exceptionhandling;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles the checked exceptions thrown by the controllers. Unchecked exceptions are
 * handled by the {@code UncheckedExceptionResolver}. This is the last handler that catches all
 * uncaught exceptions. Registers itself among the handlers and renders a generic error page upon
 * receiving an ordinary HTTP request or sends a JSON response when it detects an AJAX request.
 */
@Component
public class DefaultExceptionResolver extends AbstractExceptionResolver {
  private static final String TECHNICAL_ERROR_MESSAGE_KEY = "technical.error.message";

  @Resource
  private MessageSource messageSource;

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE + 2;
  }

  @Override
  public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse, Object o,
                                       Exception e) {
    httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    if (!isAjaxRequest(httpServletRequest)) {
      return new ModelAndView(ERROR_VIEW_NAME);
    } else {
      ModelAndView modelAndView = new ModelAndView(jsonView);

      String message =
          messageSource
              .getMessage(TECHNICAL_ERROR_MESSAGE_KEY, null, httpServletRequest.getLocale());

      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

      return errorResponseToJsonModelAndView(errorResponse);
    }
  }
}
