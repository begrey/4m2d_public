package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.users.CustomUserDetail;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository userRepository;

    public UserDetails loadUserByUsername(String id)
            throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Users user = userRepository.findByIntraId(id);

        if(user == null){
            throw new UsernameNotFoundException(id);
        }
        //userDetail을 default로 사용할때
//        return new User(person.get(0).getId(), person.get(0).getPassword(), "ROLE_USER");

        return new CustomUserDetail(user);
    }
}
