package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TrendDao extends CrudRepository<Trend, Long> {

} // class TrendDao
