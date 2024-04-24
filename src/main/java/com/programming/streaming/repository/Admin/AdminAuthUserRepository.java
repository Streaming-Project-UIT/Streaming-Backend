package com.programming.streaming.repository.Admin;
import com.programming.streaming.entity.AdminAuthUser;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
@Repository
public interface AdminAuthUserRepository extends MongoRepository<AdminAuthUser, String> {
    Optional<AdminAuthUser> findByUsername(String username);
} 