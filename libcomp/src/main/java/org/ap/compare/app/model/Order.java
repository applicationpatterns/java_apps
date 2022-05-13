package org.ap.compare.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ap.compare.lib.annotations.ComparableNode;
import org.ap.compare.lib.annotations.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
// jackson needs empty constructor
@NoArgsConstructor
// lombok builder needs an all args constructor.
// it generates automatically only if no other constructor is explicitly defined
@AllArgsConstructor
@ComparableNode
public class Order {
    @PrimaryKey
    long orderId;
    long customerId;
    Date date;
    // TO add later
    // shipperDate;

    List<OrderItem> orderItemList;
}
