//왼쪽메뉴 접기
$('.menuSize-btn').click(function () {
    $('.admin-leftMenu-wrap').toggleClass('foldMenu');
    $('.admin').toggleClass('foldAdmin');
});


//depth2메뉴
$('.admin-leftMenu-wrap .navList > li').click(function () {

    if ($(this).hasClass('actived')) {
        $(this).removeClass('actived');
        $(this).children('.depth2').slideUp(100);
    } else {
        $('.admin-leftMenu-wrap .navList > li').removeClass('actived');
        $('.admin-leftMenu-wrap .navList > li').children('.depth2').slideUp(100);
        $(this).addClass('actived');
        $(this).children('.depth2').slideDown(100);
    }
});
