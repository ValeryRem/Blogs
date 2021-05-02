package main.repository;

import main.entity.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalSettingsRepository extends JpaRepository <GlobalSettings, Integer> {
}
