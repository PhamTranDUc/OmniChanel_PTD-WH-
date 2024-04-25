package com.example.omnichannelfinal.service;

import com.example.omnichannelfinal.component.JwtTokenUtil;
import com.example.omnichannelfinal.dto.UserDto;
import com.example.omnichannelfinal.entity.Role;
import com.example.omnichannelfinal.entity.User;
import com.example.omnichannelfinal.repository.RoleRepository;
import com.example.omnichannelfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements  IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Override
    public void createUser(UserDto userDto) throws Exception{
        User newUser= User.builder().userName(userDto.getUserName())
                .fullName(userDto.getFullName())
                .build();
        Role role= roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new RuntimeException("Not found Role with role_id= "+userDto.getRoleId()));
        HashSet<Role> roles= new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);
            String passWordEncode= bCryptPasswordEncoder.encode(userDto.getPassword());
            newUser.setPassword(passWordEncode);
        userRepository.save(newUser);
    }

    @Override
    public ResponseEntity<?> login(String userName, String passWord) throws Exception{
        Optional<User> optionalUser= userRepository.findByUserName(userName);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Username of password invalid !!!");
        }
        User userExist=optionalUser.get();
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userName,passWord,userExist.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        Map<String,String> token = new HashMap<>();
        token.put("token",jwtTokenUtil.generateToken(userExist));
        return ResponseEntity.ok().body(token);
    }

    @Override
    public String getRoleByToken(String token) {
        token= token.substring(7);
        return jwtTokenUtil.extractRoleName(token);
    }

    @Override
    public User getUserByToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new RuntimeException("token is Expired");
        }
        String userName= jwtTokenUtil.extractUsername(token);
        Optional<User> user= userRepository.findByUserName(userName);
        if(user.isPresent()){
            return user.get();
        }
        throw new RuntimeException("Not Found User with UserName = "+userName);
    }

    @Override
    public List<User> getAllAccount() {
        return userRepository.findAll();
    }


    @Override
    public User updatePassword(String newPassword,String token) {
        User user= this.getUserByToken(token);
        String newPasswordBcrypt= bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPasswordBcrypt);
        userRepository.save(user);
        return user;
    }
}