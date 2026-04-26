package com.assignment.BackendIntern.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.BackendIntern.constant.AppConstants;
import com.assignment.BackendIntern.dto.TaskDTO;
import com.assignment.BackendIntern.exception.ResourceNotFoundException;
import com.assignment.BackendIntern.exception.UnauthorizedException;
import com.assignment.BackendIntern.model.Task;
import com.assignment.BackendIntern.model.User;
import com.assignment.BackendIntern.repository.TaskRepository;
import com.assignment.BackendIntern.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;
   
    
    public Task createTask(TaskDTO dto, String email) {
    	log.info("Creating task for user: {}", email);
        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> {
                    log.warn("Task creation failed — user not found: {}", email);
                    return new ResourceNotFoundException(AppConstants.USER_NOT_FOUND);
                });
        
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : AppConstants.STATUS_PENDING);
        task.setUser(user);

        Task saved = taskRepository.save(task);
        log.info("Task created with id: {} for user: {}", saved.getId(), email);

        return saved;
    }

   
    public List<Task> getAllTasks(String email) {
    	log.info("Fetching all tasks for user: {}", email);
        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> {
                    log.warn("Fetch tasks failed — user not found: {}", email);
                    return new ResourceNotFoundException(AppConstants.USER_NOT_FOUND);
                });
        List<Task> tasks = taskRepository.findByUser(user);
        log.info("Found {} tasks for user: {}", tasks.size(), email);

        return tasks;
    }

    
    public Task getTaskById(Long id, String email) {
    	log.info("Fetching task id: {} for user: {}", id, email);

        Task task = taskRepository.findById(id)
        		.orElseThrow(() -> {
                    log.warn("Task not found with id: {}", id);
                    return new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND);
                });

        if (!task.getUser().getEmail().equals(email)) {
            log.warn("Unauthorized access — user: {} tried to access task id: {}", email, id);
            throw new UnauthorizedException(AppConstants.UNAUTHORIZED);
        }
        
        return task;
    }

  
    public Task updateTask(Long id, TaskDTO dto, String email) {
    	log.info("Updating task id: {} for user: {}", id, email);
        Task task = getTaskById(id, email); 

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase()  : AppConstants.STATUS_PENDING);

        Task updated = taskRepository.save(task);
        log.info("Task id: {} updated successfully for user: {}", id, email);

        return updated;
    }

  
    public void deleteTask(Long id, String email) {
    	log.info("Deleting task id: {} for user: {}", id, email);
        Task task = getTaskById(id, email); 
        taskRepository.delete(task);
        log.info("Task id: {} deleted successfully for user: {}", id, email);

    }
}