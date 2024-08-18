package service;

import co.com.andres.domain.User;
import co.com.andres.dto.PhoneDTO;
import co.com.andres.dto.UserDTO;
import co.com.andres.repository.PhoneRepository;
import co.com.andres.repository.UsersRepository;
import co.com.andres.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest{

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private static UsersRepository usersRepository;
    @Mock
    private static PhoneRepository phoneRepository;
    @Spy
    private static ModelMapper modelMapper;

    private static UserDTO userDTO;
    private static List<PhoneDTO> phones;
    private static PhoneDTO phone1;
    private static User user;

    @BeforeAll
    private static void setup() {
        phone1 = new PhoneDTO();
        phone1.setNumber("123456789");
        phone1.setCityCode("1");
        phone1.setCountryCode("57");

        phones = Arrays.asList(phone1);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Andres Morales");
        userDTO.setEmail("andres.morales@example.com");
        userDTO.setPassword("password123");
        userDTO.setPhones(phones);
        userDTO.setCreatedAt(new Date());
        userDTO.setUpdatedAt(new Date());
        userDTO.setActive(false);

        ModelMapper modelMapper = new ModelMapper();
        user = modelMapper.map(userDTO, User.class);

    }

    @BeforeEach
    private void init() {
        userService.setUsersRepository(usersRepository);
        userService.setPhoneRepository(phoneRepository);
        userService.setModelMapper(modelMapper);
    }

    @Test
    public void testGetAllUsers() {

        List<User> users = Arrays.asList(user);
        Page<User> pageResult = new PageImpl<>(users);

        when(usersRepository.findByActiveTrue(any(Pageable.class))).thenReturn(pageResult);

        List<UserDTO> result = userService.getAllUsers(0, 2);

        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
    }

    @Test
    public void testCreateUser() {
        when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDTO result = userService.createUser(userDTO);
        assertEquals(userDTO, result);
    }

    @Test
    public void testSoftDeleteUser() {
        when(usersRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        Assertions.assertDoesNotThrow(() -> userService.softDeleteProduct(1L));
    }

    @Test
    public void testFindById() {
        when(usersRepository.findByIdAndActiveTrue(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        UserDTO result = userService.findById(1L);
        assertEquals(userDTO, result);
    }

    @Test
    public void testFindByEmail() {
        when(usersRepository.findByEmailAndActiveTrue(Mockito.any(String.class))).thenReturn(Optional.ofNullable(user));
        UserDTO result = userService.findByEmail(userDTO);
        assertEquals(userDTO, result);
    }

    @Test
    public void testUpdateUser() {
        when(usersRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDTO result = userService.update(userDTO);
        userDTO.setUpdatedAt(result.getUpdatedAt());
        assertEquals(userDTO, result);
    }


}
