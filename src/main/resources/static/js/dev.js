function toastModal(txt, sec) {
    let secs = 1000;
    if(sec == undefined || sec == null) secs = 1000;
    else secs = sec;

    const newDiv = document.createElement('div');
    const newText = document.createTextNode(txt);

    newDiv.appendChild(newText);

    $(newDiv).css({"position":"fixed","top":"49%","left":"50%","transform":"translateX(-50%)","background-color":"#276cad","padding":"12px 30px","border-radius":"20px","color":"#fff","z-index":9999999});

    document.body.appendChild(newDiv);

    setTimeout(function () {
        $(newDiv).animate({
            opacity: "hide"
        });
    }, secs);
}

function fMobilePagination(result, page, div) {
    let num = "<div  class='page-num'>";
    let reNum = parseInt(result.number / 5);
    let pre = "";
    let next = "";
    let first = "";
    let last = "";

    // 이전버튼 (5페이지씩 이동, 처음으로)
    if (reNum >= 1 && page > 5) {
        pre = "<a gopage='" + (reNum * 5) + "' class='page-arrow2 page-arrow'>이전 게시물 목록</a>";
        first = "<a gopage='1' class='page-arrow1 page-arrow pc-mr05'>처음 게시물 목록</a>";
    }

    // 다음버튼 (5페이지씩 이동, 마지막으로)
    if (parseInt((result.totalPages - 1) / 5) != reNum && result.totalPages > 5) {
        next = "<a gopage='" + ( reNum * 5 + 6 ) + "' class='page-arrow3 page-arrow pc-mr05'>다음 게시물 목록</a>";
        last = "<a gopage='" + result.totalPages + "' class='page-arrow4 page-arrow'>마지막 게시물 목록</a>";
    }

    for ( let idx = 1; idx <= result.totalPages; idx++ ) {
        if ( idx <= reNum * 5 + 5 && idx > reNum * 5 ) {
            if (idx == page) {
                num += "<a gopage=" + idx + " class='actived'>" + idx + "</a>";
            } else {
                num += "<a gopage=" + idx + ">" + idx + "</a>";
            }
        }
    }
    num += "</div>";

    if( div == undefined ) $("#pagination").html(first + pre + num + next + last);
    else $(div).html(first + pre + num + next + last);
}

function fPagination2(result, page, div) {
    let num = "<div  class='page-num'>";
    let reNum = parseInt(result.number / 10);
    let pre = "";
    let next = "";
    let first = "";
    let last = "";

    // 이전버튼 (10페이지씩 이동, 처음으로)
    if (reNum >= 1 && page > 10) {
        pre = "<a gopage='" + (reNum * 10) + "' class='page-arrow2 page-arrow'>이전 게시물 목록</a>";
        first = "<a gopage='1' class='page-arrow1 page-arrow pc-mr05'>처음 게시물 목록</a>";
    }

    // 다음버튼 (10페이지씩 이동, 마지막으로)
    if (parseInt((result.totalPages - 1) / 10) != reNum && result.totalPages > 10) {
        next = "<a gopage='" + ( reNum * 10 + 11 ) + "' class='page-arrow3 page-arrow pc-mr05'>다음 게시물 목록</a>";
        last = "<a gopage='" + result.totalPages + "' class='page-arrow4 page-arrow'>마지막 게시물 목록</a>";
    }

    for ( let idx = 1; idx <= result.totalPages; idx++ ) {
        if ( idx <= reNum * 10 + 10 && idx > reNum * 10 ) {
            if (idx == page) {
                num += "<a gopage=" + idx + " class='actived'>" + idx + "</a>";
            } else {
                num += "<a gopage=" + idx + ">" + idx + "</a>";
            }
        }
    }
    num += "</div>";

    if( div == undefined ) $("#pagination").html(first + pre + num + next + last);
    else $(div).html(first + pre + num + next + last);
}

