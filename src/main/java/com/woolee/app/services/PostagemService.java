package com.woolee.app.services;


import com.woolee.app.dtos.PostagemDTO;
import com.woolee.app.models.*;
import com.woolee.app.repositories.ComentarioRepository;
import com.woolee.app.repositories.CurtidasRepository;
import com.woolee.app.repositories.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository repository;

    @Autowired
    private CurtidasRepository curtidasRepository;

    @Autowired
    private UserService userService;


    @Autowired
    private ComentarioRepository comentarioRepository;

    public Postagem insert(Postagem postagem){
        return repository.save(postagem);
    }

    public List<Postagem> findAll(){
        return repository.findAll();
    }

    public List<PostagemDTO> findPostagemByIsDeletedFalse() {
        List<Postagem> posts = repository.findPostagemByIsDeletedFalse();
        List<PostagemDTO> postsDTO = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        return getPostagemDTOS(postsDTO, user, posts);
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
        return comentarioRepository.findComentariosByIsDeletedIsFalseAndPostagem(postagem);
    }

    public void comentarios(Comentario comentario){
        comentario.setIsDeleted(false);
        comentarioRepository.save(comentario);
    }

    public Comentario findComentarioById(Long id){
        return comentarioRepository.getById(id);
    }

    public void deleteComentario(Long id){
            Comentario c = comentarioRepository.getById(id);
            if(!c.getIsDeleted()){
                c.setIsDeleted(true);
            }
            comentarioRepository.save(c);
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

    public List<PostagemDTO> findPostagemsByUserId(Long idUsuario) {
        List<PostagemDTO> postsDTO = new ArrayList<>();
        User user = userService.findById(idUsuario);
        List<Postagem> posts = repository.findPostagemsByUserId(idUsuario);
        return getPostagemDTOS(postsDTO, user, posts);
    }

    public List<PostagemDTO> findPostsEmAlta(){
        List<Postagem> posts = repository.findPostsEmAlta();
        List<PostagemDTO> postsDTO = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        return getPostagemDTOS(postsDTO, user, posts);
    }

    public List<PostagemDTO> findPostagensByUsers(List<User> users) {
        List<PostagemDTO> postsDTO = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Postagem> posts = repository.findPostagemsByUsers(users);
        return getPostagemDTOS(postsDTO, user, posts);
    }

    private List<PostagemDTO> getPostagemDTOS(List<PostagemDTO> postsDTO, User user, List<Postagem> posts) {
        posts.forEach(p -> {
            PostagemDTO postagemDTO = new PostagemDTO();
            Integer curtidas = curtidasRepository.countAllByPostagem(p);
            p.setCurtidas(curtidas);
            Optional<Curtidas> jaCurtiu = curtidasRepository.findByUserAndPostagem(user, p);
            postagemDTO.setPostagem(p);
            if (jaCurtiu.isPresent()) {
                postagemDTO.setCurtido(true);
            } else {
                postagemDTO.setCurtido(false);
            }
            postsDTO.add(postagemDTO);
        });
        return postsDTO;
    }


}
