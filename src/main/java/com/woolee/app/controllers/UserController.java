package com.woolee.app.controllers;


import com.woolee.app.dtos.PostagemDTO;
import com.woolee.app.models.*;
import com.woolee.app.repositories.RoleRepository;
import com.woolee.app.services.ConexaoService;
import com.woolee.app.services.PostagemService;
import com.woolee.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostagemService postagemService;

    @Autowired
    ConexaoService conexaoService;


    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value={"/index"})
    public String index(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
          if(Objects.isNull(user)){
              return "redirect:/login";
          }

      return "redirect:/home";

    }

    @GetMapping(value={"/home"})
    public ModelAndView home(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView("home");
        Postagem postagem = new Postagem();
        List<User> users = userService.findUsersConnectedByUser(user);
        users.add(user);
        List<PostagemDTO> posts = postagemService.findPostagensByUsers(users);
        postagem.setUser(user);
        modelAndView.addObject("usuario2",user);
        modelAndView.addObject("posts",posts);
        modelAndView.addObject("post", postagem);
        return modelAndView;
    }

    @GetMapping(value = {"/explorar"})
    public ModelAndView explorar(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Temas> temas = user.getTemas();
        ModelAndView modelAndView = new ModelAndView("explorar");
        Set<User> uniqueUsers = new HashSet<>();
        List<User> users = new ArrayList<>();
        temas.forEach(tema -> {
            uniqueUsers.addAll(userService.findUsersByTema(tema));
        });
        uniqueUsers.forEach(u -> {
            users.add(u);
        });
        List<PostagemDTO> posts = postagemService.findPostagensByUsers(users);
        modelAndView.addObject("usuario2", user);
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }

    @GetMapping(value = {"/em-alta"})
    public ModelAndView emAlta(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView("alta");
        List<PostagemDTO> posts = postagemService.findPostsEmAlta();
        modelAndView.addObject("usuario2", user);
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }

    @GetMapping(value = "/visualizar/usuario/{id}")
    public ModelAndView viewUserPage(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        User user = userService.findById(id);
        List<PostagemDTO> posts = postagemService.findPostagemsByUserId(id);
        Conexao conexao = conexaoService.findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(user2, user);
        if(conexao == null){
            conexao = conexaoService.findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(user,user2);
        }
        modelAndView.addObject("usuario2",user2);
        modelAndView.addObject("usuario",user);
        modelAndView.addObject("posts",posts);
        modelAndView.addObject("conexao", conexao);
        modelAndView.setViewName("visualizar-usuario");
        return modelAndView;
    }

    @GetMapping(value = "/visualizar-perfil")
    public ModelAndView visualizarPerfil(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        List<PostagemDTO> posts = postagemService.findPostagemsByUserId(user2.getId());
        Conexao conexao = conexaoService.findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(user2, user2);
        if(conexao == null){
            conexao = conexaoService.findConexaoByRemetenteAndDestinatarioAndDeletedAtIsNull(user2,user2);
        }
        modelAndView.addObject("usuario2",user2);
        modelAndView.addObject("usuario",user2);
        modelAndView.addObject("posts",posts);
        modelAndView.addObject("conexao", conexao);
        modelAndView.setViewName("perfil");
        return modelAndView;
    }

    @GetMapping(value={"/voltar"})
    public String voltar(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        return "redirect:/login";
    }


    @GetMapping(value="/cadastro/usuario")
    public ModelAndView createUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("usuario", user);
        modelAndView.setViewName("cadastro-usuario");
        return modelAndView;
    }

    @PostMapping(value = "/cadastro/usuario")
    public ModelAndView postForUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);

            Boolean confirm = userService.confirmarSenha(user.getSenha(),user.getRepetirSenha());
            Boolean emailConfirm = userService.findUserByEmail(user.getEmail());
            Boolean userNameConfirm = userService.findUserUsernameBoolean(user.getUserName());
            if(Boolean.FALSE.equals(confirm)){
                modelAndView.addObject("senhas","as senhas não coincidem");
                modelAndView.addObject("usuario", user);
                modelAndView.setViewName("cadastro-usuario");
            } else if(Boolean.FALSE.equals(userNameConfirm)){
                modelAndView.addObject("userName","Este nome de usuário já existe");
                modelAndView.addObject("usuario", user);
                modelAndView.setViewName("cadastro-usuario");
            } else if(Boolean.FALSE.equals(emailConfirm)){
                modelAndView.addObject("email","Este email já foi cadastrado");
                modelAndView.addObject("usuario", user);
                modelAndView.setViewName("cadastro-usuario");
            } else {
                userService.saveUser(user);
                modelAndView.addObject("successMessage", "Usuario cadastrado com sucesso");
                modelAndView.addObject("usuario", new User());
                modelAndView.setViewName("cadastro-usuario");
            }

        return modelAndView;
    }

    @GetMapping(value = "/editar/usuario")
    public ModelAndView updateUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        modelAndView.setViewName("atualizar-usuario");
        return modelAndView;
    }

    @PostMapping(value = "/editar/usuario")
    public ModelAndView editSave(User user){

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());

        modelAndView.addObject("usuario2", user2);
        Boolean confirm = userService.confirmarSenha(user.getSenha(),user.getRepetirSenha());

        if(Boolean.FALSE.equals(confirm)){
            modelAndView.addObject("senhas","as senhas não coincidem");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("atualizar-usuario");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Usuario atualizado com sucesso");
            modelAndView.addObject("usuario", new User());
            modelAndView.setViewName("atualizar-usuario");
        }

        return modelAndView;
    }

    @RequestMapping("/deletar/usuario/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        userService.deleteUser(id);
        redirectAttributes.addAttribute("msg", "Usuário desativado com sucesso!");
        return "redirect:/login";
    }

}
