package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Product;
import com.pullm.backendmonolit.models.response.ChartResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            """
                         SELECT new com.pullm.backendmonolit.models.response.ChartResponse(p.productType, SUM(p.price))
                         FROM Product p
                         INNER JOIN p.transaction t
                         WHERE t.user.id = :userId
                         AND t.date BETWEEN :startDate AND :endDate
                         GROUP BY p.productType
                    """
    )
    List<ChartResponse> getAllChartResponse(LocalDate startDate, LocalDate endDate, Long userId);
}
