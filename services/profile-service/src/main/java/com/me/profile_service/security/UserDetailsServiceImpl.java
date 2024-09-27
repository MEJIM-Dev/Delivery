package com.me.profile_service.security;

import com.me.profile_service.model.AppUser;
import com.me.profile_service.repository.AppUserRepository;
import com.me.profile_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> optionalAppUser = appUserRepository.findByLogin(username);

        if(optionalAppUser.isEmpty()){
            log.error("[+] Error: Invalid Username: {}", username);
            throw new UsernameNotFoundException("Invalid Username or Password");
        }

        AppUser appUser = optionalAppUser.get();
        User user = new User(
                appUser.getLogin(),
                appUser.getPassword(),
                UserUtil.isAccountEnabled(appUser.getStatus()),
                UserUtil.isAccountNonExpired(appUser.getStatus()),
                UserUtil.isCredentialsNonExpired(appUser.getStatus()),
                UserUtil.isAccountNonLocked(appUser.getStatus()),
                UserUtil.getUserRoles(appUser)
        );
        return user;
    }
}
