package org.example.springdatajpademo.dto;

import org.example.springdatajpademo.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                employeeMapper.toDTOList(project.getEmployees())
        );
    }

    public Project toEntity(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setEmployees(employeeMapper.toEntityList(projectDTO.getEmployees()));
        return project;
    }

    public List<ProjectDTO> toDTOList(List<Project> projects) {
        if (projects == null) {
            return new ArrayList<>();
        }
        return projects.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Project> toEntityList(List<ProjectDTO> projectDTOs) {
        if (projectDTOs == null) {
            return new ArrayList<>();
        }
        return projectDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

