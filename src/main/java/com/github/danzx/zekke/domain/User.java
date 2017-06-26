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
package com.github.danzx.zekke.domain;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.util.Strings.hidden;

import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.github.danzx.zekke.domain.constraint.CheckUser;

import org.mongodb.morphia.annotations.Entity;

/** 
 * Represents a user of the application. 
 * 
 * @author Daniel Pedraza-Arcega 
 */
@CheckUser
@Entity(value = "users", noClassnameStored = true)
public class User extends BaseEntity<Long> {

    public enum Role {
        ANONYMOUS(1), ADMIN(2);

        private final long userId;

        Role(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }
    }

    @NotNull private Role role;
    private String password;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = requireNonNull(role);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isUserEqualTo((User) obj);
    }

    /**
     * Use this method to complete your equals method.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the argument; {@code false} otherwise.
     */
    protected boolean isUserEqualTo(User other) {
        return isEntityEqualTo(other) &&
               Objects.equals(role, other.role) && 
               Objects.equals(password, other.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role, password);
    }

    @Override
    public String toString() {
        return "{ _id:" + getId() + ", role:" + role + ", password:" + hidden(password) + " }";
    }
}
