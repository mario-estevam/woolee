package com.woolee.app.repositories;

import com.woolee.app.models.Conexao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConexaoRepository extends JpaRepository<Conexao, Long> {


}
