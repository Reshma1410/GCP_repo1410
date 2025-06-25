package org.example.service;

import com.google.cloud.spring.storage.GoogleStorageResource;
import org.example.common.BaseResponse;
import org.example.model.Order;
import org.springframework.web.multipart.MultipartFile;


public interface OrderService {

    BaseResponse createOrder(Order order);
    BaseResponse getOrderById(Long id);
    BaseResponse getAllOrders();
    BaseResponse updateOrder(Long id, Order order);
    BaseResponse deleteOrder(Long id);

    String uploadFile(MultipartFile file);


}
