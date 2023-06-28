package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.dues.Dues;
import com.muzisoft.division.web.api.dto.admin.popup.PopupCondition;
import com.muzisoft.division.web.api.dto.admin.popup.PopupListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.PopupExposureType;
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
import static com.muzisoft.division.domain.dues.QDues.dues;
import static com.muzisoft.division.domain.point.QWithdrawal.withdrawal;

public class PopupQueryRepositoryImpl extends QuerydslRepositorySupport implements PopupQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PopupQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Popup.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PopupListResponse> popupList(Pageable pageable, PopupCondition popupCondition, CommonCondition condition) {
        JPAQuery<PopupListResponse> query = queryFactory.select(Projections.constructor(PopupListResponse.class,
                        popup.seq,
                        popup.mainTitle,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", popup.exposureStart),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %T')", popup.exposureEnd),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", popup.createdAt),
                        popup.exposure
                ))
                .from(popup)
                .where(
                        popup.mainTitle.contains(condition.getQuery()),
                        filterByExposure(popupCondition.getExposure())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            PathBuilder<? extends Popup> pathBuilder = new PathBuilder<Popup>(popup.getType(), popup.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        QueryResults<PopupListResponse> result= query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private Predicate filterByExposure(PopupExposureType exposureType) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (exposureType) {
            case WHOLE:
                return null;
            case SHOW:
                builder.and(popup.exposure.isTrue());
                break;
            case HIDE:
                builder.and(popup.exposure.isFalse());
                break;
        }
        return builder;
    }

    private BooleanExpression dateGoe(Date dateFrom) {
        return dateFrom != null ? popup.createdAt.goe(dateFrom) : null;
    }

    private BooleanExpression dateLt(Date dateTo) {
        return dateTo != null ? popup.createdAt.lt(dateTo) : null;
    }
}
