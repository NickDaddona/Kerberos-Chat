package edu.ccsu.cs492.kerberoschat.user.repository;

import edu.ccsu.cs492.kerberoschat.user.entity.AppRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AppRoleRepository extends CrudRepository<AppRole, Integer> {

}
