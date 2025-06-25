package org.example.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.example.common.BaseResponse;
import org.example.model.Order;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;



    @PostMapping("/postorder")
    public BaseResponse createOrder(@RequestBody Order order) {

            return orderService.createOrder(order);
            

    }

    @GetMapping("/{id}")
    public BaseResponse getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/getAll")
    public BaseResponse getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}")
    public BaseResponse updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/cancel/{id}")
    public BaseResponse deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);

    }
    @GetMapping("/greets")
    public  String    greet(HttpServletRequest request){
        return "hello this is greeting "+ request.getSession().getId();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = orderService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }


    }