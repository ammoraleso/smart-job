package co.com.andres.service;

import co.com.andres.domain.Phone;
import co.com.andres.domain.User;
import co.com.andres.dto.UserDTO;
import co.com.andres.repository.PhoneRepository;
import co.com.andres.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageResult = usersRepository.findByActiveTrue(pageable);
        List<User> users = pageResult.getContent();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        List<Phone> phones = userDTO.getPhones().stream()
                .map(phoneDTO -> modelMapper.map(phoneDTO, Phone.class))
                .collect(Collectors.toList());

        phones.forEach(phone -> phone.setUser(user));

        user.setPhones(phones);
        User savedUser = usersRepository.save(user);
        return  modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + userId + " no existe"));
        user.setActive(false);
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Optional<User> currentUser = usersRepository.findById(user.getId());
        if (currentUser.isEmpty()) {
            throw new NoSuchElementException("No se encontró el user con Id:  " + user.getId());
        }
        user = currentUser.get();

        if(userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if(userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        user.setUpdatedAt(new Date());
        user = usersRepository.save((user));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> user = usersRepository.findByIdAndActiveTrue(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el user con Id:  " + id);
        }
        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Override
    public UserDTO findByEmail(UserDTO userDTO) {
        Optional<User> userInBD = usersRepository.findByEmailAndActiveTrue(userDTO.getEmail());

        if(userInBD.isPresent()) {
            return modelMapper.map(userInBD.get(), UserDTO.class);
        }
        return null;
    }

}
