package com.payfairshare.app.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payfairshare.app.models.FinalSplit;

import java.util.List;


@Repository
@Transactional
public interface FinalSplitRepository extends JpaRepository<FinalSplit,Long>{
    List<FinalSplit> findByGroupId(Long id);
    List<FinalSplit> findByGroupIdAndStatus(Long id,String status);
}
