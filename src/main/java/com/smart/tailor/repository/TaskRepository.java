package com.smart.tailor.repository;

import com.smart.tailor.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}
