package com.employee.project.repo;

import com.employee.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    boolean existsByMail(String mail);

    boolean existsByFirstName(String firstName);

    boolean existsByLastName(String lastName);

    boolean existsByPhonenumbersIn(List<String> phonenumbers);
}
