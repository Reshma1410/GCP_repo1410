package org.example.service;


import org.example.common.BaseResponse;
import org.example.constants.ConstantCode;
import org.example.model.Users;
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
       return userRepo.save(user);
    }

    public BaseResponse verify(Users user) {
        try{
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

            BaseResponse response=new BaseResponse();

            if(authentication.isAuthenticated()){
                response.setCode(ConstantCode.SUCCESS);
                response.setData(Map.of("Token",jwtService.generateToken(user.getUsername())));
                response.setMessage("JWT token generated");
                return response;
            }else{
                response.setCode(ConstantCode.TOKEN_EXPIRED);
                response.setData(null);
                response.setMessage("JWT token unsuccessful");
                return response;
            }



        } catch (Exception e) {
            return new BaseResponse(
                    ConstantCode.LOGIN_FAILED,
                    e.getMessage(),
                    null
            );
        }



    }
}
