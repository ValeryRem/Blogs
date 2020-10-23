package main.repository;

import main.entity.CaptchaCode;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaRepository extends CrudRepository <CaptchaCode, Integer> {
}
