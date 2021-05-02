package main.repository;

import main.entity.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {
    Optional<CaptchaCode> findBySecretCode (String secretCode);
}
