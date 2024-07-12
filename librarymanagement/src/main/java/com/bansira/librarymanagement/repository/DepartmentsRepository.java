package com.bansira.librarymanagement.repository;

import com.bansira.librarymanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentsRepository extends JpaRepository<Department, String> {
}
