package com.epam.rft.atsy.service.domain.request;

/**
 * Created by tothd on 2015. 11. 07..
 */
public class CandidateRequestDTO {

    private String fieldName;

    private String order;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
