package com.epam.rft.atsy.service.impl;

import com.epam.rft.atsy.persistence.entities.PasswordHistoryEntity;
import com.epam.rft.atsy.persistence.entities.UserEntity;
import com.epam.rft.atsy.persistence.repositories.PasswordHistoryRepository;
import com.epam.rft.atsy.persistence.repositories.UserRepository;
import com.epam.rft.atsy.service.ConverterService;
import com.epam.rft.atsy.service.PasswordChangeService;
import com.epam.rft.atsy.service.domain.PasswordHistoryDTO;
import com.epam.rft.atsy.service.exception.DuplicateRecordException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasswordChangeServiceImpl implements PasswordChangeService {

  private static final int PASSWORD_HISTORY_LIMIT = 5;

  @Autowired
  private ConverterService converterService;

  @Autowired
  private PasswordHistoryRepository passwordHistoryRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  @Override
  public Long saveOrUpdate(PasswordHistoryDTO passwordHistoryDTO) {
    Assert.notNull(passwordHistoryDTO);
    Assert.notNull(passwordHistoryDTO.getUserId());
    PasswordHistoryEntity
        entity =
        converterService.convert(passwordHistoryDTO, PasswordHistoryEntity.class);

    UserEntity userEntity = userRepository.findOne(passwordHistoryDTO.getUserId());
    Assert.notNull(userEntity);

    entity.setUserEntity(userEntity);
    try {
      Long retId = passwordHistoryRepository.save(entity).getId();
      if (passwordHistoryRepository.findByUserEntity(entity.getUserEntity()).size()
          > PASSWORD_HISTORY_LIMIT) {
        deleteOldestPassword(passwordHistoryDTO.getUserId());
      }
      return retId;
    } catch (ConstraintViolationException | DataIntegrityViolationException ex) {
      log.error("Save to repository failed.", ex);

      String userId = passwordHistoryDTO.getUserId().toString();

      throw new DuplicateRecordException(userId,
          "Duplication occurred when saving password history for user with ID: "
              + userId, ex);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public List<String> getOldPasswords(Long userId) {
    Assert.notNull(userId);
    List<PasswordHistoryEntity>
        oldPasswords =
        passwordHistoryRepository.findByUserEntity(userRepository.findOne(userId));
    List<String> passwords = new ArrayList<>();
    for (PasswordHistoryEntity pass : oldPasswords) {
      passwords.add(pass.getPassword());
    }
    return passwords;
  }

  @Transactional
  @Override
  public void deleteOldestPassword(Long userId) {
    Assert.notNull(userId);
    PasswordHistoryEntity
        entity =
        passwordHistoryRepository.findTop1ByUserEntityIdOrderByChangeDate(userId);
    passwordHistoryRepository.delete(entity);
  }
}
