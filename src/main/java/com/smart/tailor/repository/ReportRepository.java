package com.smart.tailor.repository;

import com.smart.tailor.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
}
