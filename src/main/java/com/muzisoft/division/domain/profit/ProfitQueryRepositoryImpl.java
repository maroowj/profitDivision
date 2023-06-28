package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.board.Popup;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.popup.PopupListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.LastProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitDetailsPerUser;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
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

import static com.muzisoft.division.domain.board.QPopup.popup;
import static com.muzisoft.division.domain.profit.QProfit.profit;
import static com.muzisoft.division.domain.profit.QProfitDetails.profitDetails;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class ProfitQueryRepositoryImpl extends QuerydslRepositorySupport implements ProfitQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProfitQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Profit.class);
        this.queryFactory = queryFactory;
    }


    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? profit.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? profit.createdAt.lt(dateTo) : null;
    }


    @Override
    public Page<LastProfitListResponse> lastProfitList(Pageable pageable, CommonCondition condition) {
        JPAQuery<LastProfitListResponse> query = queryFactory.select(Projections.constructor(LastProfitListResponse.class,
                    profit.seq,
                    profit.amount,
                    profit.yield,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", profit.createdAt)
                ))
                .from(profit)
                .where(
                        dateGoe(condition.getDateFrom()),
                        dateLt(condition.getDateTo())
                );

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Profit> pathBuilder = new PathBuilder<Profit>(profit.getType(), profit.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<LastProfitListResponse> result= query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
