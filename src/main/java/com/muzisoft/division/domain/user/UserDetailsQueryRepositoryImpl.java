package com.muzisoft.division.domain.user;



import com.muzisoft.division.domain.common.enums.Role;
import com.muzisoft.division.web.api.dto.admin.member.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.UserSearchType;
import com.muzisoft.division.web.api.dto.common.enums.UserWithdrawalType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.muzisoft.division.domain.user.QGrade.grade;
import static com.muzisoft.division.domain.user.QUser.user;
import static com.muzisoft.division.domain.user.QUserAccount.userAccount;
import static com.muzisoft.division.domain.user.QUserDetails.userDetails;

public class UserDetailsQueryRepositoryImpl extends QuerydslRepositorySupport implements UserDetailsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserDetailsQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserDetails.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UserListResponse.UserList> userList(Pageable pageable, UserCondition userCondition, CommonCondition condition) {
        JPAQuery<UserListResponse.UserList> query = queryFactory.select(Projections.constructor(UserListResponse.UserList.class,
                    userDetails.membershipNumber,
                    userDetails.seq,
                    user.userId,
                    userDetails.name,
                    userDetails.birth,
                    userDetails.mobile,
                    userDetails.email,
                    userDetails.address,
                    userDetails.investment,
                    Expressions.constant("-"),
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", userDetails.createdAt),
                    Projections.constructor(UserListResponse.Grade.class,
                            grade.seq,
                            grade.title),
                    user.withdrawal
                ))
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(grade).on(userDetails.grade.eq(grade))
                .where(
                        searchBySearchTypeAndKeyword(userCondition.getQueryType(), condition.getQuery()),
                        searchByWithdrawal(userCondition.getWithdrawal()),
                        userDetails.accepted.eq(1),
                        user.roles.contains(Role.USER),
                        user.roles.contains(Role.ADMIN).not()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<UserListResponse.UserList> result = query.fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public int userCount() {
        return queryFactory.select(
                        userDetails.count().intValue()
                )
                .from(userDetails)
                .where(
                        userDetails.accepted.eq(1),
                        userDetails.membershipNumber.ne("0000")
                )
                .fetchOne();
    }

    @Override
    public int countByGrade(Grade grade) {
        return queryFactory.select(
                        userDetails.count().intValue()
                )
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        userDetails.accepted.eq(1),
                        userDetails.grade.eq(grade),
                        user.withdrawal.isFalse(),
                        userDetails.membershipNumber.ne("0000")
                )
                .fetchOne();
    }

    @Override
    public UserDetailsResponseInAdmin userDetailsInAdmin(String userDetailsSeq) {

        QUser recommender = new QUser("recommender");

        return queryFactory.select(Projections.constructor(UserDetailsResponseInAdmin.class,
                    userDetails.membershipNumber,
                    userDetails.seq,
                    userDetails.name,
                    Projections.constructor(UserDetailsResponseInAdmin.Grade.class,
                            grade.seq,
                            grade.title),
                    Expressions.constant("-"),
                    user.userId,
                    userDetails.birth,
                    userDetails.mobile,
                    userDetails.email,
                    userDetails.address,
                    userAccount.bankName,
                    userAccount.accountNumber,
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", userDetails.createdAt),
                    recommender.userId,
                    user.withdrawal,
                    user.withdrawalReason
                ))
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(userAccount).on(userAccount.userDetails.eq(userDetails))
                .leftJoin(grade).on(userDetails.grade.eq(grade))
                .leftJoin(recommender).on(userDetails.recommender.eq(recommender))
                .where(
                        userDetails.seq.eq(userDetailsSeq)
                )
                .fetchOne();
    }

