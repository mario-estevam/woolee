package com.woolee.app.repositories;

import com.woolee.app.models.Conexao;
import com.woolee.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConexaoRepository extends JpaRepository<Conexao, Long> {

    List<Conexao> findConexaosByDestinatarioAndSituacaoAndDeletedAtIsNull(User destinatario, Boolean situacao);

    Conexao findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(User remetente, User destinatario);

    Conexao findConexaoById(Long id);

    @Query(value = "SELECT c.remetente FROM Conexao c WHERE c.destinatario = ?1 and c.deletedAt is null and c.situacao=true")
    List<User> findConexaosByDestinatarioAndDeletedAtIsNullAndSituacaoTrue(User user);

    @Query(value = "SELECT c.destinatario FROM Conexao c WHERE c.remetente = ?1 and c.deletedAt is null and c.situacao=true")
    List<User> findConexaosByRemetenteAndDeletedAtIsNullSituacaoTrue(User user);
}
