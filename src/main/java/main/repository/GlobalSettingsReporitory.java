package main.repository;

import main.entity.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsReporitory extends JpaRepository <GlobalSettings, Integer> {
}
