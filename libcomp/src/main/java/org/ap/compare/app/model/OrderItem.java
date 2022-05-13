package org.ap.compare.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ap.compare.lib.annotations.ComparableNode;
import org.ap.compare.lib.annotations.PrimaryKey;

import java.util.Date;

@Data
@Builder
// jackson needs empty constructor
@NoArgsConstructor
// lombok builder needs an all args constructor.
// it generates automatically only if no other constructor is explicitly defined
@AllArgsConstructor
@ComparableNode
public class OrderItem {
    @PrimaryKey
    long orderItemId;
    long orderId;
    long customerId;
    long productId;
    long productQty;
    // TO add later
    // shipperDate;

    Product product;
}
