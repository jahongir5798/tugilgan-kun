package uz.jahonservice.birthdate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.jahonservice.birthdate.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> deleteByEmail(String email);

//    Page<User> findAll(Pageable pageable);
//
//    Page<User> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    List<User> findAllByFirstNameContainingIgnoreCase(String firstName);



}
