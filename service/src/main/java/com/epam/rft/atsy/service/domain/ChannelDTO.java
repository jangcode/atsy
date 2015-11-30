package com.epam.rft.atsy.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Ikantik.
 */
public class ChannelDTO implements Serializable {
    private Long channelId;
    @NotNull
    @Size(min = 1)
    private String name;

    public ChannelDTO(Long channelId, String name) {
        this.channelId = channelId;
        this.name = name;
    }

    public ChannelDTO() {
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChannelDTO that = (ChannelDTO) o;

        return new EqualsBuilder()
                .append(channelId, that.channelId)
                .append(name, that.name)
                .isEquals();
    }
}