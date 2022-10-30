package com.woolee.app.controllers;

import com.woolee.app.models.Conexao;
import com.woolee.app.models.User;
import com.woolee.app.repositories.RoleRepository;
import com.woolee.app.services.ConexaoService;
import com.woolee.app.services.PostagemService;
import com.woolee.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class ConexaoController {

    @Autowired
    private UserService userService;

    @Autowired
    ConexaoService conexaoService;

    @RequestMapping("/solicitar/conexao/{id}")
    public String requestUserConection(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        Conexao conexao = new Conexao();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userFrom = userService.findUserByUserName(auth.getName());
        User userTo = userService.findById(id);
        conexao.setDestinatario(userTo);
        conexao.setRemetente(userFrom);
        conexao.setSituacao(false);
        conexaoService.inserir(conexao);
        redirectAttributes.addAttribute("msg", "Solicitação realizada!");
        return "redirect:/home";
    }

    @RequestMapping("/confirmar/conexao/{id}")
    public String confirmUserConection(@PathVariable (name = "id") Long id, RedirectAttributes redirectAttributes){
        Conexao conexao = conexaoService.findConexaoById(id);
        conexao.setSituacao(true);
        conexao.setDataConexao(new Date());
        conexaoService.inserir(conexao);
        redirectAttributes.addAttribute("msg", "Solicitação aceita!");
        return "redirect:/conexoes";
    }

    @GetMapping(value={"/conexoes"})
    public ModelAndView conexoes(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView();

        List<Conexao> conexoes = conexaoService.findConexaosByDestinatarioAndSituacao(user, false);
        modelAndView.addObject("requests", conexoes);
        modelAndView.addObject("usuario2",user);
        modelAndView.setViewName("conexoes");
        return modelAndView;
    }

    @RequestMapping("/excluir/conexao/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        conexaoService.delete(id);
        redirectAttributes.addAttribute("msg", "Conexao excluida!");
        return "redirect:/home";
    }
}
