package org.example.empresa.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.entity.Role;
import org.example.empresa.entity.User;
import org.example.empresa.exception.ProductNotFoundException;
import org.example.empresa.repository.UserRepository;
import org.example.empresa.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

    private static final String ADMIN_EMAIL = "admin.product.test@gmail.com";
    private static final String PASSWORD = "12345";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ProductService service;

    @BeforeEach
    void setUp() {
        User user = new User(
                null,
                ADMIN_EMAIL,
                passwordEncoder.encode(PASSWORD),
                Role.ADMIN,
                true
        );

        userRepository.save(user);
    }


    private String loginAndGetToken(String email) throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto(email, PASSWORD);

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response)
                .get("token")
                .asText();
    }

    @Test
    void testProductNotFoundException() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        when(service.getById(140L))
                .thenThrow(new ProductNotFoundException(140L));

        mockMvc.perform(get("/api/v1/products/140")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}