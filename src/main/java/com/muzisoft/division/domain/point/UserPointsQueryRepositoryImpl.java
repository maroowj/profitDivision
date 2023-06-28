package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsQueryRepository;
import com.muzisoft.division.web.api.dto.admin.profit.TotalPointsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;

import static com.muzisoft.division.domain.point.QUserPoints.userPoints;

public class UserPointsQueryRepositoryImpl extends QuerydslRepositorySupport implements UserPointsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserPointsQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserPoints.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public TotalPointsResponse totalPointsByUserDetailsAndPointsSupplier(UserDetails userDetails, PointSupplier pointSupplier) {
        return queryFactory.select(Projections.constructor(TotalPointsResponse.class,
                        userPoints.savedAmount.sum(),
                        userPoints.usedAmount.sum()
                ))
                .from(userPoints)
                .where(
                        userPoints.userDetails.eq(userDetails),
                        userPoints.supplier.eq(pointSupplier),
                        userPoints.usable.isTrue()
                )
                .fetchOne();
    }

    @Override
    public TotalPointsResponse totalPointsByUserDetailsAndPointsSupplierAndDateTo(UserDetails userDetails, PointSupplier pointSupplier, Date dateTo) {
        return queryFactory.select(Projections.constructor(TotalPointsResponse.class,
                        userPoints.savedAmount.sum(),
                        userPoints.usedAmount.sum()
                ))
                .from(userPoints)
                .where(
                        userPoints.userDetails.eq(userDetails),
                        userPoints.supplier.eq(pointSupplier),
                        userPoints.usable.isTrue(),
                        dateLt(dateTo)
                )
                .fetchOne();
    }

    @Override
    public long remainedTotalSavedAmount(Date dateTo, UserDetails userDetails) {
        return queryFactory.select(
                        new CaseBuilder()
                                .when(userPoints.savedAmount.sum().isNull())
                                .then(Expressions.constant(0))
                                .otherwise(userPoints.savedAmount.sum())
                )
                .from(
                        userPoints
                )
                .where(
                        userPoints.supplier.eq(PointSupplier.PROCEEDS),
                        userPoints.usable.isTrue(),
                        dateLt(dateTo),
                        findByUserDetails(userDetails)
                )
                .fetchOne();
    }

    @Override
    public long remainedTotalUsedAmount(Date dateTo, UserDetails userDetails) {
        return queryFactory.select(
                        new CaseBuilder()
                                .when(userPoints.usedAmount.sum().isNull())
                                .then(Expressions.constant(0))
                                .otherwise(userPoints.usedAmount.sum())
                )
                .from(
                        userPoints
                )
                .where(
                        userPoints.supplier.eq(PointSupplier.PROCEEDS),
                        userPoints.usable.isTrue(),
                        dateLt(dateTo),
                        findByUserDetails(userDetails)
                )
                .fetchOne();
    }

    @Override
    public long remainedTotalSavedAmountForUser(UserDetails userDetails) {
        return queryFactory.select(
                        new CaseBuilder()
                                .when(userPoints.savedAmount.sum().isNull())
                                .then(Expressions.constant(0))
                                .otherwise(userPoints.savedAmount.sum())
                )
                .from(
                        userPoints
                )
                .where(
                        userPoints.supplier.eq(PointSupplier.PROCEEDS).or(userPoints.supplier.eq(PointSupplier.INTEREST)),
                        userPoints.usable.isTrue(),
                        findByUserDetails(userDetails)
                )
                .fetchOne();
    }

    @Override
    public long remainedTotalUsedAmountForUser(UserDetails userDetails) {
        return queryFactory.select(
                        new CaseBuilder()
                                .when(userPoints.usedAmount.sum().isNull())
                                .then(Expressions.constant(0))
                                .otherwise(userPoints.usedAmount.sum())
                )
                .from(
                        userPoints
                )
                .where(
                        userPoints.supplier.eq(PointSupplier.PROCEEDS).or(userPoints.supplier.eq(PointSupplier.INTEREST)),
                        userPoints.usable.isTrue(),
                        findByUserDetails(userDetails)
                )
                .fetchOne();
    }


    private BooleanExpression dateLt(Date dateLt) {
        return dateLt != null ? userPoints.createdAt.lt(dateLt) : null;
    }

    private BooleanExpression findByUserDetails(UserDetails userDetails) {
        return userDetails != null ? userPoints.userDetails.eq(userDetails) : null;
    }
}
