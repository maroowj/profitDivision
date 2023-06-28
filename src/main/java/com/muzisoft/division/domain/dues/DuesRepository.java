package com.muzisoft.division.domain.dues;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuesRepository extends JpaRepository<Dues, String>, DuesQueryRepository {

    List<Dues> findByDuesMonthAndPaymentState(DuesMonth duesMonth, int paymentState);

    List<Dues> findAllByUserDetailsAndPaymentState(UserDetails userDetails, int paymentState);

    Optional<Dues> findByDuesMonthAndUserDetailsAndPaymentState(DuesMonth duesMonth, UserDetails userDetails, int paymentState);
}