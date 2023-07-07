package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.ResourceNotFoundException;
import com.pullm.backendmonolit.models.request.ProductRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.repository.ProductRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Override
    public List<ChartResponse> getAllChartResponse(LocalDate startDate, LocalDate endDate) {
        log.info("getAllChartResponse().start startDate: {}  endDate: {} ", startDate, endDate);
        Long userId = getUser().getId();
        List<ChartResponse> allChartResponse = productRepository.getAllChartResponse(startDate, endDate, userId);
        log.info("getAllChartResponse().end");

        return allChartResponse;
    }

    @Override
    public void updateProduct(Long id, ProductRequest productRequest) {
        log.info("updateProduct().start id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found by id " + id));

        product.setName(productRequest.getName());
        product.setProductSubType(productRequest.getProductSubType());
        product.setCount(productRequest.getCount());
        product.setPrice(productRequest.getPrice());
        product.setWeight(productRequest.getWeight());

        log.info("updateProduct().end id: {}", id);

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("deleteProduct().start id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found by id " + id));

        productRepository.delete(product);
        log.info("deleteProduct().end id: {}", id);
    }


    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new UsernameNotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }
}
