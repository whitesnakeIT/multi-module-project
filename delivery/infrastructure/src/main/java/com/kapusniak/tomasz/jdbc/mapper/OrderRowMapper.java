package com.kapusniak.tomasz.jdbc.mapper;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

        Order order = new Order();
        order.setId(rs.getLong("ORDER_ID"));
        order.setPackageType(PackageType.valueOf(rs.getString("PACKAGE_TYPE")));
        order.setPackageSize(PackageSize.valueOf(rs.getString("PACKAGE_SIZE")));
        order.setSenderAddress(rs.getString("SENDER_ADDRESS"));
        order.setReceiverAddress(rs.getString("RECEIVER_ADDRESS"));
        order.setPreferredDeliveryDate(rs.getDate("PREFERRED_DELIVERY_DATE").toLocalDate());

        return order;
    }
}
