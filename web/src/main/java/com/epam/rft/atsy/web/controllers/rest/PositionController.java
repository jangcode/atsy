package com.epam.rft.atsy.web.controllers.rest;

import com.epam.rft.atsy.service.PositionService;
import com.epam.rft.atsy.service.domain.PositionDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Locale;


/**
 * Created by Ikantik.
 */
@RestController
@RequestMapping(value = "/secure/positions")
public class PositionController {
    private static final String DUPLICATE_POSITION_MESSAGE_KEY = "settings.positions.error.duplicate";
    private static final String EMPTY_POSITION_NAME_MESSAGE_KEY = "settings.positions.error.empty";
    private static final String TECHNICAL_ERROR_MESSAGE_KEY = "technical.error.message";
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionController.class);
    @Resource
    private PositionService positionService;
    @Resource
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<PositionDTO> getPositions() {
        return positionService.getAllPositions();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> saveOrUpdate(@RequestBody PositionDTO positionDTO, BindingResult result, Locale locale) {
        ResponseEntity entity = new ResponseEntity<String>("", HttpStatus.OK);

        if (!result.hasErrors()) {
            positionService.saveOrUpdate(positionDTO);
        } else {
            entity = new ResponseEntity<String>(messageSource.getMessage(EMPTY_POSITION_NAME_MESSAGE_KEY,
                    null, locale), HttpStatus.BAD_REQUEST);
        }
        return entity;
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity handleDuplicateException(Locale locale, DuplicateRecordException ex) {
        return new ResponseEntity<String>(messageSource.getMessage(DUPLICATE_POSITION_MESSAGE_KEY,
                new Object[]{ex.getName()}, locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Locale locale, Exception ex) {
        LOGGER.error("Error while saving position changes", ex);
        return new ResponseEntity<String>(messageSource.getMessage(TECHNICAL_ERROR_MESSAGE_KEY,
                null, locale), HttpStatus.BAD_REQUEST);
    }


}