/*header*/
$('.header-wrap .header-inner .nav-wrap .nav > li').mouseenter(function(){
    $(this).children('.depth2').css('display','block');
    $(this).children('a').css('color','#b58408');
});

$('.header-wrap .header-inner .nav-wrap .nav > li').mouseleave(function(){
    $(this).children('.depth2').css('display','none');
    $(this).children('a').css('color','#000');
});



$('.user-wrap > li').mouseenter(function(){
    $(this).children('.depth2').css('display','block');
    $(this).children('a').css('color','#b58408');
});

$('.user-wrap > li').mouseleave(function(){
    $(this).children('.depth2').css('display','none');
    $(this).children('a').css('color','#888');
});


/*모바일 메뉴*/
$('.m-nav-wrap .nav-wrap > li.hasDep2').click(function(){
    if($(this).hasClass('actived')){
        $(this).removeClass('actived');
        $(this).children('.depth2').slideUp();
    }else{
        $(this).addClass('actived');
        $(this).children('.depth2').slideDown();
    }
});

$('.m-header-wrap .info-wrap .nav-btn a').click(function(){
   $('.m-nav-wrap').css('display','block'); 
});

$('.m-nav-wrap .nav-logo-wrap .close-btn').click(function(){
   $('.m-nav-wrap').css('display','none'); 
});


/*모달 공통사항*/
$('.close-btn, .modal-bg').click(function(){
   $('.modal').css('display','none'); 
});


/*회원가입 join*/
//전체 약관 동의 check
$('.join-agree-wrap .all-agree label').click(function(){
    if($(this).children('input').is(":checked")){
        $('.join-agree-wrap .agree-text-wrap label input').prop("checked",true);
    }else{
       $('.join-agree-wrap .agree-text-wrap label input').prop("checked",false); 
    }
});


/*search-wrap 기간*/
$('.period-wrap li').click(function(){
   $('.period-wrap li').removeClass('actived');
    $(this).addClass('actived');
});


/*table tr sort*/
$('.page-table1 tr th p').click(function(){
   $(this).toggleClass('actived'); 
});


/*자주묻는글*/
$('.qnaWrap .qnaBox li a').click(function(){
    if($(this).hasClass('actived')){
           $(this).removeClass('actived'); 
           $(this).siblings('.answer').slideUp(); 
       }else{
           $('.qnaWrap .qnaBox li a').removeClass('actived');
           $(this).addClass('actived');
           $('.qnaWrap .qnaBox li .answer').slideUp();
           $(this).siblings('.answer').slideDown(); 
       }
   
});









