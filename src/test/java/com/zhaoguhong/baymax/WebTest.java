package com.zhaoguhong.baymax;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void anonymousDemoEndpointIsAccessible() throws Exception {
    mockMvc.perform(get("/test/successResult"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"));
  }

  @Test
  public void protectedEndpointRedirectsToLogin() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection());
  }
}
