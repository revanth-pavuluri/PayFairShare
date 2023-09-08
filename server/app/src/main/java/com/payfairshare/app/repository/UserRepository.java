package com.payfairshare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findById(Long id);
    User findByUsername(String name);
    List<Group> findByUserGroupsId(Long id);
}
