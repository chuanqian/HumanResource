package com.zaqacu.service.impl;

import com.zaqacu.dao.UserLoginMapper;
import com.zaqacu.entity.UserLogin;
import com.zaqacu.service.LoginAndOutService;
import com.zaqacu.service.PermissionsService;
import com.zaqacu.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginAndOutServiceImpl implements LoginAndOutService {
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PermissionsService permissionsService;


    @Override
    public boolean getOneUserLoginByUserName(String userName) {
        return userLoginMapper.selectByUserName(userName) != null ? false : true;
    }

    @Override
    public boolean addUserLogin(UserLogin userLogin) {
        return userLoginMapper.insertSelective(userLogin) != 1 ? false : true;
    }

    @Override
    public boolean deleteByStaffUidList(List<String> staffUidList) {
        return userLoginMapper.deleteBatch(staffUidList) != 0 ? true : false;
    }

    @Override
    public int getByStaffUid(String staffUid) {
        return userLoginMapper.selectByStaffUid(staffUid);
    }

}
