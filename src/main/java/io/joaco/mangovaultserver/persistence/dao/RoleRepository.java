package io.joaco.mangovaultserver.persistence.dao;

import io.joaco.mangovaultserver.persistence.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

}
