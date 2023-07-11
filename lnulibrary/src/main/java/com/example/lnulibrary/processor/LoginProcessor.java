package com.example.lnulibrary.processor;

import com.example.lnulibrary.Member;
import com.example.lnulibrary.repositories.MemberRepository;

public class LoginProcessor {
    public Member login(String personalNumber, String password) throws Exception {
        Member member = MemberRepository.find(personalNumber);
        if(member == null)
            return null;

        String hashedPassword = SecurityHelper.computeHash(password,member.getSalt());
        if(!member.getPassword().equals(hashedPassword)){
            return null;
        }

        return member;
    }
}
