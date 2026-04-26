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

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;
   
    
    public Task createTask(TaskDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : AppConstants.STATUS_PENDING);
        task.setUser(user);

        return taskRepository.save(task);
    }

   
    public List<Task> getAllTasks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        return taskRepository.findByUser(user);
    }

    
    public Task getTaskById(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND));

        if (!task.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException(AppConstants.UNAUTHORIZED);
        }
        return task;
    }

  
    public Task updateTask(Long id, TaskDTO dto, String email) {
        Task task = getTaskById(id, email); 

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase()  : AppConstants.STATUS_PENDING);

        return taskRepository.save(task);
    }

  
    public void deleteTask(Long id, String email) {
        Task task = getTaskById(id, email); 
        taskRepository.delete(task);
    }
}