package com.woolee.app.services;

import com.woolee.app.models.Conexao;
import com.woolee.app.models.User;
import com.woolee.app.repositories.ConexaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConexaoService {
    @Autowired
    private ConexaoRepository conexaoRepository;

    public void inserir(Conexao conexao){
        conexaoRepository.save(conexao);
    }

    public List<Conexao> findConexaosByDestinatarioAndSituacaoAndDeletedAtIsNull(User destinarario, Boolean situacao){
        return conexaoRepository.findConexaosByDestinatarioAndSituacaoAndDeletedAtIsNull(destinarario, situacao);
    }

    public Conexao findConexaoById(Long id){
        return conexaoRepository.findConexaoById(id);
    }

    public Conexao findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(User remetente, User destinatario){
        return conexaoRepository.findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(remetente, destinatario);
    }

    public void delete(Long id){
        Conexao conexao = findConexaoById(id);
        conexao.setDeletedAt(new Date());
        conexaoRepository.save(conexao);
    }
}
