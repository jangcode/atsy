package com.epam.rft.atsy.web.controllers.rest;

import com.epam.rft.atsy.service.CandidateService;
import com.epam.rft.atsy.service.domain.CandidateDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import com.epam.rft.atsy.web.exceptionhandling.ErrorResponse;
import com.epam.rft.atsy.web.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/secure/candidate")
public class SingleCandidateController {
  private static final String DUPLICATE_CANDIDATE_ERROR_KEY = "candidate.error.duplicate";
  private static final String TECHNICAL_ERROR_MESSAGE_KEY = "technical.error.message";
  private static final String COMMON_INVALID_INPUT_MESSAGE_KEY = "common.invalid.input";
  private static final Logger LOGGER = LoggerFactory.getLogger(SingleCandidateController.class);

  @Resource
  private CandidateService candidateService;

  @Resource
  private MessageSource messageSource;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity saveOrUpdate(@Valid @RequestBody CandidateDTO candidateDTO,
                                     BindingResult result, Locale locale) {
    if (!result.hasErrors()) {
      Long candidateId = candidateService.saveOrUpdate(candidateDTO);

      return new ResponseEntity<>(candidateId, HttpStatus.OK);
    } else {
      ErrorResponse errorResponse = parseValidationErrors(result.getFieldErrors(), locale);

      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(DuplicateRecordException.class)
  public ResponseEntity handleDuplicateException(Locale locale, DuplicateRecordException ex) {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaTypes.TEXT_PLAIN_UTF8);

    return new ResponseEntity<>(messageSource.getMessage(DUPLICATE_CANDIDATE_ERROR_KEY,
        new Object[]{ex.getName()}, locale), headers, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleException(Locale locale, Exception ex) {
    LOGGER.error("Error while saving position changes", ex);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaTypes.TEXT_PLAIN_UTF8);

    return new ResponseEntity<>(messageSource.getMessage(TECHNICAL_ERROR_MESSAGE_KEY,
        null, locale), headers, HttpStatus.BAD_REQUEST);
  }

  private ErrorResponse parseValidationErrors(List<FieldError> fieldErrors, Locale locale) {
    String errorMessage = messageSource.getMessage(COMMON_INVALID_INPUT_MESSAGE_KEY, null, locale);

    ErrorResponse errorResponse = new ErrorResponse(errorMessage);

    for (FieldError fieldError : fieldErrors) {
      errorResponse.addField(fieldError.getField(),
          messageSource.getMessage(fieldError.getDefaultMessage(), new Object[0], locale));
    }

    return errorResponse;
  }
}
