package org.example.springdatajpademo.controller;

import org.example.springdatajpademo.dto.ProjectDTO;
import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.model.Project;
import org.example.springdatajpademo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long pid) {
        return new ResponseEntity<>(projectService.getProjectById(pid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        return new ResponseEntity<>(projectService.addProject(project), HttpStatus.CREATED);
    }

    @PutMapping("/{pid}")
    public ResponseEntity<Project> updateProject(@PathVariable Long pid, @RequestBody Project projectDetails) {
        return new ResponseEntity<>(projectService.updateProject(pid, projectDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<String> deleteProject(@PathVariable Long pid) {
        return new ResponseEntity<>(projectService.deleteProject(pid), HttpStatus.OK);
    }

    @PostMapping("/assign/{pid}/{eid}")
    public ResponseEntity<String> assignEmployeeToProject(@PathVariable Long pid, @PathVariable Long eid) {
        return new ResponseEntity<>(projectService.assignEmployeeToProject(pid, eid), HttpStatus.OK);
    }

    @PostMapping("/unassign/{pid}/{eid}")
    public ResponseEntity<String> unassignEmployeeFromProject(@PathVariable Long pid, @PathVariable Long eid) {
        return new ResponseEntity<>(projectService.unassignEmployeeFromProject(pid, eid), HttpStatus.OK);
    }

    @GetMapping("/{pid}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByProject(@PathVariable Long pid) {
        return new ResponseEntity<>(projectService.getEmployeesByProject(pid), HttpStatus.OK);
    }

    @GetMapping("/employees/by-project-name/{projectName}")
    public ResponseEntity<List<Employee>> getEmployeesByProjectName(@PathVariable String projectName) {
        return new ResponseEntity<>(projectService.getEmployeesByProjectName(projectName), HttpStatus.OK);
    }

    @GetMapping("/by-employee/{eid}")
    public ResponseEntity<List<Project>> getProjectsByEmployeeId(@PathVariable Long eid) {
        return new ResponseEntity<>(projectService.getProjectsByEmployeeId(eid), HttpStatus.OK);
    }

    // New custom query APIs
    @GetMapping("/search/by-name/{name}")
    public ResponseEntity<List<Project>> getProjectsByName(@PathVariable String name) {
        return new ResponseEntity<>(projectService.getProjectsByName(name), HttpStatus.OK);
    }

    // Update project name using custom query
    @PutMapping("/{pid}/name")
    public ResponseEntity<String> updateProjectName(@PathVariable Long pid, @RequestParam String name) {
        long result = projectService.updateProjectNameById(pid, name);
        if (result > 0) {
            return new ResponseEntity<>("Project name updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
    }

    // Get all projects as DTO
    @GetMapping("/dto/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsAsDTO() {
        return new ResponseEntity<>(projectService.getAllProjectsAsDTO(), HttpStatus.OK);
    }

    // Get project by id as DTO
    @GetMapping("/dto/{pid}")
    public ResponseEntity<ProjectDTO> getProjectByIdAsDTO(@PathVariable Long pid) {
        return new ResponseEntity<>(projectService.getProjectByIdAsDTO(pid), HttpStatus.OK);
    }
}
