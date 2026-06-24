package org.example.springdatajpademo.repository;

import org.example.springdatajpademo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByName(String name);

    // Custom query to find projects by name
    @Query("SELECT p FROM Project p WHERE p.name = :name")
    List<Project> findProjectsByName(@Param("name") String name);

    // Custom query to find projects by name containing a substring
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    List<Project> findProjectsByNameContaining(@Param("namePart") String namePart);

    // Custom query to find projects by employee id
    @Query("SELECT p FROM Project p JOIN p.employees e WHERE e.id = :employeeId")
    List<Project> findProjectsByEmployeeId(@Param("employeeId") Long employeeId);

    // Update query to update project name by id
    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.name = :newName WHERE p.id = :id")
    long updateProjectNameById(@Param("id") Long id, @Param("newName") String newName);

    // Custom query to count projects
    @Query("SELECT COUNT(p) FROM Project p")
    long countAllProjects();

    // Custom query to find all projects ordered by name
    @Query("SELECT p FROM Project p ORDER BY p.name ASC")
    List<Project> findAllOrderByName();

    // Custom query to find projects with specific number of employees
    @Query("SELECT p FROM Project p WHERE SIZE(p.employees) > :minEmployees")
    List<Project> findProjectsWithMoreThanEmployees(@Param("minEmployees") int minEmployees);
}
