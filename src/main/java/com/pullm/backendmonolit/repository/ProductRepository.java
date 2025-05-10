package com.pullm.backendmonolit.repository;

import com.pullm.backendmonolit.entities.Product;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.models.response.ChartSingleResponse;
import com.pullm.backendmonolit.models.response.PopularProductResponse;
import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
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
                         and t.date >= :startDate
                         and t.date <= :endDate
                         GROUP BY p.productType
                    """
    )
    List<String> getAllProductTypes(Long userId, LocalDateTime startDate, LocalDateTime endDate);


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
    List<ChartResponse> getAllChartResponse(LocalDateTime startDate, LocalDateTime endDate, Long userId);


    @Query(
            """
            SELECT new com.pullm.backendmonolit.models.response.ChartSingleResponse(
                CONCAT(EXTRACT(YEAR FROM t.date), '-', EXTRACT(MONTH FROM t.date)),
              SUM(p.price))
            FROM
              Product p
            INNER JOIN p.transaction t WHERE t.user.id = :userId
            and p.productType = :productType
            and t.date IS NOT NULL       
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

    @Query("""
        select new com.pullm.backendmonolit.models.response.PopularProductResponse(p.name, p.productType, count(p))
        from Transaction t
        join t.products p
        where t.user = :user
        group by p.name, p.productType
        order by count(p) desc
        """)
    List<PopularProductResponse> getPopularProduct(User user, Pageable pageable);


    @Query("""
                    SELECT new com.pullm.backendmonolit.models.response.StatisticsProductResponse(
                t.date,
                p.name,
                p.price,
                p.weight,      
                p.count, 
                p.productType,          
                p.productSubType)
                    FROM
                Transaction t
                    RIGHT JOIN
                Product p
            ON
                t.id = p.transaction.id
                    WHERE
                t.user = :user
                AND p.productType = :productType
                AND (p.productSubType = :productSubType OR :productSubType IS NULL OR :productSubType = '')
                    ORDER BY
                t.date
                
            """)
    List<StatisticsProductResponse> findAllByProductTypeAndSubtype(ProductSubType productSubType,
                                                                   ProductType productType, User user);






}
