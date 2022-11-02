package com.woolee.app.repositories;

import com.woolee.app.models.Postagem;
import com.woolee.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem,Long> {
    List<Postagem> findPostagemByIsDeletedFalse();

    List<Postagem> findPostagemsByUserId(Long user_id);

    @Query(value = "SELECT p FROM Postagem p where p.user in (?1) and p.isDeleted is false")
    List<Postagem> findPostagemsByUsers(List<User> users);
}
