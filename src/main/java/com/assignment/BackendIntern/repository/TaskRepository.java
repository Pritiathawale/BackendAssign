package com.assignment.BackendIntern.repository;



import com.assignment.BackendIntern.model.Task;
import com.assignment.BackendIntern.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}