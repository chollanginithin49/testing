package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entities.Deliverable;
import com.task.entities.DeliverableID;

public interface DeliverableRepository extends JpaRepository<Deliverable,DeliverableID> {
    
}
