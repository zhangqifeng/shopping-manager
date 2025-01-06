package com.example.test;

import com.example.entity.Type;
import com.example.mapper.TypeMapper;
import com.example.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TypeServiceTest {

    @Mock
    private TypeMapper typeMapper;

    @InjectMocks
    private TypeService typeService;

    private Type type;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample Type object
        type = new Type();
        type.setId(1);
        type.setName("Test Type");
    }

    @Test
    public void testAddType() {
        // Call the method under test
        typeService.add(type);

        // Verify that the insert method is called once with the type object
        verify(typeMapper, times(1)).insert(type);
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of deleteById
        doNothing().when(typeMapper).deleteById(anyInt());

        // Call the method under test
        typeService.deleteById(1);

        // Verify that the deleteById method is called once with the correct ID
        verify(typeMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        // Mock the behavior of deleteById for each ID
        doNothing().when(typeMapper).deleteById(anyInt());

        // Call the method under test
        typeService.deleteBatch(ids);

        // Verify that the deleteById method is called once for each ID in the list
        verify(typeMapper, times(3)).deleteById(anyInt());
    }

    @Test
    public void testUpdateById() {
        // Mock the behavior of updateById
        when(typeMapper.updateById(any(Type.class))).thenReturn(1);

        // Call the method under test
        typeService.updateById(type);

        // Verify that the updateById method is called once with the correct type
        verify(typeMapper, times(1)).updateById(type);
    }

    @Test
    public void testSelectById() {
        // Mock the behavior of selectById
        when(typeMapper.selectById(anyInt())).thenReturn(type);

        // Call the method under test
        Type result = typeService.selectById(1);

        // Assert that the returned type matches the expected type
        assertEquals("Test Type", result.getName());
    }

    @Test
    public void testSelectAll() {
        // Mock the behavior of selectAll
        List<Type> types = Arrays.asList(type);
        when(typeMapper.selectAll(any(Type.class))).thenReturn(types);

        // Call the method under test
        List<Type> result = typeService.selectAll(new Type());

        // Assert that the result contains the correct type
        assertEquals(1, result.size());
        assertEquals("Test Type", result.get(0).getName());
    }

    @Test
    public void testSelectPage() {
        // Mock the behavior of selectAll
        List<Type> types = Arrays.asList(type);
        PageInfo<Type> pageInfo = new PageInfo<>(types);
        when(typeMapper.selectAll(any(Type.class))).thenReturn(types);

        // Call the method under test
        PageInfo<Type> result = typeService.selectPage(new Type(), 1, 10);

        // Assert that the result is a non-null PageInfo object and contains the expected type
        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals("Test Type", result.getList().get(0).getName());
    }
}
