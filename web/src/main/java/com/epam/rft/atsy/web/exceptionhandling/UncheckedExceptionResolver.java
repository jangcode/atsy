package com.epam.rft.atsy.web.exceptionhandling;

import com.epam.rft.atsy.web.messageresolution.MessageKeyResolver;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.WebUtils;

/**
 * This class handles the unchecked exceptions thrown by the controllers. Registered with the
 * highest precedence among the handlers, it renders a generic error page upon receiving an ordinary
 * HTTP request or sends a JSON response when it detects an AJAX request.
 *
 * Maintains a list of exception classes that require a more specific handling and therefore should
 * be passed to resolvers with lower precedence.
 */
public class UncheckedExceptionResolver implements HandlerExceptionResolver {
  private static final ModelAndView PASS_TO_NEXT_RESOLVER = null;

  private static final String ERROR_VIEW_NAME = "error";

  private static final String TECHNICAL_ERROR_MESSAGE_KEY = "technical.error.message";

  private MappingJackson2JsonView jsonView;

  private MessageKeyResolver messageKeyResolver;

  private final Map<Class<? extends Exception>, String>
      errorMessageKeyMap =
      ImmutableMap.<Class<? extends Exception>, String>builder()
          .build();

  public UncheckedExceptionResolver(MappingJackson2JsonView jsonView, MessageKeyResolver messageKeyResolver) {
    this.jsonView = jsonView;
    this.messageKeyResolver = messageKeyResolver;
  }

  @Override
  public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse, Object o,
                                       Exception e) {
    // if e is not an unchecked exception, let other handlers process it
    if (!(e instanceof RuntimeException)) {
      return PASS_TO_NEXT_RESOLVER;
    }

    int httpStatusCode =
        AnnotationUtils.isAnnotationDeclaredLocally(ResponseStatus.class, e.getClass())
            ? AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class).value().value()
            : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    String
        errorMessageKey =
        errorMessageKeyMap.getOrDefault(e.getClass(), TECHNICAL_ERROR_MESSAGE_KEY);
    String errorMessage = messageKeyResolver.resolveMessageOrDefault(errorMessageKey);

    httpServletResponse.setStatus(httpStatusCode);
    httpServletRequest.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, httpStatusCode);
    httpServletRequest.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, errorMessage);

    if (!RequestInspector.isAjaxRequest(httpServletRequest)) {
      return new ModelAndView(ERROR_VIEW_NAME);
    } else {
      RestResponse restResponse = new RestResponse(e.getMessage());

      ModelAndView modelAndView = new ModelAndView(jsonView);

      modelAndView.addObject("errorMessage", restResponse.getErrorMessage());
      modelAndView.addObject("fields", restResponse.getFields());

      return modelAndView;
    }
  }
}
