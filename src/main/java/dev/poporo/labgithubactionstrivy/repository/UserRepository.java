package dev.poporo.labgithubactionstrivy.repository;

import dev.poporo.labgithubactionstrivy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
