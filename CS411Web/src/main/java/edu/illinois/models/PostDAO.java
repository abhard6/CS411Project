package edu.illinois.models;

/**
 * Created by nprince on 3/16/16.
 */
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface PostDao extends CrudRepository<Post, Long> {

} // class UserDao
