package com.woolee.app.services;


import com.woolee.app.models.Role;
import com.woolee.app.models.User;
import com.woolee.app.repositories.RoleRepository;
import com.woolee.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Boolean findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user ==null){
            return true;
        }else{
            return false;
        }
    }

    public User findById(Long id){
        return userRepository.getById(id);
    }

    public Boolean findUserUsernameBoolean(String userName){
        User user = userRepository.findByUserName(userName);
        if(user ==null){
            return true;
        }else{
            return false;
        }
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Boolean confirmarSenha(String senha, String repetirSenha){
        if(senha.equals(repetirSenha)){
            return true;
        }else{
            return false;
        }
    }

    public User saveUser(User user) {
        user.setSenha(bCryptPasswordEncoder.encode(user.getSenha()));
        user.setRepetirSenha(bCryptPasswordEncoder.encode(user.getRepetirSenha()));
        user.getTemas();
        Role userRole = roleRepository.findByRole("COMUM");
        user.setActive(true);
        user.setRoles(new HashSet<Role>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }
}
