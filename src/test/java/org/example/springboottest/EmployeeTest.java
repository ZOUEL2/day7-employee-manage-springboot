package org.example.springboottest;

import jakarta.annotation.Resource;
import org.example.springboottest.repository.EmployeeJpaRepository;
import org.example.springboottest.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeTest {

    @Resource
    private EmployeeRepository employeeRepository;

    @Resource
    private MockMvc mockMvc;

    private long defaultCompanyId;

    @BeforeEach
    void setUp() throws Exception {
        employeeRepository.clear();
        // 每个用例准备一个公司
        MvcResult result = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"defaultCo"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        defaultCompanyId = Long.parseLong(content.replaceAll("\\D+", ""));
    }

    private String employeeJson(String name, String gender, int age, double salary, long companyId) {
        return """
                {
                  "name":"%s",
                  "gender":"%s",
                  "age":%d,
                  "salary":%s,
                  "companyId":%d
                }
                """.formatted(name, gender, age, salary, companyId);
    }

    private long createEmployee(String name, String gender, int age, double salary) throws Exception {
        MvcResult result = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson(name, gender, age, salary, defaultCompanyId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.activeStatus").value(true))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        return Long.parseLong(content.replaceAll("\\D+", ""));
    }

    @Test
    public void should_create_employee_when_post_given_a_valid_body() throws Exception {
        long id1 = createEmployee("Tom", "Male", 18, 5000.0);
        long id2 = createEmployee("Jerry", "Male", 18, 5000.0);
        assert id1 + 1 == id2;
    }

    @Test
    void should_return_400_when_create_employee_with_duplicate_name_and_gender() throws Exception {
        createEmployee("Alice", "Female", 25, 21000.0);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Alice", "Female", 26, 30000.0, defaultCompanyId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_find_employee_when_given_id() throws Exception {
        long id = createEmployee("Tom", "Male", 18, 5000.0);
        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0))
                .andExpect(jsonPath("$.companyId").value((int) defaultCompanyId));
    }

    @Test
    void should_return_404_when_get_employee_given_not_exist_id() throws Exception {
        mockMvc.perform(get("/employees/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_query_employees_by_gender() throws Exception {
        createEmployee("Tom", "Male", 18, 5000.0);
        createEmployee("Lucy", "Female", 22, 7000.0);

        mockMvc.perform(get("/employees").param("gender", "Male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Tom"));
    }

    @Test
    void should_list_all_employees() throws Exception {
        createEmployee("Tom", "Male", 18, 5000.0);
        createEmployee("Lucy", "Female", 22, 7000.0);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_update_employee_when_put_given_existing_id() throws Exception {
        long id = createEmployee("Tom", "Male", 18, 5000.0);
        String updateBody = employeeJson("TomUpdated", "Male", 19, 8000.0, defaultCompanyId);

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("TomUpdated"))
                .andExpect(jsonPath("$.age").value(19))
                .andExpect(jsonPath("$.salary").value(8000.0))
                .andExpect(jsonPath("$.companyId").value((int)defaultCompanyId));
    }

    @Test
    void should_return_404_when_put_given_inactive_employee() throws Exception {
        long id = createEmployee("Tom", "Male", 18, 5000.0);

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());

        String updateBody = employeeJson("CannotUpdate", "Male", 20, 6000.0, defaultCompanyId);
        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404_when_put_given_not_exist_id() throws Exception {
        String updateBody = employeeJson("Ghost", "Male", 30, 10000.0, defaultCompanyId);

        mockMvc.perform(put("/employees/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_employee_when_delete_given_existing_id() throws Exception {
        long id = createEmployee("Tom", "Male", 18, 5000.0);

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.activeStatus").value(false));
    }

    @Test
    void should_return_404_when_delete_given_not_exist_id() throws Exception {
        mockMvc.perform(delete("/employees/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_page_query_employees_when_given_page_and_size() throws Exception {
        createEmployee("E1", "Male", 20, 1000);
        createEmployee("E2", "Male", 21, 1000);
        createEmployee("E3", "Female", 22, 1000);
        createEmployee("E4", "Male", 23, 1000);
        createEmployee("E5", "Female", 24, 1000);
        createEmployee("E6", "Male", 25, 1000);
        createEmployee("E7", "Female", 26, 1000);

        mockMvc.perform(get("/employees").param("page", "1").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("E1"))
                .andExpect(jsonPath("$[4].name").value("E5"));

        mockMvc.perform(get("/employees").param("page", "2").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("E6"))
                .andExpect(jsonPath("$[1].name").value("E7"));
    }

    @Test
    void should_return_empty_list_when_page_out_of_range() throws Exception {
        createEmployee("E1", "Male", 20, 1000);
        createEmployee("E2", "Male", 21, 1000);

        mockMvc.perform(get("/employees").param("page", "2").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_return_400_when_create_employee_age_over_30_and_salary_below_20000() throws Exception {
        String createBody = employeeJson("TomUpdated", "Male", 39, 8000.0, defaultCompanyId);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isBadRequest());
    }

}