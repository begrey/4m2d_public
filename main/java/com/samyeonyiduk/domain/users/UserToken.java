package com.samyeonyiduk.domain.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class UserToken {

    private Long id;

    private Long apiId;

    private String intraId;

    public UserToken(Users entity) {
        this.id = entity.getId();
        this.apiId = entity.getApiId();
        this.intraId = entity.getIntraId();
    }

}
