package co.com.andres.web;

import co.com.andres.dto.UserDTO;
import co.com.andres.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "1") int size){
        List<UserDTO> listUser = userService.getAllUsers(page, size);
        return listUser;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = fieldError.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
                return ResponseEntity.badRequest().body(errors);
            }
            UserDTO userInDB = userService.findByEmail(userDTO);
            if(userInDB != null){
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("El correo ya esta registrado");
            }
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(createdUser);
        } catch (JsonParseException | NullPointerException e) {
            log.error("Error parsing user JSON: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing user JSON.");
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating user due to data integrity violation: ", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error creating user due to data integrity violation.");
        } catch (Exception e) {
            log.error("Error creating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user.");
        }
    }

    @PatchMapping("/{userId}")
    ResponseEntity<?> updateUser(@RequestBody UserDTO userToUpdate, @PathVariable Long userId) {
        try {
            userToUpdate.setId(userId);
            UserDTO updatedUser = userService.update(userToUpdate);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedUser);
        } catch (Exception e) {
            log.error("Error updating user: ");
            e.printStackTrace();
            if (e instanceof JsonParseException || e instanceof NullPointerException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing user JSON.");
            } else if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error updating iser due data integrity violation: ");
            } else if (e instanceof NoSuchElementException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user.");
            }
        }
    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserDTO userDTO = userService.findById(userId);
            return ResponseEntity.ok(userDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: El usuario no fue encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Ha ocurrido un error al consultar el usuario");
        }
    }

    @PatchMapping("/{userId}/delete")
    public ResponseEntity<Void> softDeleteProduct(@PathVariable Long userId) {
        userService.softDeleteProduct(userId);
        return ResponseEntity.noContent().build();
    }
}