function fPagination(data, page) {
    let num = "";
    let reNum = parseInt(data.number / 10);
    let pre = "";
    let next = "";

    // 이전버튼 (5페이지씩 이동, 처음으로)
    if (reNum >= 1 && page > 10) {
        pre = "<a gopage='" + (reNum * 10) + "'><i id='pre' class='icon-green-arrow-left'></i></a>";
    }

    // 다음버튼 (5페이지씩 이동, 마지막으로)
    if (parseInt((data.totalPages - 1) / 10) != reNum && data.totalPages > 10) {
        next = "<a gopage='" + ( reNum * 10 + 11 ) + "'><i id='next' class='icon-green-arrow-left'></i></a>";
    }

    num += "<div class=\"page-num\">";
    for ( let idx = 1; idx <= data.totalPages; idx++ ) {
        if ( idx <= reNum * 10 + 10 && idx > reNum * 10 ) {
            if (idx == page) {
                num += "<a class='num actived' gopage=" + idx + ">" + idx + "</a>";
            } else {
                num += "<a class='num' gopage=" + idx + ">" + idx + "</a>";
            }
        }
    }
    num += "</div>";
    $("#pagination").html(pre + num + next);
}

function fModalPagination(result, page, modal) {
    let num = "<div class='page-num'>";
    let reNum = parseInt(result.number / 10);
    let pre = "";
    let next = "";
    let first = "";
    let last = "";

    // 이전버튼 (5페이지씩 이동, 처음으로)
    if (reNum >= 1 && page > 10) {
        pre = "<a gopage='" + (reNum * 10) + "' class='page-arrow2 page-arrow'>이전 게시물 목록</a>";
        first = "<a gopage='1' class='page-arrow1 page-arrow pc-mr05'>처음 게시물 목록</a>";
    }

    // 다음버튼 (5페이지씩 이동, 마지막으로)
    if (parseInt((result.totalPages - 1) / 10) != reNum && result.totalPages > 10) {
        next = "<a gopage='" + ( reNum * 10 + 11 ) + "' class='page-arrow3 page-arrow pc-mr05'>다음 게시물 목록</a>";
        last = "<a gopage='" + result.totalPages + "' class='page-arrow4 page-arrow'>마지막 게시물 목록</a>";
    }

    for ( let idx = 1; idx <= result.totalPages; idx++ ) {
        if ( idx <= reNum * 10 + 10 && idx > reNum * 10 ) {
            if (idx == page) {
                num += "<a gopage=" + idx + " class='actived'>" + idx + "</a>";
            } else {
                num += "<a gopage=" + idx + ">" + idx + "</a>";
            }
        }
    }
    num += "</div>";
    $("#"+modal).find(".modalPagination").html(first + pre + num + next + last);
}

// 천단위 콤마
function fcomma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

// 천단위 콤마 제거
function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, '');
}

function fOpenSelectBox(div){
    if ( div.parent('.selectBox').hasClass('active') ) {
        div.parent('.selectBox').removeClass('active');
    } else {
        div.parent('.selectBox').addClass("active");
    }
}

function fSelect(div){
    let li_value = div.text();
    let li_min = div.attr("min");
    let li_seq = div.attr("seq");

    div.parent(".optionList").siblings('.label').text(li_value);
    div.parent(".optionList").siblings('.label').attr("min", li_min);
    div.parent(".optionList").siblings('.label').attr("seq", li_seq);
    div.parents('.selectBox').removeClass("active");
}

// string/yyyy-MM -> dateTo format
function stringToFormat(string){
    let year=string.substr(0, 4);
    let month=string.substr(5, 2);
    if(month.length === 1) month='0'+month;

    let lastDay=new Date(year, month, 0);

    let day=lastDay.getDate();

    if(day.length === 1) day='0'+day;
    return year+'-'+month+'-'+day;
}

function getDayOfWeek(day){ //날짜문자열 yyyy-mm-dd
    let week = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
    let dayOfWeek = week[new Date(day).getDay()];
    return dayOfWeek;
}