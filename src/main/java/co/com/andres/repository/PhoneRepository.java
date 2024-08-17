package co.com.andres.repository;

import co.com.andres.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<PhoneRepository> {

    Optional<Phone> findByNumberAndCityCodeAndCountryCode(String number, String cityCode, String countryCode);
}