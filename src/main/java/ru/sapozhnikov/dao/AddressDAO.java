package ru.sapozhnikov.dao;

import org.springframework.data.repository.CrudRepository;
import ru.sapozhnikov.entity.Address;

public interface AddressDAO extends CrudRepository<Address, Long> {
}
