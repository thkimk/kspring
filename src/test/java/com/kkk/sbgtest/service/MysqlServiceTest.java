package com.kkk.sbgtest.service;

import com.kkk.sbgtest.jpa.Posts;
import com.kkk.sbgtest.jpa.PostsRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = { "propertyTest.value=propertyTest", "testValue=test" }
)

@AutoConfigureMockMvc
class MysqlServiceTest {

    @Value("${propertyTest.value}")
    private String propertyTestValue;

    @Value("${testValue}")
    private String value; //test

    @Autowired
    MockMvc mvc;

    @Autowired
    PostsRepository postsRepository;

    //@Test
    void selectB2bBas() throws Exception {
/*
        mvc.perform(get("/test2"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello saelobi"))
                .andDo(print());
*/

    }

//    @After
//    public void cleanup() {
//        postsRepository.deleteAll();
//    }

    @Test
    public void testJpa() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

/*
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("b088081@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
*/
//        assertThat(posts.getTitle()).isEqualTo(title);
//        assertThat(posts.getContent()).isEqualTo(content);
    }
}