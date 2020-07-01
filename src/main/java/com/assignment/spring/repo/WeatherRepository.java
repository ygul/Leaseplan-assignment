package com.assignment.spring.repo;


import com.assignment.spring.domain.WeatherEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {

    List<WeatherEntity> findAll();
}
