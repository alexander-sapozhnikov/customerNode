package ru.sapozhnikov.dao;

import org.springframework.data.repository.CrudRepository;
import ru.sapozhnikov.entity.PaidType;

public interface PaidTypeDAO extends CrudRepository<PaidType, Long> {
    PaidType getById(int id);
}
