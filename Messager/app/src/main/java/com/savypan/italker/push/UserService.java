package com.savypan.italker.push;

public class UserService implements IUserService {

    @Override
    public String search(int hash) {
        return "User: " + hash;
    }
}
