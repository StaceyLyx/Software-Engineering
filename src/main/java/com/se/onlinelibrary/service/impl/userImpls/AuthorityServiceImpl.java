package com.se.onlinelibrary.service.impl.userImpls;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.mapper.AuthorityMapper;
import com.se.onlinelibrary.service.userServices.AuthorityService;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {

}
