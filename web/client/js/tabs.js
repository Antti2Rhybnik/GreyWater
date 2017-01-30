$(function() {
        $('#sensors').show();

        $('#items li').click(function() {
            if ($(this).attr('class') == 'active') {
                return false;
            }
            var link = $(this).children().attr('href'); // ссылка на текст вкладки для показа
            var prevActive = $('li.active').children().attr('href'); // ссылка на текст пока что активной вкладки
            $('li.active').removeClass('active');
            $(this).addClass('active');
            // скрываем/показываем текст вкладок
            $(prevActive).fadeOut(500, function() {
                $(link).fadeIn();
                // изменяем цвета
                $('#items li').css('background', '#ccc');
                $('li.active').css('background', '#f3f3f3');
            });
            return false;
        });
    });
