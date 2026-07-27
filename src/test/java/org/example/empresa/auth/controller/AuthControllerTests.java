    package org.example.empresa.auth.controller;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import jakarta.transaction.Transactional;
    import org.example.empresa.dto.product.v1.ProductRequestDto;
    import org.example.empresa.dto.security.LoginRequestDto;
    import org.example.empresa.dto.security.RegisterRequestDto;
    import org.example.empresa.entity.Role;
    import org.example.empresa.entity.User;
    import org.example.empresa.repository.UserRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.http.MediaType;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.test.context.ActiveProfiles;
    import org.springframework.test.web.servlet.MockMvc;

    import java.math.BigDecimal;

    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @Transactional
    class AuthControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private UserRepository repository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        void setUp() {
            User user = new User(1L, "admin@gmail.com", passwordEncoder.encode("12345")
            , Role.ADMIN, true);

            User user2 = new User(2L, "user@gmail.com", passwordEncoder.encode("12345")
                    , Role.USER, true);

            repository.save(user);
            repository.save(user2);
        }

        private String loginAndGetToken(String email) throws Exception {
            LoginRequestDto requestDto = new LoginRequestDto(email, "12345");
            String response = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            return mapper.readTree(response).get("token").asText();
        }

        @Test
        void shouldRegisterSuccessfully() throws Exception {
            RegisterRequestDto request = new RegisterRequestDto("email@email.com", Role.USER, "12345");

            mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value(request.getEmail()));
        }

        @Test
        void shouldLoginSuccessfully() throws Exception {
            LoginRequestDto requestDto = new LoginRequestDto("admin@gmail.com", "12345");

            mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.expiresIn").exists());
        }

        @Test
        void shouldLoginAndFail() throws Exception {
            LoginRequestDto requestDto = new LoginRequestDto("admin@gmail.com", "123098");

            mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldUnauthorizedGetProductNoToken() throws Exception {
            mockMvc.perform(get("/api/v1/products"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldGetProductsValidToken() throws Exception {
            String token = loginAndGetToken("user@gmail.com");

            mockMvc.perform(get("/api/v1/products").header("Authorization", "Bearer "
            + token))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldForbiddenWhenPostProduct() throws Exception {
            String token = loginAndGetToken("user@gmail.com");

            ProductRequestDto request = new ProductRequestDto("Mouse",
                    "Mouse inalambrico",new BigDecimal("25.99"),
                    50, 1L);

            mockMvc.perform(post("/api/v1/products").header("Authorization","Bearer "
                            + token).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void shouldCreateProductSuccessfully() throws Exception {
            String token = loginAndGetToken("admin@gmail.com");
            ProductRequestDto request = new ProductRequestDto("Mouse",
                    "Mouse inalambrico",new BigDecimal("25.99"),
                    50, 1L);

            mockMvc.perform(post("/api/v1/products").header("Authorization","Bearer "
                            + token).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldGetForbiddenWhenModifiedToken() throws Exception {
            String token = loginAndGetToken("user@gmail.com");

            mockMvc.perform(get("/api/v1/products").header("Authorization", "Bearer "
                            + token + "123"))
                    .andExpect(status().isUnauthorized());
        }
    }
