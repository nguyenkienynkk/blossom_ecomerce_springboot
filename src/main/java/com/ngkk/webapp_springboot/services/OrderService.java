package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.OrderDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Order;
import com.ngkk.webapp_springboot.models.OrderStatus;
import com.ngkk.webapp_springboot.models.User;
import com.ngkk.webapp_springboot.repositories.OrderRepository;
import com.ngkk.webapp_springboot.repositories.UserRepository;
import com.ngkk.webapp_springboot.services.impl.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {
    UserRepository userRepository;
    OrderRepository orderRepository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem user id đó có tồn tại hay không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id:" + orderDTO));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());  //lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
        //Kiểm tra shipping date phải là ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Can not find Order with id " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Can not find user with id " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional //Nếu có 1 trong các dữ liệu bị hỏng thì sẽ quay lại ban đầu rollback
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByOrderId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
