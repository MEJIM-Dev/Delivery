package com.me.profile_service.util;

import com.me.profile_service.model.AppUser;
import com.me.profile_service.model.enumeration.Status;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserUtil {

    public static boolean isAccountNonLocked(Status status){
        return !Status.DISABLED.equals(status) && !Status.DELETED.equals(status);
    }

    public static boolean isAccountEnabled(Status status){
        return Status.ACTIVE.equals(status);
    }

    public static boolean isCredentialsNonExpired(Status status){
        return !Status.DISABLED.equals(status);
    }

    public static boolean isAccountNonExpired(Status status){
        return !Status.DELETED.equals(status);
    }

    public static List<SimpleGrantedAuthority> getUserRoles (AppUser appUser) {
        return appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public static Map<String, Object> getExtraClaims(AppUser user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("sex", user.getSex());
        extraClaims.put("firstName", user.getFirstname());
        extraClaims.put("lastName", user.getLastname());
        extraClaims.put("otherNames", user.getOtherNames());
        extraClaims.put("login", user.getLogin());
        extraClaims.put("id", user.getId());
        return extraClaims;
    }
}
