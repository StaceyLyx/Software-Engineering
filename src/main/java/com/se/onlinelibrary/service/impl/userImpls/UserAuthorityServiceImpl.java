package com.se.onlinelibrary.service.impl.userImpls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.user.UserAuthority;
import com.se.onlinelibrary.mapper.UserAuthorityMapper;
import com.se.onlinelibrary.service.userServices.UserAuthorityService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority> implements UserAuthorityService {

}
