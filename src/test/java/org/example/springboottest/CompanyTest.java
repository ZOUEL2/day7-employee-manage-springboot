package org.example.springboottest;

import jakarta.annotation.Resource;
import org.example.springboottest.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyTest {

    @Resource
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        companyRepository.clear();
    }

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
        assert id1 + 1 == id2;

    }

    @Test
    void should_list_all_companies() throws Exception {
        createCompany("alibaba");
        createCompany("tencent");

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_get_company_by_id_success() throws Exception {
        createCompany("TestCompany");

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCompany"));
    }

    @Test
    public void should_return_404_when_company_not_found() throws Exception {
        mockMvc.perform(get("/companies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_list_companies_with_pagination() throws Exception {
        for (int i = 1; i <= 5; i++) {
            createCompany("Company" + i);
        }

        mockMvc.perform(get("/companies")
                        .param("page", "2")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Company3"))
                .andExpect(jsonPath("$[1].name").value("Company4"));
    }

    @Test
    void should_update_company_name_when_put_given_a_valid_body() throws Exception {
        long id = createCompany("OldName");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"NewName"
                                }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/companies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));
    }

    @Test
    void should_delete_company_when_delete_given_existing_id() throws Exception {
        long id = createCompany("ToBeDeleted");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/companies/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/companies/{id}", id))
                .andExpect(status().isNotFound());
    }


}
