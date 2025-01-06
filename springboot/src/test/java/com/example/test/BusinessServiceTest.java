package com.example.test;

import com.example.entity.Account;
import com.example.entity.Business;
import com.example.exception.CustomException;
import com.example.mapper.BusinessMapper;
import com.example.common.enums.ResultCodeEnum;
import com.example.service.BusinessService;
import com.example.utils.TokenUtils;
import com.example.common.enums.StatusEnum;
import com.example.common.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BusinessServiceTest {

    @Mock
    private BusinessMapper businessMapper;

    @InjectMocks
    private BusinessService businessService;

    private Business business;
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        business = new Business();
        business.setId(1);
        business.setUsername("business1");
        business.setPassword("password");
        business.setName("Business One");
        business.setStatus(StatusEnum.CHECKING.status);

        account = new Account();
        account.setUsername("business1");
        account.setPassword("password");
        account.setNewPassword("newPassword");
    }

    @Test
    public void testAddBusinessWhenBusinessDoesNotExist() {
        when(businessMapper.selectByUsername(any())).thenReturn(null);

        businessService.add(business);

        verify(businessMapper, times(1)).insert(any(Business.class));
    }

    @Test
    public void testAddBusinessWhenBusinessExists() {
        when(businessMapper.selectByUsername(any())).thenReturn(business);

        CustomException exception = null;
        try {
            businessService.add(business);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_EXIST_ERROR;
    }

    @Test
    public void testDeleteById() {
        doNothing().when(businessMapper).deleteById(anyInt());

        businessService.deleteById(1);

        verify(businessMapper, times(1)).deleteById(1);
    }

    @Test
    public void testUpdateById() {
        when(businessMapper.updateById(any(Business.class))).thenReturn(1);

        businessService.updateById(business);

        verify(businessMapper, times(1)).updateById(any(Business.class));
    }

    @Test
    public void testLoginWhenBusinessExistsAndPasswordCorrect() {
        when(businessMapper.selectByUsername(any())).thenReturn(business);

        Account loggedInAccount = businessService.login(account);

        assert loggedInAccount.getUsername().equals("business1");
        assert loggedInAccount.getToken() != null;
    }

    @Test
    public void testLoginWhenBusinessDoesNotExist() {
        when(businessMapper.selectByUsername(any())).thenReturn(null);

        CustomException exception = null;
        try {
            businessService.login(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_NOT_EXIST_ERROR;
    }

    @Test
    public void testLoginWhenPasswordIncorrect() {
        business.setPassword("wrongPassword");
        when(businessMapper.selectByUsername(any())).thenReturn(business);

        CustomException exception = null;
        try {
            businessService.login(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_ACCOUNT_ERROR;
    }

    @Test
    public void testRegister() {
        when(businessMapper.selectByUsername(any())).thenReturn(null);

        businessService.register(account);

        verify(businessMapper, times(1)).insert(any(Business.class));
    }

    @Test
    public void testUpdatePassword() {
        when(businessMapper.selectByUsername(any())).thenReturn(business);
        when(businessMapper.updateById(any(Business.class))).thenReturn(1);

        businessService.updatePassword(account);

        verify(businessMapper, times(1)).updateById(any(Business.class));
    }

    @Test
    public void testUpdatePasswordWhenBusinessNotExist() {
        when(businessMapper.selectByUsername(any())).thenReturn(null);

        CustomException exception = null;
        try {
            businessService.updatePassword(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.USER_NOT_EXIST_ERROR;
    }

    @Test
    public void testUpdatePasswordWhenPasswordIncorrect() {
        business.setPassword("wrongPassword");
        when(businessMapper.selectByUsername(any())).thenReturn(business);

        CustomException exception = null;
        try {
            businessService.updatePassword(account);
        } catch (CustomException e) {
            exception = e;
        }

        assert exception != null;
        assert exception.getResultCode() == ResultCodeEnum.PARAM_PASSWORD_ERROR;
    }
}
