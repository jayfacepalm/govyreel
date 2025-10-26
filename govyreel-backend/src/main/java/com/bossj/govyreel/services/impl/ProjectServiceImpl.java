package com.bossj.govyreel.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bossj.govyreel.dtos.CreateProjectRequest;
import com.bossj.govyreel.dtos.ProjectResponse;
import com.bossj.govyreel.entities.Project;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.exceptions.AssetNotFoundException;
import com.bossj.govyreel.repositories.ProjectRepository;
import com.bossj.govyreel.services.ProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectResponse> getAllUserProject(UUID userId) {
        log.info("Fetching all projects for user with ID: {}", userId);
        List<Project> projects = projectRepository.findAllByUserIdOrderByLastModifiedDateDesc(userId);
        log.debug("Found {} projects in the repository for user ID: {}", projects.size(), userId);

        List<ProjectResponse> projectList = projects.stream()
                .map(project -> {
                    log.trace("Mapping project with ID: {} to ProjectResponse", project.getId());
                    return new ProjectResponse(
                            project.getId().toString(),
                            project.getName(),
                            project.getDescription(),
                            project.getCreatedDate(),
                            project.getLastModifiedDate());
                })
                .toList();

        log.info("Successfully fetched and mapped {} projects for user with ID: {}", projectList.size(), userId);
        return projectList;
    }

    @Override
    public ProjectResponse getUserProject(UUID userId, UUID projectId) {
        log.info("Fetching project with ID {} for user with ID: {}", projectId, userId);
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> {
                    log.warn("Project with ID {} not found for user with ID {}", projectId, userId);
                    return new AssetNotFoundException("Project not found");
                });

        ProjectResponse projectResponse = new ProjectResponse(project.getId().toString(), project.getName(),
                project.getDescription(), project.getCreatedDate(), project.getLastModifiedDate());

        log.info("Successfully fetched project with ID {} for user with ID: {}", projectId, userId);
        return projectResponse;

    }

    @Override
    public ProjectResponse createProject(User user, CreateProjectRequest request) {
        log.info("Creating project for user with ID: {}", user.getId());
        log.debug("CreateProjectRequest: {}", request);
        Project newProject = new Project();
        newProject.setName(request.getName());
        newProject.setDescription(request.getDescription());
        newProject.setUser(user);

        Project savedProject = projectRepository.save(newProject);
        log.info("Saved new project with ID: {}", savedProject.getId());

        ProjectResponse projectResponse = new ProjectResponse(savedProject.getId().toString(), savedProject.getName(),
                savedProject.getDescription(), savedProject.getCreatedDate(), savedProject.getLastModifiedDate());

        log.info("Successfully created project with ID {} for user with ID: {}", savedProject.getId(), user.getId());
        return projectResponse;

    }

}
