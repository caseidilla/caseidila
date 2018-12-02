package org.codet.caseidilla.user.repository;

import org.codet.caseidilla.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
