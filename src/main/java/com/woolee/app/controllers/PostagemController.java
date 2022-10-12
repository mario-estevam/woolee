package com.woolee.app.controllers;

import com.woolee.app.models.Comentario;
import com.woolee.app.models.Postagem;
import com.woolee.app.models.User;
import com.woolee.app.services.FileStorageService;
import com.woolee.app.services.PostagemService;
import com.woolee.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Random;


@Controller
public class PostagemController {

    @Autowired
    PostagemService service;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    UserService userService;

    public PostagemController(PostagemService service) {
        this.service = service;
    }

    @PostMapping(value = "/postar")
    public String postForUser(Postagem postagem, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
       if(file != null){
           Random random = new Random();
           Double aleatorio = random.nextDouble();
           postagem.setImagemUri(aleatorio + file.getOriginalFilename());
           fileStorageService.save(file, aleatorio);
       }
        postagem.setDataCadastro(new Date());
        postagem.setIsDeleted(false);
        service.insert(postagem);
        return "redirect:/home";
    }

    @RequestMapping("/deletar/post/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        service.delete(id);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/home";
    }

    @GetMapping(value = "/editar/post/{id}")
    public ModelAndView updateUser(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        Postagem postagem = service.findById(id);
        modelAndView.addObject("post", postagem);
        modelAndView.setViewName("atualizar-postagem");
        return modelAndView;
    }

    @PostMapping(value = "/editar/post")
    public ModelAndView editSave(Postagem postagem, @RequestParam(value = "file", required = false) MultipartFile file){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        if(!file.getOriginalFilename().equals("")){
            Random random = new Random();
            Double aleatorio = random.nextDouble();
            postagem.setImagemUri(aleatorio + file.getOriginalFilename());
            fileStorageService.save(file, aleatorio);
        }else{
            Postagem postagem1 = service.findById(postagem.getId());
            postagem.setImagemUri(postagem1.getImagemUri());
        }
        postagem.setDataAtualizacao(new Date());
        postagem.setIsDeleted(false);
        service.insert(postagem);
        modelAndView.addObject("successMessage", "Post atualizado com sucesso");
        modelAndView.addObject("post", new Postagem());
        modelAndView.setViewName("atualizar-postagem");
        return modelAndView;
    }

    @RequestMapping("/curtir/post/{id}")
    public String curtir(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        service.curtir(id,user2);
        return "redirect:/index";
    }

    @GetMapping(value = "/comentarios/post/{id}")
    public ModelAndView comentariosPublicacao(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        Postagem postagem = service.findById(id);
        modelAndView.addObject("post", postagem);
        Comentario comentario = new Comentario();
        comentario.setPostagem(postagem);
        comentario.setUser(user2);
        List<Comentario> comentarios = service.getComentsByPost(postagem);
        modelAndView.addObject("comentario", comentario);
        modelAndView.addObject("comentarios", comentarios);
        modelAndView.setViewName("comentarios-postagem");
        return modelAndView;
    }

    @RequestMapping("/comentar/post")
    public String comentar(Comentario comentario){
        service.comentarios(comentario);
        return "redirect:/comentarios/post/" + comentario.getPostagem().getId().toString();
    }

}
