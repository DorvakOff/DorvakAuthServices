package com.dorvak.das.repositories;

import com.dorvak.das.models.Oauth2Application;
import org.springframework.data.repository.CrudRepository;

public interface Oauth2ApplicationRepository extends CrudRepository<Oauth2Application, String> {
    Oauth2Application findByClientId(String clientId);
}
