function _UpdateTableHeadersScroll() {
    $("div.divtftable").each(function() {
        var originalHeaderRow = $(".tableFloatingHeaderOriginal", this);
        var floatingHeaderRow = $(".tableFloatingHeader", this);
        var offset = $(this).offset();
        var scrollTop = $(window).scrollTop();
        // check if floating header should be displayed
        if ((scrollTop > offset.top) && (scrollTop < offset.top + $(this).height() - originalHeaderRow.height())) {
            floatingHeaderRow.css("visibility", "visible");
            floatingHeaderRow.css("left", -$(window).scrollLeft());
        }
        else {
            floatingHeaderRow.css("visibility", "hidden");
        }
    });
}


function _UpdateTableHeadersResize() {
    $("div.divtftable").each(function() {
        var originalHeaderRow = $(".tableFloatingHeaderOriginal", this);
        var floatingHeaderRow = $(".tableFloatingHeader", this);

        // Copy cell widths from original header
        $("th", floatingHeaderRow).each(function(index) {
            var cellWidth = $("th", originalHeaderRow).eq(index).css('width');
            $(this).css('width', cellWidth);
        });

        // Copy row width from whole table
        floatingHeaderRow.css("width", Math.max(originalHeaderRow.width(), $(this).width()) + "px");

    });
}


function ActivateFloatingHeaders(selector_str){
    $(selector_str).each(function() {
        $(this).wrap("<div class=\"divtftable\" style=\"position:relative\"></div>");

        // use first row as floating header by default
        var floatingHeaderSelector = "tr:first";
        var explicitFloatingHeaderSelector = "tr.floating-header"
        if ($(explicitFloatingHeaderSelector, this).length){
            floatingHeaderSelector = explicitFloatingHeaderSelector;
        }

        var originalHeaderRow = $(floatingHeaderSelector, this).first();
        var clonedHeaderRow = originalHeaderRow.clone()
        originalHeaderRow.before(clonedHeaderRow);

        clonedHeaderRow.addClass("tableFloatingHeader");
        clonedHeaderRow.css("position", "fixed");
        // not sure why but 0px is used there is still some space in the top
        clonedHeaderRow.css("top", "-2px");
        clonedHeaderRow.css("margin-left", $(this).offset().left);
        clonedHeaderRow.css("visibility", "hidden");

        originalHeaderRow.addClass("tableFloatingHeaderOriginal");
    });
    _UpdateTableHeadersResize();
    _UpdateTableHeadersScroll();
    $(window).scroll(_UpdateTableHeadersScroll);
    $(window).resize(_UpdateTableHeadersResize);
}
