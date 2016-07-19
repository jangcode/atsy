package com.epam.rft.atsy.web.controllers.rest;

import com.epam.rft.atsy.service.ChannelService;
import com.epam.rft.atsy.service.domain.ChannelDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import com.epam.rft.atsy.web.ErrorResponse;
import com.epam.rft.atsy.web.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;
import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/secure/channels")
public class ChannelController {
  private static final String DUPLICATE_POSITION_MESSAGE_KEY = "settings.channels.error.duplicate";
  private static final String EMPTY_POSITION_NAME_MESSAGE_KEY = "settings.channels.error.empty";
  private static final String TECHNICAL_ERROR_MESSAGE_KEY = "technical.error.message";
  private static final Logger LOGGER = LoggerFactory.getLogger(ChannelController.class);

  @Resource
  private ChannelService channelService;
  @Resource
  private MessageSource messageSource;

  @RequestMapping(method = RequestMethod.GET)
  public Collection<ChannelDTO> getChannels() {
    return channelService.getAllChannels();
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ErrorResponse> saveOrUpdate(@RequestBody ChannelDTO channelDTO,
                                                    BindingResult result, Locale locale) {
    if (!result.hasErrors()) {
      channelService.saveOrUpdate(channelDTO);

      return new ResponseEntity<>(new ErrorResponse(), HttpStatus.OK);
    } else {
      String errorMessage = messageSource.getMessage(EMPTY_POSITION_NAME_MESSAGE_KEY,
          null, locale);

      ErrorResponse errorResponse = new ErrorResponse(errorMessage);

      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(DuplicateRecordException.class)
  public ResponseEntity handleDuplicateException(Locale locale, DuplicateRecordException ex) {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaTypes.TEXT_PLAIN_UTF8);

    return new ResponseEntity<>(messageSource.getMessage(DUPLICATE_POSITION_MESSAGE_KEY,
        new Object[]{ex.getName()}, locale), headers, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleException(Locale locale, Exception ex) {
    LOGGER.error("Error while saving channel changes", ex);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaTypes.TEXT_PLAIN_UTF8);

    return new ResponseEntity<>(messageSource.getMessage(TECHNICAL_ERROR_MESSAGE_KEY,
        null, locale), headers, HttpStatus.BAD_REQUEST);
  }


}
