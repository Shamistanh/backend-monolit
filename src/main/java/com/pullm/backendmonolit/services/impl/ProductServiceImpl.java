package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.criteria.DateCriteria;
import com.pullm.backendmonolit.models.request.ProductRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.models.response.PopularProductResponse;
import com.pullm.backendmonolit.repository.ProductRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public List<ChartResponse> getAllChartResponse(DateCriteria dateCriteria) {
        log.info("getAllChartResponse().start DateCriteria: {} ", dateCriteria);

        Long userId = getUser().getId();
        List<ChartResponse> allChartResponse =
                productRepository.getAllChartResponse(dateCriteria.getToDate(), dateCriteria.getFromDate(), userId);
        log.info("getAllChartResponse().end");

        return allChartResponse;
    }

    public void updateProduct(Long id, ProductRequest productRequest) {
        log.info("updateProduct().start id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found by id " + id));

        product.setName(productRequest.getName());
        product.setProductSubType(productRequest.getProductSubType());
        product.setCount(productRequest.getCount());
        product.setPrice(productRequest.getPrice());
        product.setWeight(productRequest.getWeight());

        log.info("updateProduct().end id: {}", id);

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.info("deleteProduct().start id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found by id " + id));

        productRepository.delete(product);
        log.info("deleteProduct().end id: {}", id);
    }


    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    public List<PopularProductResponse> getPopularProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.getPopularProduct(getUser(), pageable);
    }

}
