package com.woolee.app.services;

import com.woolee.app.models.Conexao;
import com.woolee.app.models.User;
import com.woolee.app.repositories.ConexaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConexaoService {
    @Autowired
    private ConexaoRepository conexaoRepository;

    public void inserir(Conexao conexao){
        conexaoRepository.save(conexao);
    }

    public List<Conexao> findConexaosByDestinatarioAndSituacao(User destinarario, Boolean situacao){
        return conexaoRepository.findConexaosByDestinatarioAndSituacao(destinarario, situacao);
    }

    public Conexao findConexaoById(Long id){
        return conexaoRepository.findConexaoById(id);
    }

    public Conexao findConexaoByRemetenteAndDestinatario(User remetente, User destinatario){
        return conexaoRepository.findConexaoByRemetenteAndDestinatario(remetente, destinatario);
    }

    public void delete(Long id){
        conexaoRepository.deleteById(id);
    }
}
