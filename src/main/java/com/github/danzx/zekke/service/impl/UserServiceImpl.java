/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.service.impl;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.persistence.dao.UserDao;
import com.github.danzx.zekke.security.crypto.HashFunction;
import com.github.danzx.zekke.service.ServiceException;
import com.github.danzx.zekke.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Default User service implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Validated @Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final HashFunction hashFunction;

    public @Inject UserServiceImpl(UserDao userDao, HashFunction hashFunction) {
        this.userDao = requireNonNull(userDao);
        this.hashFunction = hashFunction;
    }

    @Override
    public boolean isAdminRegistered(User user) {
        log.debug("user: {}", user);
        User.Role adminRole = User.Role.ADMIN;
        if (adminRole != user.getRole()) {
            throw new ServiceException.Builder()
                .messageKey("user.not_admin.error")
                .build();
        }
        Optional<User> adminUser = userDao.findById(adminRole.getUserId());
        String encodedPassword = hashFunction.hash(user.getPassword().get());
        return adminUser
                .flatMap(User::getPassword)
                .map(adminPassword -> Objects.equals(encodedPassword, adminPassword))
                .orElseThrow(() -> new ServiceException.Builder().messageKey("admin.not.found").build());
    }
}
