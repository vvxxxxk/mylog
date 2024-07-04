package com.golym.mylog.repository;

import com.golym.mylog.model.entity.AuthEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthEmailRepository extends CrudRepository<AuthEmail, String> {
}
