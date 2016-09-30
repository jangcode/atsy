package com.epam.rft.atsy.web.controllers.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.rft.atsy.service.CandidateService;
import com.epam.rft.atsy.service.domain.CandidateDTO;
import com.epam.rft.atsy.web.controllers.AbstractControllerTest;
import com.epam.rft.atsy.web.helper.CandidateValidatorHelper;
import com.epam.rft.atsy.web.messageresolution.MessageKeyResolverImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class SingleCandidateControllerTest extends AbstractControllerTest {
  private static final String REQUEST_URL_TO_SAVE_OR_UPDATE = "/secure/candidate";

  private static final String REQUEST_URL_TO_VALIDATE = "/secure/candidate/validate";

  private static final String COMMON_INVALID_INPUT_MESSAGE_KEY = "common.invalid.input";

  private static final String NOT_NULL_MESSAGE_KEY = "javax.validation.constraints.NotNull.message";

  private static final String NAME_LENGTH_ERROR_MESSAGE_KEY = "candidate.error.name.long";

  private static final String EMAIL_LENGTH_ERROR_MESSAGE_KEY = "candidate.error.email.long";

  private static final String EMAIL_INCORRECT_ERROR_MESSAGE_KEY = "candidate.error.email.incorrect";

  private static final String PHONE_LENGTH_ERROR_MESSAGE_KEY = "candidate.error.phone.long";

  private static final String PHONE_INCORRECT_ERROR_MESSAGE_KEY = "candidate.error.phone.incorrect";

  private static final String REFERER_LENGTH_ERROR_MESSAGE_KEY = "candidate.error.referer.long";

  private static final String LANGUAGE_SKILL_TOO_LOW_MESSAGE_KEY =
      "javax.validation.constraints.Min.message";

  private static final String LANGUAGE_SKILL_TOO_HIGH_MESSAGE_KEY =
      "javax.validation.constraints.Max.message";

  private static final String CANDIDATE_ERROR_DUPLICATE_MESSAGE_KEY = "candidate.error.duplicate";

  private static final String JSON_PATH_ERROR_MESSAGE = "$.errorMessage";

  private static final String JSON_PATH_FIELD_NAME = "$.fields.name";

  private static final String JSON_PATH_FIELD_EMAIL = "$.fields.email";

  private static final String JSON_PATH_FIELD_PHONE = "$.fields.phone";

  private static final String JSON_PATH_FIELD_REFERRER = "$.fields.referer";

  private static final String JSON_PATH_FIELD_LANGUAGE_SKILL = "$.fields.languageSkill";

  private static final String
      CANDIDATE_DUPLICATE_ERROR_MESSAGE =
      "Candidate email or phone number already exists!";

  private final String missingName = null;
  private final String emptyName = StringUtils.EMPTY;
  private final String tooLongName = StringUtils.repeat('a', 101);
  private final String correctName = "John Doe";

  private final String missingEmail = null;
  private final String emptyEmail = StringUtils.EMPTY;
  private final String tooLongEmail = StringUtils.repeat('a', 255) + "@email.com";
  private final String incorrectEmail = "malformed";
  private final String correctEmail = "john@doe.com";

  private final String tooLongPhone = StringUtils.repeat('0', 21);
  private final String incorrectPhone = "malformed";
  private final String correctPhone = "06301111111";

  private final String tooLongReferer = StringUtils.repeat('a', 21);
  private final String correctReferer = "John Doe";

  private final Short tooLowLanguageSkill = -1;
  private final Short tooHighLanguageSkill = 11;
  private final Short correctLanguageSkill = 10;

  private final String correctDescription = "correctDescription";

  private final Long candidateId = 1L;

  @Mock
  private CandidateService candidateService;

  @Mock
  private CandidateValidatorHelper candidateValidatorHelper;

  @Mock
  private MessageKeyResolverImpl messageKeyResolver;

  @InjectMocks
  private SingleCandidateController singleCandidateController;

  /**
   * Contains a correct email and a correct name so there won't be {@code NotNull} validation errors
   * for these fields. In each method where we expect error response this object gets used and
   * filled with malformed input. That way we can ensure that only one error message will be
   * returned per test method.
   */
  private CandidateDTO baseCandidateDto;

  /**
   * All fields are filled with correct values.
   */
  private CandidateDTO correctCandidateDto;

  @Override
  protected Object[] controllersUnderTest() {
    return new Object[]{singleCandidateController};
  }

  @Override
  public void setUp() {
    super.setUp();

    // always return the message key so we don't have to mock each call to return
    // a message key specific error message
    given(messageKeyResolver.resolveMessageOrDefault(anyString()))
        .willAnswer(i -> i.getArgumentAt(0, String.class));

    given(messageKeyResolver.getMessage(anyString(), any(Object[].class), any(Locale.class)))
        .willAnswer(i -> i.getArgumentAt(0, String.class));

    baseCandidateDto = CandidateDTO.builder().name(correctName).email(correctEmail).build();

    correctCandidateDto =
        CandidateDTO.builder().name(correctName).email(correctEmail).phone(correctPhone)
            .referer(correctReferer).languageSkill(correctLanguageSkill)
            .description(correctDescription).build();
  }

  @Override
  protected LocalValidatorFactoryBean localValidatorFactoryBean() {
    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();

    validatorFactoryBean.setValidationMessageSource(messageKeyResolver);

    return validatorFactoryBean;
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenNameIsNull() throws Exception {
    baseCandidateDto.setName(missingName);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.name").exists())
        .andExpect(jsonPath("$.fields.name", equalTo(NOT_NULL_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenNameIsEmpty() throws Exception {
    baseCandidateDto.setName(emptyName);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.name").exists())
        .andExpect(jsonPath("$.fields.name", equalTo(NAME_LENGTH_ERROR_MESSAGE_KEY)))
        .andReturn();

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenNameIsTooLong() throws Exception {
    baseCandidateDto.setName(tooLongName);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.name").exists())
        .andExpect(jsonPath("$.fields.name", equalTo(NAME_LENGTH_ERROR_MESSAGE_KEY)))
        .andReturn();

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenEmailIsNull() throws Exception {
    baseCandidateDto.setEmail(missingEmail);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.email").exists())
        .andExpect(jsonPath("$.fields.email", equalTo(NOT_NULL_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenEmailIsEmpty() throws Exception {
    baseCandidateDto.setEmail(emptyEmail);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.email").exists())
        .andExpect(jsonPath("$.fields.email", equalTo(EMAIL_INCORRECT_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenEmailIsTooLong() throws Exception {
    baseCandidateDto.setEmail(tooLongEmail);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.email").exists())
        .andExpect(jsonPath("$.fields.email", equalTo(EMAIL_LENGTH_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenEmailIsIncorrect() throws Exception {
    baseCandidateDto.setEmail(incorrectEmail);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.email").exists())
        .andExpect(jsonPath("$.fields.email", equalTo(EMAIL_INCORRECT_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenPhoneIsTooLong() throws Exception {
    baseCandidateDto.setPhone(tooLongPhone);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.phone").exists())
        .andExpect(jsonPath("$.fields.phone", equalTo(PHONE_LENGTH_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenPhoneIsIncorrect() throws Exception {
    baseCandidateDto.setPhone(incorrectPhone);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.phone").exists())
        .andExpect(jsonPath("$.fields.phone", equalTo(PHONE_INCORRECT_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenRefererIsTooLong() throws Exception {
    baseCandidateDto.setReferer(tooLongReferer);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.referer").exists())
        .andExpect(jsonPath("$.fields.referer", equalTo(REFERER_LENGTH_ERROR_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenLanguageSkillIsTooLow()
      throws Exception {
    baseCandidateDto.setLanguageSkill(tooLowLanguageSkill);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.languageSkill").exists())
        .andExpect(jsonPath("$.fields.languageSkill", equalTo(LANGUAGE_SKILL_TOO_LOW_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithErrorResponseWhenLanguageSkillIsTooHigh()
      throws Exception {
    baseCandidateDto.setLanguageSkill(tooHighLanguageSkill);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, baseCandidateDto))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", equalTo(COMMON_INVALID_INPUT_MESSAGE_KEY)))
        .andExpect(jsonPath("$.fields.languageSkill").exists())
        .andExpect(
            jsonPath("$.fields.languageSkill", equalTo(LANGUAGE_SKILL_TOO_HIGH_MESSAGE_KEY)));

    verifyZeroInteractions(candidateService);
  }

  @Test
  public void saveOrUpdateShouldRespondWithAnIdWhenPostedDtoIsCorrect() throws Exception {
    given(candidateService.saveOrUpdate(correctCandidateDto)).willReturn(candidateId);

    mockMvc.perform(buildJsonPostRequest(REQUEST_URL_TO_SAVE_OR_UPDATE, correctCandidateDto))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.id", equalTo(candidateId.intValue())));

    then(candidateService).should().saveOrUpdate(correctCandidateDto);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenNameIsNull() throws Exception {
    baseCandidateDto.setName(null);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_NAME, NOT_NULL_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenNameIsEmpty() throws Exception {
    baseCandidateDto.setName(emptyName);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_NAME, NAME_LENGTH_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenNameIsTooLong() throws Exception {
    baseCandidateDto.setName(tooLongName);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_NAME, NAME_LENGTH_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenEmailIsNull() throws Exception {
    baseCandidateDto.setEmail(null);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_EMAIL, NOT_NULL_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenEmailIsEmpty() throws Exception {
    baseCandidateDto.setEmail(emptyEmail);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_EMAIL, EMAIL_INCORRECT_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenEmailIsTooLong() throws Exception {
    baseCandidateDto.setEmail(tooLongEmail);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_EMAIL, EMAIL_LENGTH_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenEmailIsInCorrect() throws Exception {
    baseCandidateDto.setEmail(incorrectEmail);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_EMAIL, EMAIL_INCORRECT_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenPhoneIsTooLong() throws Exception {
    baseCandidateDto.setPhone(tooLongPhone);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_PHONE, PHONE_LENGTH_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenPhoneIsInCorrect() throws Exception {
    baseCandidateDto.setPhone(incorrectPhone);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_PHONE, PHONE_INCORRECT_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenReferrerIsTooLong() throws Exception {
    baseCandidateDto.setReferer(tooLongReferer);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_REFERRER, REFERER_LENGTH_ERROR_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenLanguageSkillIsToLow()
      throws Exception {
    baseCandidateDto.setLanguageSkill(tooLowLanguageSkill);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_LANGUAGE_SKILL, LANGUAGE_SKILL_TOO_LOW_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondWithAErrorJsonWhenLanguageSkillIsTooHigh()
      throws Exception {
    baseCandidateDto.setLanguageSkill(tooHighLanguageSkill);

    this.doTestValidateCandidateWhenThereIsValidationFieldError(baseCandidateDto,
        JSON_PATH_FIELD_LANGUAGE_SKILL, LANGUAGE_SKILL_TOO_HIGH_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondBadRequestWhenSavingCandidateAndEmailOrPhoneAlreadyExists()
      throws Exception {
    final CandidateDTO
        candidateDTO =
        CandidateDTO.builder().id(null).name(correctName).email(correctEmail).phone(correctPhone)
            .build();
    given(this.candidateValidatorHelper.isValidNonExistingCandidate(candidateDTO))
        .willReturn(false);
    given(this.messageKeyResolver.resolveMessageOrDefault(CANDIDATE_ERROR_DUPLICATE_MESSAGE_KEY))
        .willReturn(CANDIDATE_DUPLICATE_ERROR_MESSAGE);

    this.mockMvc
        .perform(buildJsonPostRequest(REQUEST_URL_TO_VALIDATE, candidateDTO))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(JSON_PATH_ERROR_MESSAGE).value(CANDIDATE_DUPLICATE_ERROR_MESSAGE));

    then(this.candidateValidatorHelper).should().isValidNonExistingCandidate(candidateDTO);
    then(this.candidateValidatorHelper).should(never())
        .isValidExistingCandidate(any(CandidateDTO.class));
    then(this.messageKeyResolver).should()
        .resolveMessageOrDefault(CANDIDATE_ERROR_DUPLICATE_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondOKWhenSavingCandidateAndEmailAndPhoneAreValid()
      throws Exception {
    final CandidateDTO
        candidateDTO =
        CandidateDTO.builder().id(null).name(correctName).email(correctEmail).phone(correctPhone)
            .build();
    given(this.candidateValidatorHelper.isValidNonExistingCandidate(candidateDTO))
        .willReturn(true);

    this.mockMvc
        .perform(buildJsonPostRequest(REQUEST_URL_TO_VALIDATE, candidateDTO))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_ERROR_MESSAGE).isEmpty());

    then(this.candidateValidatorHelper).should().isValidNonExistingCandidate(candidateDTO);
    then(this.candidateValidatorHelper).should(never())
        .isValidExistingCandidate(any(CandidateDTO.class));
    verifyZeroInteractions(this.messageKeyResolver);
  }

  @Test
  public void validateCandidateShouldRespondBadRequestWhenUpdatingCandidateAndEmailOrPhoneAlreadyExists()
      throws Exception {
    final CandidateDTO
        candidateDTO =
        CandidateDTO.builder().id(candidateId).name(correctName).email(correctEmail)
            .phone(correctPhone)
            .build();
    given(this.candidateValidatorHelper.isValidExistingCandidate(candidateDTO))
        .willReturn(false);
    given(this.messageKeyResolver.resolveMessageOrDefault(CANDIDATE_ERROR_DUPLICATE_MESSAGE_KEY))
        .willReturn(CANDIDATE_DUPLICATE_ERROR_MESSAGE);

    this.mockMvc
        .perform(buildJsonPostRequest(REQUEST_URL_TO_VALIDATE, candidateDTO))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(JSON_PATH_ERROR_MESSAGE).value(CANDIDATE_DUPLICATE_ERROR_MESSAGE));

    then(this.candidateValidatorHelper).should().isValidExistingCandidate(candidateDTO);
    then(this.candidateValidatorHelper).should(never())
        .isValidNonExistingCandidate(any(CandidateDTO.class));
    then(this.messageKeyResolver).should()
        .resolveMessageOrDefault(CANDIDATE_ERROR_DUPLICATE_MESSAGE_KEY);
  }

  @Test
  public void validateCandidateShouldRespondBadRequestWhenUpdatingCandidateAndEmailAndPhoneAreValid()
      throws Exception {
    final CandidateDTO
        candidateDTO =
        CandidateDTO.builder().id(candidateId).name(correctName).email(correctEmail)
            .phone(correctPhone)
            .build();
    given(this.candidateValidatorHelper.isValidExistingCandidate(candidateDTO))
        .willReturn(true);

    this.mockMvc
        .perform(buildJsonPostRequest(REQUEST_URL_TO_VALIDATE, candidateDTO))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_ERROR_MESSAGE).isEmpty());

    then(this.candidateValidatorHelper).should().isValidExistingCandidate(candidateDTO);
    then(this.candidateValidatorHelper).should(never())
        .isValidNonExistingCandidate(any(CandidateDTO.class));
    verifyZeroInteractions(this.messageKeyResolver);
  }

  private void doTestValidateCandidateWhenThereIsValidationFieldError(
      CandidateDTO candidateDTO, String jsonPathFieldName, String errorMessageKey)
      throws Exception {

    this.mockMvc
        .perform(buildJsonPostRequest(REQUEST_URL_TO_VALIDATE, candidateDTO))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath(JSON_PATH_ERROR_MESSAGE).value(COMMON_INVALID_INPUT_MESSAGE_KEY))
        .andExpect(jsonPath(jsonPathFieldName).exists())
        .andExpect(jsonPath(jsonPathFieldName).value(errorMessageKey));

    verifyZeroInteractions(this.candidateValidatorHelper);
  }
}
