package com.wk.boot.web;

import com.wk.boot.model.User;
import com.wk.boot.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by gaige on 2017/4/10.
 */
public class UserRealm extends AuthorizingRealm {

    private IUserService userService;

    public UserRealm(IUserService userService, CredentialsMatcher credentialsMatcher) {
        this.userService = userService;
        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = token.getPrincipal().toString();

        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        return new SimpleAccount(username, user.getPwd(), user.getNm());
    }
}
