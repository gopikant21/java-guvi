package org.example.springdatajpademo.service;

import org.example.springdatajpademo.model.Project;
import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.dto.ProjectDTO;

import java.util.List;

public interface IProjectService {
    Project addProject(Project project);
    List<Project> getAllProjects();
    Project getProjectById(Long pid);
    Project updateProject(Long pid, Project projectDetails);
    String deleteProject(Long pid);
    String assignEmployeeToProject(Long pid, Long eid);
    String unassignEmployeeFromProject(Long pid, Long eid);
    List<Employee> getEmployeesByProject(Long pid);
    List<Employee> getEmployeesByProjectName(String projectName);
    List<Project> getProjectsByEmployeeId(Long eid);
    List<Project> getProjectsByName(String name);
    long updateProjectNameById(Long pid, String newName);
    List<ProjectDTO> getAllProjectsAsDTO();
    ProjectDTO getProjectByIdAsDTO(Long pid);
}

