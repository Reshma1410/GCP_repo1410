package org.example.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.example.common.BaseResponse;
import org.example.constants.ConstantCode;
import org.example.repository.OrderRepository;

import org.example.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;




//    @PostConstruct
//    public void init() throws IOException {
//        GoogleCredentials credentials = GoogleCredentials.fromStream(
//                new FileInputStream("D:/BackupData/helical-castle-462604-s0-bec051f1850a.json")
//        );
//
//        storage = StorageOptions.newBuilder()
//                .setCredentials(credentials)
//                .setProjectId("helical-castle-462604-s0")
//                .build()
//                .getService();
//    }


//    @Override
//    public String uploadFile(MultipartFile file) throws IOException {
//        try{
//            BlobId blobId = BlobId.of(bucketName, Objects.requireNonNull(file.getOriginalFilename()));
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
//
//            storage.create(blobInfo, file.getBytes());
//            System.out.println("---> service is ok");
//            return String.format("https://storage.googleapis.com/%s/%s", bucketName, file.getOriginalFilename());
//
//        } catch (Exception e) {
//            System.err.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Upload failed: " + e.getMessage()));
//            throw new IOException("Upload failed: " + e.getMessage(), e);  // propagate the error to the controller
//        }
//        }

    @Override
    public String uploadFile(MultipartFile file)  {
        try{
            BlobId blobID = BlobId.of(bucketName, Objects.requireNonNull(file.getOriginalFilename()));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobID).setContentType(file.getContentType()).build();
            storage.create(blobInfo,file.getBytes());
            return null;
        } catch (IOException e) {
            System.err.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage()));
            throw new RuntimeException(e);
        }

    }


    @Override
    public BaseResponse createOrder(Order order) {
        try{
            System.out.println("order to be saved");
            Order savedOrder = orderRepository.save(order);

            System.out.println("order saved");
            BaseResponse response = new BaseResponse();
            if(savedOrder.getId()!=null){
                response.setCode(ConstantCode.SUCCESS);
                response.setMessage("Order created Successfully");
                response.setData(Map.of("order",savedOrder));
                return response;
            }

            response.setCode(ConstantCode.FAIL);
            response.setMessage("Order creation failed");
            response.setData(Map.of("order",savedOrder));
            return response;
        } catch (Exception e){
            System.out.println("Order failed");
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error creating order: " + e.getMessage(),
                    null
            );
        }



    }

    @Override
    public BaseResponse getOrderById(Long id) {
        try {
            BaseResponse response = new BaseResponse();
            Order order = orderRepository.findById(id);
            if(order.getId()!=null){
                response.setCode(ConstantCode.SUCCESS);
                response.setMessage("fetch order is sucessful");
                response.setData(Map.of("order",order));
                return response;
            }
            response.setCode(ConstantCode.FAIL);
            response.setMessage("fetch order failed");
            response.setData(null);
            return response;

        } catch (Exception e) {
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error Fetching order: " + e.getMessage(),
                    null

            );
        }

    }


    @Override
    public BaseResponse getAllOrders() {
        try{
            BaseResponse response = new BaseResponse();

            List<Order> orders =  orderRepository.findAll();
            if(!orders.isEmpty()){
                response.setCode(ConstantCode.SUCCESS);
                response.setMessage("Fetched all order");
                response.setData(Map.of("order",orders));
                return response;
            } else {
                response.setCode(ConstantCode.FAIL); // Or false, depending on your business logic
                response.setMessage("No orders found");
                response.setData(null); // Empty list
                return response;
            }
        } catch (Exception e) {
            System.out.println("Order failed");
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error fetching all orders: " + e.getMessage(),
                    null
            );
        }

    }

    @Override
    public BaseResponse updateOrder(Long id, Order order) {
        try{
            Order existing = orderRepository.findById(id);
            if (existing == null) return null;
            existing.setCustomer_name(order.getCustomer_name());
            existing.setOrder_date(order.getOrder_date());
            existing.setTotal_amount(order.getTotal_amount());
            existing.setItems(order.getItems());
            Order updatedOrder =  orderRepository.save(existing);
            BaseResponse response = new BaseResponse();

            response.setCode(ConstantCode.SUCCESS);
            response.setMessage("Order Updated");
            response.setData(Map.of("updatedOrder",updatedOrder));
            return response;
        } catch (Exception e) {
            System.out.println("Order update failed");
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error updating orders: " + e.getMessage(),
                    null
            );
        }

    }

    @Override
    public BaseResponse deleteOrder(Long id) {
        try{
            Order deletedOrder = orderRepository.deleteById(id);
            BaseResponse response = new BaseResponse();
            if (deletedOrder.getId() != null) {
                response.setCode(ConstantCode.SUCCESS);
                response.setMessage("Order Deleted");
                response.setData(Map.of("DeletedOrderID",deletedOrder.getId()));
                return response;
            }else {
                response.setCode(ConstantCode.FAIL); // Or false, depending on your business logic
                response.setMessage("No orders found");
                response.setData(null); // Empty list
                return response;
            }
        } catch (Exception e) {
            System.out.println("Order Delete failed");
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error Deleting orders: " + e.getMessage(),
                    null
            );
        }


    }


}
