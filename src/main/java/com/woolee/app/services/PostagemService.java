package com.woolee.app.services;


import com.woolee.app.models.Comentario;
import com.woolee.app.models.Curtidas;
import com.woolee.app.models.Postagem;
import com.woolee.app.models.User;
import com.woolee.app.repositories.ComentarioRepository;
import com.woolee.app.repositories.CurtidasRepository;
import com.woolee.app.repositories.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository repository;

    @Autowired
    private CurtidasRepository curtidasRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    public Postagem insert(Postagem postagem){
        return repository.save(postagem);
    }

    public List<Postagem> findAll(){
        return repository.findAll();
    }

    public List<Postagem> findPostagemByIsDeletedFalse() {
        List<Postagem> posts = repository.findPostagemByIsDeletedFalse();
        posts.forEach( p -> {
            Integer curtidas = curtidasRepository.countAllByPostagem(p);
            p.setCurtidas(curtidas);
        });
        return posts;
    }

    public Postagem findById(Long id){
        return repository.getById(id);
    }
    public String curtiuOrNo(User user) {
        boolean jaCurtiu = curtidasRepository.findCurtidasByUser(user);
        if (jaCurtiu) {
            return "curtiu";
        } else {
            return "";
        }
    }

    public List<Comentario> getComentsByPost(Postagem postagem){
        return comentarioRepository.findAllByPostagem(postagem);
    }

    public void comentarios(Comentario comentario){
        comentarioRepository.save(comentario);
    }

    public void curtir(Long id, User user){
        Postagem postagem = repository.getById(id);
        Curtidas curtidas = new Curtidas();
        Optional<Curtidas> jaCurtiu = curtidasRepository.findByUserAndPostagem(user,postagem);
        if(jaCurtiu.isPresent()){
            curtidasRepository.delete(jaCurtiu.get());
        }else{
            curtidas.setPostagem(postagem);
            curtidas.setUser(user);
            curtidasRepository.save(curtidas);
        }

    }
    public Integer getNumeroCurtidaPost(Postagem postagem){

        return curtidasRepository.countCurtidasByPostagem(postagem);

    }

    public void delete(Long id){
        Postagem p = repository.getById(id);
        if(!p.getIsDeleted()){
            p.setIsDeleted(true);
        }

        repository.save(p);
    }
}
