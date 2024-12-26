package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.mapper.AddressMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.junit.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class) // 使用 Mockito 扩展
@SpringBootTest

public class AddressServiceTest {

@Autowired
    private AddressService addressService;

    @Mock
    private AddressMapper addressMapper;
    @Mock
    private TokenUtils tokenUtils;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testAdd() {
        Address address = new Address();
        address.setId(1);
        address.setUserId(101);
        addressService.add(address);

        verify(addressMapper, times(1)).insert(address);
    }

    @Test
    public void testDeleteById() {
        int addressId = 1;

        addressService.deleteById(addressId);

        verify(addressMapper, times(1)).deleteById(addressId);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        addressService.deleteBatch(ids);

        verify(addressMapper, times(3)).deleteById(anyInt());
    }

    @Test
    public void testUpdateById() {
        Address address = new Address();
        address.setId(1);
        address.setUserId(101);

        addressService.updateById(address);

        verify(addressMapper, times(1)).updateById(address);
    }


    @Test
    public void testSelectAll() {
        Address filter = new Address();
        List<Address> expectedList = Arrays.asList(new Address(), new Address());
        when(addressMapper.selectAll(filter)).thenReturn(expectedList);

        List<Address> result = addressService.selectAll(filter);

        assertEquals(expectedList.size(), result.size());
        verify(addressMapper, times(1)).selectAll(filter);
    }

    @Test
    public void testSelectPage() {
        Address filter = new Address();
        Account currentUser = new Account();
        currentUser.setId(101);
        currentUser.setRole(RoleEnum.USER.name());
        List<Address> addressList = Arrays.asList(new Address(), new Address());

        when(TokenUtils.getCurrentUser()).thenReturn(currentUser);
        when(addressMapper.selectAll(filter)).thenReturn(addressList);

        PageInfo<Address> pageInfo = addressService.selectPage(filter, 1, 10);

        assertEquals(addressList.size(), pageInfo.getList().size());
        verify(addressMapper, times(1)).selectAll(filter);
    }
}