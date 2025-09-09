package org.example.springboottest;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CompanyTest {

    @Resource
    private MockMvc mockMvc;

    private long createCompany(String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"%s"
                                }
                                """.formatted(name)))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        return Long.parseLong(content.replaceAll("\\D+", ""));
    }

    @Test
    void should_create_company_when_post_given_a_valid_body() throws Exception {
        long id1 = createCompany("alibaba");
        long id2 = createCompany("tencent");
        // 简单断言顺序生成
        assert id1 == 1;
        assert id2 == 2;
    }

    @Test
    void should_list_all_companies() throws Exception {
        createCompany("alibaba");
        createCompany("tencent");

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}
