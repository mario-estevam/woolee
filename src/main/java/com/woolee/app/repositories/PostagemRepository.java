package com.woolee.app.repositories;

import com.woolee.app.models.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem,Long> {
    List<Postagem> findPostagemByIsDeletedFalse();

    List<Postagem> findPostagemsByUserId(Long user_id);
}
