package com.woolee.app.repositories;


import com.woolee.app.models.Temas;
import com.woolee.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    List<User> findAllByTemas(Temas temas);
}
