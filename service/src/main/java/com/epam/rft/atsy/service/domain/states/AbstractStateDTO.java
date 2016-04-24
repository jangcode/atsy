package com.epam.rft.atsy.service.domain.states;

import com.epam.rft.atsy.service.domain.ApplicationDTO;
import com.epam.rft.atsy.service.domain.PositionDTO;

import java.util.Date;

public abstract class AbstractStateDTO {

    private Long stateId;
    private Long candidateId;
    private PositionDTO position;
    private ApplicationDTO applicationDTO;
    private Short languageSkill;
    private String description;
    private String result;
    private Long offeredMoney;
    private Long claim;
    private Date feedbackDate;
    private String stateType;
    private Integer stateIndex;

    public AbstractStateDTO() {
    }

    public AbstractStateDTO(Long stateId, Long candidateId, PositionDTO position, ApplicationDTO applicationDTO, Short languageSkill, String description, String result, Long offeredMoney, Long claim, Date feedbackDate, String stateType, Integer stateIndex) {
        this.stateId = stateId;
        this.candidateId = candidateId;
        this.position = position;
        this.applicationDTO = applicationDTO;
        this.languageSkill = languageSkill;
        this.description = description;
        this.result = result;
        this.offeredMoney = offeredMoney;
        this.claim = claim;
        this.feedbackDate = feedbackDate;
        this.stateType = stateType;
        this.stateIndex = stateIndex;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public ApplicationDTO getApplicationDTO() {
        return applicationDTO;
    }

    public void setApplicationDTO(ApplicationDTO applicationDTO) {
        this.applicationDTO = applicationDTO;
    }

    public Short getLanguageSkill() {
        return languageSkill;
    }

    public void setLanguageSkill(Short languageSkill) {
        this.languageSkill = languageSkill;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getOfferedMoney() {
        return offeredMoney;
    }

    public void setOfferedMoney(Long offeredMoney) {
        this.offeredMoney = offeredMoney;
    }

    public Long getClaim() {
        return claim;
    }

    public void setClaim(Long claim) {
        this.claim = claim;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public Integer getStateIndex() {
        return stateIndex;
    }

    public void setStateIndex(Integer stateIndex) {
        this.stateIndex = stateIndex;
    }
}