package com.bossj.govyreel.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bossj.govyreel.dtos.ApiResponse;
import com.bossj.govyreel.dtos.CreateProjectRequest;
import com.bossj.govyreel.dtos.ProjectResponse;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.resolvers.CurrentUser;
import com.bossj.govyreel.services.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectList(@CurrentUser User user) {
        log.info("Getting Project List for user {}", user.getId());

        List<ProjectResponse> projectList = projectService.getAllUserProject(user.getId());

        log.info("Found {} projects for user {}", projectList.size(), user.getId());

        ApiResponse<List<ProjectResponse>> response = ApiResponse.<List<ProjectResponse>>builder()
                .data(projectList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable("projectId") String projectId,
            @CurrentUser User user) {

        log.info("Getting project {} for user {}", projectId, user.getId());

        ProjectResponse projectResponse = projectService.getUserProject(user.getId(), UUID.fromString(projectId));

        ApiResponse<ProjectResponse> response = ApiResponse.<ProjectResponse>builder()
                .data(projectResponse)
                .build();

        log.info("Successfully retrieved project {} for user {}", projectId, user.getId());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createProject(@RequestBody @Valid CreateProjectRequest request,
            @CurrentUser User user) {

        log.info("User {} is creating a project with request: {}", user.getId(), request);

        ProjectResponse projectResponse = projectService.createProject(user, request);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .data("Project Created Successfully")
                .build();

        log.info("Project created successfully with ID: {} for user: {}", projectResponse.getId(), user.getId());

        return ResponseEntity.created(URI.create("/api/project/" + projectResponse.getId())).body(response);
    }

}