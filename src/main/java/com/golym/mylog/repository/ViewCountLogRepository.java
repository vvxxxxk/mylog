package com.golym.mylog.repository;

import com.golym.mylog.model.entity.ViewCountLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewCountLogRepository extends CrudRepository<ViewCountLogEntity, String> {
}
