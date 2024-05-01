package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.OrderDetailDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Order;
import com.ngkk.webapp_springboot.models.OrderDetail;
import com.ngkk.webapp_springboot.models.Product;
import com.ngkk.webapp_springboot.repositories.OrderDetailRepository;
import com.ngkk.webapp_springboot.repositories.OrderRepository;
import com.ngkk.webapp_springboot.repositories.ProductRepository;
import com.ngkk.webapp_springboot.services.impl.IOrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws DataNotFoundException {
        Order orderexisting = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id:" + newOrderDetail.getOrderId()));
        Product productexisting = productRepository.findById(newOrderDetail.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id:" + newOrderDetail.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(orderexisting)
                .product(productexisting)
                .price(newOrderDetail.getPrice())
                .numberOfProducts(newOrderDetail.getNumberOfProducts())
                .totalMoney(newOrderDetail.getTotalMoney())
                .color(newOrderDetail.getColor())
                .build();
        //Luu vao db
        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id:" + id));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        //tìm xem order detail có tồn tại không
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id:" + id));
        //Xem orderid có thuộc về 1 order nào đó không
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id:" + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id:" + orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
