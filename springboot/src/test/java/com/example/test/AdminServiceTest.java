package com.example.test;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.common.enums.ResultCodeEnum;
import com.example.service.AdminService;
import com.example.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    @Autowired
    private AdminService adminService;

    private Admin admin;
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("password");

        account = new Account();
        account.setUsername("admin");
        account.setPassword("password");
        account.setNewPassword("newPassword");
    }

    @Test
    public void testAddAdminWhenAdminDoesNotExist() {
        when(adminMapper.selectByUsername(any())).thenReturn(null);

        adminService.add(admin);

        verify(adminMapper, times(1)).insert(any(Admin.class));
    }

    @Test
    public void testAddAdminWhenAdminExists() {
        when(adminMapper.selectByUsername(any())).thenReturn(admin);

        CustomException exception = null;
        try {
            adminService.add(admin);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_EXIST_ERROR;
    }

    @Test
    public void testDeleteById() {
        doNothing().when(adminMapper).deleteById(anyInt());

        adminService.deleteById(1);

        verify(adminMapper, times(1)).deleteById(1);
    }

    @Test
    public void testUpdateById() {
        when(adminMapper.updateById(any(Admin.class))).thenReturn(1);

        adminService.updateById(admin);

        verify(adminMapper, times(1)).updateById(any(Admin.class));
    }

    @Test
    public void testLoginWhenUserExistsAndPasswordCorrect() {
        when(adminMapper.selectByUsername(any())).thenReturn(admin);

        Account loggedInAccount = adminService.login(account);

        assert loggedInAccount.getUsername().equals("admin");
        assert loggedInAccount.getToken() != null;
    }

    @Test
    public void testLoginWhenUserDoesNotExist() {
        when(adminMapper.selectByUsername(any())).thenReturn(null);

        CustomException exception = null;
        try {
            adminService.login(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_NOT_EXIST_ERROR;
    }

    @Test
    public void testLoginWhenPasswordIncorrect() {
        admin.setPassword("wrongPassword");
        when(adminMapper.selectByUsername(any())).thenReturn(admin);

        CustomException exception = null;
        try {
            adminService.login(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_ACCOUNT_ERROR;
    }

    @Test
    public void testRegister() {
        when(adminMapper.selectByUsername(any())).thenReturn(null);

        adminService.register(account);

        verify(adminMapper, times(1)).insert(any(Admin.class));
    }

    @Test
    public void testUpdatePassword() {
        when(adminMapper.selectByUsername(any())).thenReturn(admin);
        when(adminMapper.updateById(any(Admin.class))).thenReturn(1);

        adminService.updatePassword(account);

        verify(adminMapper, times(1)).updateById(any(Admin.class));
    }

    @Test
    public void testUpdatePasswordWhenUserNotExist() {
        when(adminMapper.selectByUsername(any())).thenReturn(null);

        CustomException exception = null;
        try {
            adminService.updatePassword(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_NOT_EXIST_ERROR;
    }

    @Test
    public void testUpdatePasswordWhenPasswordIncorrect() {
        admin.setPassword("wrongPassword");
        when(adminMapper.selectByUsername(any())).thenReturn(admin);

        CustomException exception = null;
        try {
            adminService.updatePassword(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.PARAM_PASSWORD_ERROR;
    }
}
