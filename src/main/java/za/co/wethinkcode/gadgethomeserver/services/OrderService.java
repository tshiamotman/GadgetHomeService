package za.co.wethinkcode.gadgethomeserver.services;

import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.models.database.Order;
import za.co.wethinkcode.gadgethomeserver.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public void addOrder(Order order) {
        orderRepo.save(order);
    }

    public Order orderRecieved(Order order) {
        order.setTransit("Delivered");
        order.setPaymentReleased(true);
        orderRepo.deleteById(order.getId());
        return orderRepo.save(order);
    }

    public String trackOrder(String id) {
        Order order = orderRepo.getById(Long.valueOf(id));
        return order.getTransit();
    }

    public Order getOrder(Long id) {
        return orderRepo.getById(Long.valueOf(id));
    }

    public void deleteOrder(Order order) {
        orderRepo.delete(order);;
    }
}
