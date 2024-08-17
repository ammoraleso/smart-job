package co.com.andres.service;

import co.com.andres.domain.User;
import co.com.andres.dto.UserDTO;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers(int page, int size);

    UserDTO createUser(UserDTO userDTO);
    void softDeleteProduct(Long userId);

    UserDTO update(UserDTO userToUpdate);

    UserDTO findById(Long id);

    UserDTO findByEmail(UserDTO userDTO);
}
