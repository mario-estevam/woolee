package com.woolee.app.repositories;

import com.woolee.app.models.Curtidas;
import com.woolee.app.models.Postagem;
import com.woolee.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurtidasRepository extends JpaRepository<Curtidas, Long> {
    Integer countAllByPostagem(Postagem postagem);
    Optional<Curtidas> findByUserAndPostagem(User user, Postagem postagem);
    Integer countCurtidasByPostagem(Postagem postagem);
    boolean findCurtidasByUser(User user);
}
