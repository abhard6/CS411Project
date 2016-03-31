package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

@Transactional
public interface TrendDao extends CrudRepository<Trend, Long> {
    @Modifying
    @Query("DELETE FROM Trend WHERE createdAt < :olderThan")
    Long deleteOlderThan(@Param("olderThan") Timestamp olderThan);

    @Query("SELECT t FROM Trend t WHERE t.value=:val")
    List<Trend> findByValue(@Param("val") String val);
} // class TrendDao
