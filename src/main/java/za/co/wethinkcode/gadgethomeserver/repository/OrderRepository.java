package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.wethinkcode.gadgethomeserver.models.database.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
