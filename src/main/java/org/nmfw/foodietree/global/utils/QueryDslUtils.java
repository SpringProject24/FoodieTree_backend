package org.nmfw.foodietree.global.utils;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;

import java.time.LocalDateTime;
import org.nmfw.foodietree.domain.reservation.entity.QReservationSubSelect;

public class QueryDslUtils {

    public static Expression<Integer> getCurrProductCntExpression(QProduct p, QReservationSubSelect r) {
        NumberExpression<Integer> currProductCnt = new CaseBuilder()
                .when(p.pickupTime.gt(LocalDateTime.now())
                        .and(r.reservationTime.isNull()
                                .or(r.paymentTime.isNull().and(r.reservationTime.lt(LocalDateTime.now().minusMinutes(5))))
                        ))
                .then(1)
                .otherwise(0)
                .sum();

        return ExpressionUtils.as(currProductCnt, "currProductCnt");
    }
}
