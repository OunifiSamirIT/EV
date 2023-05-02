package com.ala.ala.Repositories;

import com.ala.ala.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

}
