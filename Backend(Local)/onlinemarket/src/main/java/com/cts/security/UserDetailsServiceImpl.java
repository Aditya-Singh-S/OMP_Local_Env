////package com.cts.security;
////
////import com.cts.entity.User;
////import com.cts.exception.UserNotFoundException;
////import com.cts.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
////
////import java.util.ArrayList;
////import java.util.List;
////
////@Service
////public class UserDetailsServiceImpl implements UserDetailsService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Override
////    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
////        User user = userRepository.findByEmail(email);
////        if (user == null) {
////            throw new UserNotFoundException("User not found with email: " + email);
////        }
////
////        List<GrantedAuthority> authorities = new ArrayList<>();
////        authorities.add(new SimpleGrantedAuthority(user.getUserRole().name())); 
////        return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getPassword(),
////                authorities
////        );
////    }
////}
//
////package com.cts.security;
////
////import com.cts.entity.User;
////import com.cts.exception.UserNotFoundException;
////import com.cts.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
////
////import java.util.ArrayList;
////import java.util.List;
////
////@Service
////public class UserDetailsServiceImpl implements UserDetailsService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Override
////    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
////        User user = userRepository.findByEmail(email);
////        if (user == null) {
////            throw new UserNotFoundException("User not found with email: " + email);
////        }
////
////        List<GrantedAuthority> authorities = new ArrayList<>();
////        authorities.add(new SimpleGrantedAuthority(user.getUserRole().name())); 
////        System.out.println(user.getUserRole().name());
////
////        return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getPassword(),
////                authorities
////        );
////    }
////}
//
//
package com.cts.security;

import com.cts.entity.User;
import com.cts.exception.UserNotFoundException;
import com.cts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email); 
        
     if (user == null)
     {
         throw new UserNotFoundException("User not found with email: " + email);
     }
        
        return new AppUser(user);
    }
}