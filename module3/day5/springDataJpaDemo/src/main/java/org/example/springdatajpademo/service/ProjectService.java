package org.example.springdatajpademo.service;

import org.example.springdatajpademo.exception.ResourceNotFoundException;
import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.model.Project;
import org.example.springdatajpademo.repository.EmployeeRepository;
import org.example.springdatajpademo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Project addProject(Project project){
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getProjectById(Long pid) {
        return projectRepository.findById(pid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + pid));
    }

    public Project updateProject(Long pid, Project projectDetails) {
        Project project = getProjectById(pid);
        project.setName(projectDetails.getName());
        return projectRepository.save(project);
    }

    public String deleteProject(Long pid) {
        Project project = getProjectById(pid);
        projectRepository.delete(project);
        return "Project deleted successfully";
    }

    public String assignEmployeeToProject(Long pid, Long eid) {
        Employee emp = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + eid));
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + pid));

        if (project.getEmployees() == null) {
            project.setEmployees(new ArrayList<>());
        }

        if (!project.getEmployees().contains(emp)) {
            project.getEmployees().add(emp);
            projectRepository.save(project);
        }

        return "Employee assigned to project successfully";
    }

    public String unassignEmployeeFromProject(Long pid, Long eid) {
        Employee emp = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eid));
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + pid));

        if (project.getEmployees() != null) {
            project.getEmployees().remove(emp);
            projectRepository.save(project);
        }

        return "Employee unassigned from project successfully";
    }

    public List<Employee> getEmployeesByProject(Long pid) {
        Project project = getProjectById(pid);
        return project.getEmployees() != null ? project.getEmployees() : new ArrayList<>();
    }

    public List<Employee> getEmployeesByProjectName(String projectName) {
        Project project = projectRepository.findByName(projectName);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with name: " + projectName);
        }
        return project.getEmployees() != null ? project.getEmployees() : new ArrayList<>();
    }

    public List<Project> getProjectsByEmployeeId(Long eid) {
        Employee employee = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eid));
        return employee.getProjects() != null ? employee.getProjects() : new ArrayList<>();
    }
}
