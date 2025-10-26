package com.bossj.govyreel.services;

import java.util.List;
import java.util.UUID;

import com.bossj.govyreel.dtos.CreateProjectRequest;
import com.bossj.govyreel.dtos.ProjectResponse;
import com.bossj.govyreel.entities.User;

public interface ProjectService {
    List<ProjectResponse> getAllUserProject(UUID user);
    ProjectResponse getUserProject(UUID userId, UUID projectId);
    ProjectResponse createProject(User user, CreateProjectRequest request);
    
}
