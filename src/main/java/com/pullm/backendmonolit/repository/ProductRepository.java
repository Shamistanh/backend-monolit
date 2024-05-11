package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Product;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.models.response.ChartSingleResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            """
                         SELECT p.productType
                         FROM Product p 
                         INNER JOIN p.transaction t
                         WHERE t.user.id = :userId 
                         GROUP BY p.productType
                    """
    )
    List<String> getAllProductTypes(Long userId);


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


    @Query(
            """
            SELECT new com.pullm.backendmonolit.models.response.ChartSingleResponse(
                CONCAT(EXTRACT(YEAR FROM t.date), '-', EXTRACT(MONTH FROM t.date)),
              SUM(p.price))
            FROM
              Product p
            INNER JOIN p.transaction t WHERE t.user.id = :userId
            and p.productType = :productType
            GROUP BY
              EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
            """
    )
    List<ChartSingleResponse> getStatisticsDetailsByMonth(ProductType productType, Long userId);


    @Query(
            """
            SELECT new com.pullm.backendmonolit.models.response.ChartSingleResponse(
              CAST(EXTRACT(YEAR FROM t.date) AS STRING),
              SUM(p.price))
            FROM
              Product p
            INNER JOIN p.transaction t WHERE t.user.id = :userId
            and p.productType = :productType
            GROUP BY
              CAST(EXTRACT(YEAR FROM t.date) AS STRING)
            """
    )
    List<ChartSingleResponse> getStatisticsDetailsByYear(ProductType productType, Long userId);


    @Query(
            """
            SELECT new com.pullm.backendmonolit.models.response.ChartSingleResponse(
              CONCAT(EXTRACT(YEAR FROM t.date), '-', EXTRACT(MONTH FROM t.date), '-',EXTRACT(DAY FROM t.date)),
              SUM(p.price))
            FROM
              Product p
            INNER JOIN p.transaction t WHERE t.user.id = :userId
            and p.productType = :productType
            GROUP BY
              EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date), EXTRACT(DAY FROM t.date)
            """
    )
    List<ChartSingleResponse> getStatisticsDetailsByDay(ProductType productType, Long userId);






}
