package com.woolee.app.services;

import com.woolee.app.models.Conexao;
import com.woolee.app.repositories.ConexaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConexaoService {
    @Autowired
    private ConexaoRepository conexaoRepository;

    public Conexao inserir(Conexao conexao){
        return conexaoRepository.save(conexao);
    }

}
