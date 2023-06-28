package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.common.enums.AccountBookType;
import com.muzisoft.division.utils.converter.AccountBookTypeConverter;
import com.muzisoft.division.utils.manager.SeqManager;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class AccountBook extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_account_book")
    @GenericGenerator(name = "seq_manager_account_book", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "actb_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

//    @Convert(converter = AccountBookTypeConverter.class)
//    @Column(nullable = false, columnDefinition = "tinyint(1)")
//    private AccountBookType type; // 0: 수익 / 1: 지출

    @Column
    private int year;

    @Type(type = "json")
    @Column(columnDefinition = "JSON")
    private Map<String, Object> contents;

//    @Column(nullable = false)
//    private long amount;
//
//    @Column(nullable = false)
//    private String purpose;



    @Column(columnDefinition = "text")
    private String etc;

    public static AccountBook create(int year, Map<String, Object> contents, String etc) {
        AccountBook accountBook = new AccountBook();
        accountBook.setYear(year);
        accountBook.setContents(contents);
        accountBook.setEtc(etc);
        return accountBook;
    }

    public void update(int year, Map<String, Object> contents, String etc) {
        setYear(year);
        setContents(contents);
        setEtc(etc);
    }
}
