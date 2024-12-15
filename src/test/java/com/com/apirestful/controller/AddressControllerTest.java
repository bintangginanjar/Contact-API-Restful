package com.com.apirestful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import com.com.apirestful.entity.AddressEntity;
import com.com.apirestful.entity.ContactEntity;
import com.com.apirestful.entity.UserEntity;
import com.com.apirestful.model.AddressResponse;
import com.com.apirestful.model.CreateAddressRequest;
import com.com.apirestful.model.UpdateAddressRequest;
import com.com.apirestful.model.WebResponse;
import com.com.apirestful.repository.AddressRepository;
import com.com.apirestful.repository.ContactRepository;
import com.com.apirestful.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {        
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUsername("bintang.ginanjar");
        user.setName("Bintang Ginanjar");
        user.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 1));
        userRepository.save(user);

        ContactEntity contact = new ContactEntity();        
        contact.setUser(user);
        contact.setFirstname("Bintang");
        contact.setLastname("Ginanjar");
        contact.setEmail("bintang@gmail.com");
        contact.setPhone("123456");
        contactRepository.save(contact);
    }

    @Test
    void testCreateAddressSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Pasirluyu");   
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");        
        request.setCountry("Indonesia");
        request.setPostalCode("40254");

        mockMvc.perform(
                post("/api/contacts/" + contact.getId() + "/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void testCreateAddressBadContactId() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();        
        request.setCountry("Indonesia");

        mockMvc.perform(
                post("/api/contacts/123test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateAddressNullCountry() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();        
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/123/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateAddressContactNotFound() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();        
        request.setCountry("Indonesia");

        mockMvc.perform(
                post("/api/contacts/123456/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId() + "/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void testGetAddressAddressNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Pasirluyu");   
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");        
        request.setCountry("Indonesia");
        request.setPostalCode("40254");
        
        mockMvc.perform(
                get("/api/contacts/" + contact.getId() + "/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressContactNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);
        
        mockMvc.perform(
                get("/api/contacts/123/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressAddressAndContactNotFound() throws Exception {                
        mockMvc.perform(
                get("/api/contacts/123/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressBadContactId() throws Exception {                
        mockMvc.perform(
                get("/api/contacts/123test/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressBadAddressId() throws Exception {                
        mockMvc.perform(
                get("/api/contacts/123/addresses/123test")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Puri Suryalaya");   
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");        
        request.setCountry("Indonesia");
        request.setPostalCode("40239");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId() + "/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void testUpdateAddressNullCountry() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contacts/123/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressBadAddressId() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("Indonesia");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId() + "/addresses/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressBadContactId() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("Indonesia");

        mockMvc.perform(
                put("/api/contacts/123test/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressContactNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("Indonesia");

        mockMvc.perform(
                put("/api/contacts/123/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressAddressNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("Indonesia");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId() + "/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteAddressSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId() + "/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
            assertFalse(addressRepository.existsById(address.getId()));
        });
    }

    @Test
    void testDeleteAddressBadContactId() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/123test/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteAddressBadAddressId() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId() + "/addresses/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteAddressContactNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/123/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteAddressAddressNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId() + "/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressesBadContactId() throws Exception {                
        mockMvc.perform(
                get("/api/contacts/123test/addresses")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressesSuccess() throws Exception {      
        UserEntity user = userRepository.findByUsername("bintang.ginanjar").orElse(null);
        ContactEntity contact = contactRepository.findFirstByUser(user).orElse(null);

        AddressEntity address = new AddressEntity();
        address.setStreet("Pasirluyu");   
        address.setCity("Bandung");
        address.setProvince("Jawa Barat");        
        address.setCountry("Indonesia");
        address.setPostalCode("40254");
        address.setContact(contact);

        addressRepository.save(address);
        
        mockMvc.perform(
                get("/api/contacts/" + contact.getId() + "/addresses")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressesContactNotFound() throws Exception {                
        mockMvc.perform(
                get("/api/contacts/123/addresses")
                        .accept(MediaType.APPLICATION_JSON)                      
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")               
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }
}
