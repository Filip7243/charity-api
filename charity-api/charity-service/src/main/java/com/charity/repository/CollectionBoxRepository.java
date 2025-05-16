package com.charity.repository;

import com.charity.model.CollectionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {

    Integer countByFundraisingEventId(Long fundraisingEventId);

    @Query("select distinct cb from CollectionBox cb left join fetch cb.moneys")
    List<CollectionBox> findAllWithMoneys();

    @Query("select cb from CollectionBox cb left join fetch cb.moneys where cb.id = :id")
    Optional<CollectionBox> findByIdWithMoneys(@Param("id") Long id);
}
