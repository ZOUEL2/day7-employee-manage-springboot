package org.example.springboottest;

import jakarta.annotation.Resource;
import org.example.springboottest.po.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeTest {

    @Resource
    private MockMvc mockMvc;

    private String employeeJson(String name, String gender, int age, double salary) {
        return """
                {
                  "name":"%s",
                  "gender":"%s",
                  "age":%d,
                  "salary":%s
                }
                """.formatted(name, gender, age, salary);
    }

    @Test
    void should_create_employee_when_post_given_a_valid_body() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Tom", "Male", 18, 5000.0))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Tom", "Male", 18, 5000.0))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void should_find_employee_when_given_id() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Tom", "Male", 18, 5000.0)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/employees/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    void should_return_404_when_get_employee_given_not_exist_id() throws Exception {
        mockMvc.perform(get("/employees/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_query_employees_by_gender() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Tom", "Male", 18, 5000.0)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Lucy", "Female", 22, 7000.0)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/employees").param("gender", "male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Tom"));
    }

    @Test
    void should_list_all_employees() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Tom", "Male", 18, 5000.0)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Lucy", "Female", 22, 7000.0)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }


}