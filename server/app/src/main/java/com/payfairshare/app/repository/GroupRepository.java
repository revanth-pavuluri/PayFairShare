package com.payfairshare.app.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payfairshare.app.models.Group;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Long>{
    Optional<Group> findById(Long id);
    Group findByName(String name);
}
