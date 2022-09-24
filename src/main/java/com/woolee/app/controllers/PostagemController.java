package com.woolee.app.controllers;

import com.woolee.app.models.Postagem;
import com.woolee.app.services.FileStorageService;
import com.woolee.app.services.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;


@Controller
public class PostagemController {

    @Autowired
    PostagemService service;

    @Autowired
    FileStorageService fileStorageService;

    public PostagemController(PostagemService service) {
        this.service = service;
    }

    @PostMapping(value = "/postar")
    public String postForUser(Postagem postagem, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Random random = new Random();
        Double aleatorio = random.nextDouble();
        postagem.setImagemUri(aleatorio + file.getOriginalFilename());
        fileStorageService.save(file, aleatorio);
        service.insert(postagem);
        return "redirect:/home";
    }
}
