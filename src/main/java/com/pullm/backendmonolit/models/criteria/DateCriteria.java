package com.pullm.backendmonolit.models.criteria;

import com.pullm.backendmonolit.enums.DateRange;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DateCriteria {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    private DateRange dateRange;
}
