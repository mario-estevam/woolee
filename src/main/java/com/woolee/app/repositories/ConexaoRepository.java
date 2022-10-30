package com.woolee.app.repositories;

import com.woolee.app.models.Conexao;
import com.woolee.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConexaoRepository extends JpaRepository<Conexao, Long> {

    List<Conexao> findConexaosByDestinatarioAndSituacao(User destinatario, Boolean situacao);

    Conexao findConexaoByRemetenteAndDestinatario(User remetente, User destinatario);

    Conexao findConexaoById(Long id);



}
