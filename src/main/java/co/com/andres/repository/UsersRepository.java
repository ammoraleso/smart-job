package co.com.andres.repository;

import co.com.andres.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByIdAndActiveTrue(Long id);
    Optional<User> findByEmailAndActiveTrue(String email);

    Page<User> findByActiveTrue(Pageable pageable);
}