package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.common.enums.ProfitLogType;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitDetailsPerUser;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitStatusFilter;
import com.muzisoft.division.web.api.dto.users.profit.ProfitLogListResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitTypeCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;

import static com.muzisoft.division.domain.profit.QProfitDetails.profitDetails;
import static com.muzisoft.division.domain.profit.QProfitDetailsLog.profitDetailsLog;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ProfitDetailsLogQueryRepositoryImpl extends QuerydslRepositorySupport implements ProfitDetailsLogQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProfitDetailsLogQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ProfitDetailsLog.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ProfitLogListResponse> profitLogList(Pageable pageable, ProfitTypeCondition profitTypeCondition, CommonCondition condition, UserDetails userDetails) {
        JPAQuery<ProfitLogListResponse> query = queryFactory.select(Projections.constructor(ProfitLogListResponse.class,
                    Expressions.constant(0),
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", profitDetailsLog.createdAt),
                    profitDetailsLog.content,
                    profitDetailsLog.amount,
                    profitDetailsLog.total,
                    profitDetailsLog.type
                ))
                .from(profitDetailsLog)
                .where(
                        profitDetailsLog.userDetails.eq(userDetails),
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo()),
                        searchByProfitStatusType(profitTypeCondition.getType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends ProfitDetailsLog> pathBuilder = new PathBuilder<ProfitDetailsLog>(profitDetailsLog.getType(), profitDetailsLog.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<ProfitLogListResponse> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate searchByProfitStatusType(ProfitStatusFilter type) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (type) {
            case WHOLE:
                builder.and(profitDetailsLog.type.eq(ProfitLogType.DEPOSIT).or(profitDetailsLog.type.eq(ProfitLogType.WITHDRAWAL)));
                break;
            case DEPOSIT:
                builder.and(profitDetailsLog.type.eq(ProfitLogType.DEPOSIT));
                break;
            case WITHDRAWAL:
                builder.and(profitDetailsLog.type.eq(ProfitLogType.WITHDRAWAL));
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? profitDetailsLog.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? profitDetailsLog.createdAt.lt(dateTo) : null;
    }

}
