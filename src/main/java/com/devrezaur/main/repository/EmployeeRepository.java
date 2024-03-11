package com.devrezaur.main.repository;

import com.devrezaur.main.service.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // You can add custom query methods here if needed
}
