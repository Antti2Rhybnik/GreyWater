function initGraph() { //не нужна
    for (var i = 0; i < maxGraph; i++)
        $("div.graph" + i).slideUp();
}

//закрытие панели и убирние второго графика
function graph2Off() {
    panelOpen = !panelOpen;
    if (panelOpen) {
        $("div.graph1").slideUp();
        $("#strelka").attr("src", "img/strelka-2.png")
    } else {
        $("#strelka").attr("src", "img/strelka.png")
        if (sensorsOnGraph.length == maxGraph)
            $("div.graph1").slideDown();
    }
}

//чекбоксы, блок на добавление графиков
(function($) {
    $.fn.tzCheckbox = function(options) {
        // Default On / Off labels:
        options = $.extend({
            labels: ['ON', 'OFF']
        }, options);

        return this.each(function() {
            var originalCheckBox = $(this),
                labels = [];
            // Checking for the data-on / data-off HTML5 data attributes:
            if (originalCheckBox.data('on')) {
                labels[0] = originalCheckBox.data('on');
                labels[1] = originalCheckBox.data('off');
            } else labels = options.labels;

            // Creating the new checkbox markup:
            var checkBox = $('<span>', {
                className: 'tzCheckBox ' + (this.checked ? 'checked' : ''),
                html: '<span class="tzCBContent">' + labels[this.checked ? 0 : 1] +
                    '</span><span class="tzCBPart"></span>'
            });
            // Inserting the new checkbox, and hiding the original:
            checkBox.insertAfter(originalCheckBox.hide());
            checkBox.click(function() {
                //нельзя выбрать слишком много checkbox на график
                if (originalCheckBox.attr("value").search("graph") != -1) {
                    var checked = checkBox.hasClass('checked');
                    getValues();
                    if (sensorsOnGraph.length >= maxGraph && !checked)
                        return;
                }
                checkBox.toggleClass('checked');
                var isChecked = checkBox.hasClass('checked');
                // Synchronizing the original checkbox:
                originalCheckBox.attr('checked', isChecked);
                checkBox.find('.tzCBContent').html(labels[isChecked ? 0 : 1]);
                getValues();
                for (var i = 0; i < maxGraph; i++)
                    $("div.graph" + i).slideUp();
                //TODO загрузить данные датчиков в графики по порядку.
                drawSensorGraph();
                for (var i = 0; i < sensorsOnGraph.length; i++) {
                    $("div.graph" + i).slideDown();
                }
            });
            // Listening for changes on the original and affecting the new one:
            originalCheckBox.bind('change', function() {
                checkBox.click();
            });
        });
    };
})(jQuery);
