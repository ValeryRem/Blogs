package main.repository;

import main.entity.CaptchaCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends CrudRepository <CaptchaCode, Integer> {
}
