package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.manager.SeqManager;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Board extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_board")
    @GenericGenerator(name = "seq_manager_board", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "bord_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", nullable = false)
    private UserDetails writer;

    @OneToOne(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attach")
    private EFileManager attach;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String contents;

    @Column(nullable = false)
    private boolean fixed;

    @Column(nullable = false)
    private int boardNo;


    public static Board create(UserDetails writer, String title, String contents, boolean fixed, EFileManager attach, int boardNo) {
        Board board = new Board();
        board.setWriter(writer);
        board.setTitle(title);
        board.setContents(contents);
        board.setFixed(fixed);
        board.setAttach(attach);
        board.setBoardNo(boardNo);
        return board;
    }

    public void update(UserDetails writer, String title, String contents, boolean fixed, EFileManager attach, boolean attachPresent) {
        /*if(!FileManagerUtils.equals(getAttach(), attach)) {
            if(attach!=null) {
                setAttach(attach);
            }else {
                if(attachPresent==false) {
                    setAttach(null);
                }
            }
        }*/
        if(!FileManagerUtils.equals(getAttach(), attach)) {
            if (attach != null) {
                setAttach(attach);
            }
        }

        setWriter(writer);
        setTitle(title);
        setContents(contents);
        setFixed(fixed);
    }
}
