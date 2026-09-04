package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventPublisherService eventPublisher;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, User userDetails) {
        User user = findById(id);
        user.setEmail(userDetails.getEmail());
        user.setRoles(userDetails.getRoles());
        User saved = userRepository.save(user);
        eventPublisher.publishUserUpdated(saved);
        return saved;
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.deleteById(id);
        eventPublisher.publishUserDeleted(id);
    }
}
