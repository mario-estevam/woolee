package com.woolee.app.repositories;

import com.woolee.app.models.Comentario;
import com.woolee.app.models.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findAllByPostagem(Postagem postagem);
}
