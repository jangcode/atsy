package com.epam.rft.atsy.service.impl;

import com.epam.rft.atsy.persistence.entities.ChannelEntity;
import com.epam.rft.atsy.persistence.repositories.ChannelRepository;
import com.epam.rft.atsy.service.ChannelService;
import com.epam.rft.atsy.service.domain.ChannelDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ModelMapper modelMapper;

    @Autowired
    private ChannelRepository channelRepository;

    private final static Type CHANNELDTO_LIST_TYPE = new TypeToken<List<ChannelDTO>>() {}.getType();

    @Override
    public Collection<ChannelDTO> getAllChannels() {
        Collection<ChannelEntity> ChannelEntities = channelRepository.findAll();
        return modelMapper.map(ChannelEntities, CHANNELDTO_LIST_TYPE);
    }

    @Override
    public void saveOrUpdate(ChannelDTO channel) {
        Assert.notNull(channel);
        ChannelEntity entity = modelMapper.map(channel, ChannelEntity.class);
        try {
            channelRepository.save(entity);
        } catch (ConstraintViolationException | DataIntegrityViolationException ex) {
            log.error("Save to repository failed.", ex);

            String channelName = channel.getName();

            throw new DuplicateRecordException(channelName,
                                               "Duplication occurred when saving channel: " + channelName, ex);
        }
    }
}
