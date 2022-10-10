package com.woolee.app.repositories;

import com.woolee.app.models.Curtidas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurtidasRepository extends JpaRepository<Long, Curtidas> {
}
