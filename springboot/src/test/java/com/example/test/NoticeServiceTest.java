package com.example.test;

import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NoticeServiceTest {

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    private Notice notice;
    private Account currentUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        notice = new Notice();
        notice.setId(1);
        notice.setTitle("Test Notice");
        notice.setContent("This is a test notice");

        currentUser = new Account();
        currentUser.setUsername("admin");
    }

    @Test
    public void testAddNotice() {
        // Mock the static method TokenUtils.getCurrentUser()
        try (MockedStatic<TokenUtils> mockedTokenUtils = mockStatic(TokenUtils.class)) {
            mockedTokenUtils.when(TokenUtils::getCurrentUser).thenReturn(currentUser);

            noticeService.add(notice);

            verify(noticeMapper, times(1)).insert(notice);
            assertEquals("admin", notice.getUser()); // Ensure the user is set from the current user
            assertNotNull(notice.getTime()); // Ensure time is set
        }
    }

    @Test
    public void testDeleteById() {
        doNothing().when(noticeMapper).deleteById(anyInt());

        noticeService.deleteById(1);

        verify(noticeMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        doNothing().when(noticeMapper).deleteById(anyInt());

        noticeService.deleteBatch(ids);

        verify(noticeMapper, times(3)).deleteById(anyInt());
    }

    @Test
    public void testUpdateById() {
        when(noticeMapper.updateById(any(Notice.class))).thenReturn(1);

        noticeService.updateById(notice);

        verify(noticeMapper, times(1)).updateById(any(Notice.class));
    }

    @Test
    public void testSelectById() {
        when(noticeMapper.selectById(anyInt())).thenReturn(notice);

        Notice result = noticeService.selectById(1);

        assertEquals("Test Notice", result.getTitle());
        assertEquals("This is a test notice", result.getContent());
    }

    @Test
    public void testSelectAll() {
        List<Notice> notices = Arrays.asList(notice);
        when(noticeMapper.selectAll(any(Notice.class))).thenReturn(notices);

        List<Notice> result = noticeService.selectAll(new Notice());

        assertEquals(1, result.size());
        assertEquals("Test Notice", result.get(0).getTitle());
    }

    @Test
    public void testSelectPage() {
        List<Notice> notices = Arrays.asList(notice);
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        when(noticeMapper.selectAll(any(Notice.class))).thenReturn(notices);

        PageInfo<Notice> result = noticeService.selectPage(new Notice(), 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals("Test Notice", result.getList().get(0).getTitle());
    }
}