    @Override
    public Page<WaitingListResponse> waitingList(Pageable pageable, CommonCondition cond) {
        QUser recommender = new QUser("recommender");

        JPAQuery<WaitingListResponse> query = queryFactory.select(Projections.constructor(WaitingListResponse.class,
                        userDetails.seq,
                        user.userId,
                        userDetails.name,
                        userDetails.birth,
                        userDetails.mobile,
                        userDetails.email,
                        userDetails.address,
                        recommender.userId,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", userDetails.createdAt),
                        userDetails.accepted
                ))
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(grade).on(userDetails.grade.eq(grade))
                .leftJoin(recommender).on(userDetails.recommender.eq(recommender))
                .where(
                        userDetails.accepted.ne(1)
                        .and(user.userId.contains(cond.getQuery()).or(userDetails.name.contains(cond.getQuery())))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for(Sort.Order o : pageable.getSort()) {
            if(o.getProperty().equals("userId")) {
                PathBuilder<? extends User> pathBuilder = new PathBuilder<User>(user.getType(), user.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }else {
                PathBuilder<? extends UserDetails> pathBuilder = new PathBuilder<UserDetails>(userDetails.getType(), userDetails.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        QueryResults<WaitingListResponse> result = query.fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public WaitingUserDetailsResponse.User waitingUserDetails(String userDetailsSeq) {
        QUser recommender = new QUser("recommender");

        return queryFactory.select(Projections.constructor(WaitingUserDetailsResponse.User.class,
                        userDetails.seq,
                        userDetails.accepted,
                        user.userId,
                        userDetails.name,
                        userDetails.birth,
                        userDetails.mobile,
                        userDetails.email,
                        userDetails.address,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", userDetails.createdAt),
                        recommender.userId,
                        recommender.seq
                ))
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(recommender).on(userDetails.recommender.eq(recommender))
                .where(
                        userDetails.seq.eq(userDetailsSeq)
                )
                .fetchOne();
    }

    @Override
    public WaitingUserDetailsResponse.Recommender waitingUserRecommenderDetails(String userDetailsSeq) {
        QUser recommender = new QUser("recommender");

        return queryFactory.select(Projections.constructor(WaitingUserDetailsResponse.Recommender.class,
                        grade.title,
                        Expressions.constant("-"),
                        userDetails.membershipNumber,
                        user.userId,
                        userDetails.name,
                        userDetails.birth,
                        userDetails.mobile,
                        userDetails.email,
                        userDetails.address,
                        userAccount.bankName,
                        userAccount.accountNumber,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", userDetails.createdAt),
                        recommender.userId,
                        user.withdrawal
                ))
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .leftJoin(userAccount).on(userAccount.userDetails.eq(userDetails))
                .leftJoin(grade).on(userDetails.grade.eq(grade))
                .leftJoin(recommender).on(userDetails.recommender.eq(recommender))
                .where(
                        userDetails.seq.eq(userDetailsSeq)
                )
                .fetchOne();
    }

    @Override
    public List<UserDetails> userDetailsList() {
        return queryFactory.select(
                userDetails
                )
                .from(userDetails)
                .leftJoin(user).on(user.userDetails.eq(userDetails))
                .where(
                        user.withdrawal.isFalse(),
                        userDetails.accepted.eq(1),
                        user.roles.contains(Role.USER),
                        user.roles.contains(Role.ADMIN).not()
                )
                .fetch();
    }

    private Predicate searchBySearchTypeAndKeyword(UserSearchType queryType, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (queryType) {
            case WHOLE :
                builder.and(userDetails.name.contains(query).or(user.userId.contains(query)));
                break;
            case USER_NAME:
                builder.and(userDetails.name.contains(query));
                break;
            case USER_ID:
                builder.and(user.userId.contains(query));
                break;
        }
        return builder;
    }

    private Predicate searchByWithdrawal(UserWithdrawalType withdrawalType) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (withdrawalType) {
            case WHOLE :
                return null;
            case REGISTER:
                builder.and(user.withdrawal.isFalse());
                break;
            case WITHDRAWAL:
                builder.and(user.withdrawal.isTrue());
                break;
        }
        return builder;
    }
}
