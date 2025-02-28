package com.StudentMS.serviceImpl;

import com.StudentMS.entity.User;
import com.StudentMS.repository.UserRepo;
import com.StudentMS.service.UserService;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return getUserFromUserDetails(userDetails);
        }

        return null;
    }

    private User getUserFromUserDetails(@NotNull UserDetails userDetails) {
        return userRepo.findByUsername(userDetails.getUsername()).orElse(null);
    }

    @Transactional
    @Override
    public void updatePassword(Long id, String newPassword) {
        userRepo.findById(id).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
        });
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    @Transactional
    public void saveUser(@NotNull User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepo.save(newUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

//    @Override
//    public User getUserByUsername(String username) {
//        return userRepo.findByUsername(username).orElse(null);
//    }

    @Override
    @Transactional
    public User registerUser(String username, String password, String role) {
        if (this.isUsernameExist(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        return saver(username, password, role);
    }

    private @NotNull User saver(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepo.save(user);
    }


    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public boolean isUsernameExist(String username, Long excludeUserId) {
        if (excludeUserId == null) {
            return userRepo.existsByUsername(username);
        } else {
            return userRepo.existsByUsernameAndIdNot(username, excludeUserId);
        }
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    @Transactional
    public void savePassRole(String username, String newPassword, String newRole) {
        entityManager.createQuery(
                        "UPDATE User u SET u.password = :password, u.role = :role WHERE u.username = :username")
                .setParameter("password", passwordEncoder.encode(newPassword))
                .setParameter("role", newRole)
                .setParameter("username", username)
                .executeUpdate();
    }
}