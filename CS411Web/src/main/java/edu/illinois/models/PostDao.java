package edu.illinois.models;

/**
 * Created by nprince on 3/16/16.
 */
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

@Transactional
public interface PostDao extends CrudRepository<Post, Long> {
    @Modifying
    @Query("DELETE FROM Post WHERE timestamp < :olderThan")
    Long deleteOlderThan(@Param("olderThan") Timestamp olderThan);
} // class UserDao
