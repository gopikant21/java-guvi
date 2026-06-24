package org.example.springdatajpademo.repository;

import org.example.springdatajpademo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Custom query to find employees by name
    @Query("SELECT e FROM Employee e WHERE e.name = :name")
    List<Employee> findEmployeesByName(@Param("name") String name);

    // Custom query to find employees by email
    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Employee findEmployeeByEmail(@Param("email") String email);

    // Custom query to find employees by name containing a substring
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    List<Employee> findEmployeesByNameContaining(@Param("namePart") String namePart);

    // Update query to update employee email by id
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.email = :newEmail WHERE e.id = :id")
    long updateEmployeeEmailById(@Param("id") Long id, @Param("newEmail") String newEmail);

    // Update query to update employee name by id
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.name = :newName WHERE e.id = :id")
    long updateEmployeeNameById(@Param("id") Long id, @Param("newName") String newName);

    // Custom query to count employees
    @Query("SELECT COUNT(e) FROM Employee e")
    long countAllEmployees();

    // Custom query to find all employees ordered by name
    @Query("SELECT e FROM Employee e ORDER BY e.name ASC")
    List<Employee> findAllOrderByName();
}
