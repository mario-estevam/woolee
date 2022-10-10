package com.woolee.app.services;


import com.woolee.app.models.Curtidas;
import com.woolee.app.models.Postagem;
import com.woolee.app.models.User;
import com.woolee.app.repositories.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository repository;

    public Postagem insert(Postagem postagem){
        return repository.save(postagem);
    }

    public List<Postagem> findAll(){
        return repository.findAll();
    }

    public List<Postagem> findPostagemByIsDeletedFalse() {return repository.findPostagemByIsDeletedFalse(); }

    public Postagem findById(Long id){
        return repository.getById(id);
    }

    public void curtir(Long id, User user){
        Postagem postagem = repository.getById(id);
        Curtidas curtidas = new Curtidas();
        curtidas.setPostagem(postagem);
        curtidas.setUser(user);
    }

    public void delete(Long id){
        Postagem p = repository.getById(id);
        if(!p.getIsDeleted()){
            p.setIsDeleted(true);
        }

        repository.save(p);
    }
}
