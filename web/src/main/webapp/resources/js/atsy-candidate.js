/**
 * Created by tothd on 2015. 11. 19..
 */
$(document).ready(function () {
    var form = $("#searchCandidate"),
        table = $("#candidates");


    if ("onhashchange" in window) {

        form.submit(function (event) {

            var values = ["name", "email", "phone", "position"];
            var $this = $(this);
            values = values.map(function (selector) {
                return [selector, $this.find("[name=" + selector + "]").val()];
            }).filter(function (value) {
                return value[1].length != 0;
            }).map(function (value) {
                return value[0] + "/" + value[1];
            }).join("/");
            if (values.length > 0) {
                location.hash = values;
            }
            event.preventDefault();
            return false;
        })

        function locationHashChanged() {
            //alert(location.hash);
            var nth = 0;
            result = {};
            var data = location.hash.replace(/["/"]/g, function (match) {
                nth++;
                return (nth % 2 === 1) ? ";" : match;
            }).replace("#", "").split("/").map(function (x) {
                x = x.split(";");
                result[x[0]] = x[1];
            });
            var $this = $("#searchCandidate");
            $.ajax({
                url: $this.attr('action'),
                method: $this.attr('method'),
                contentType: 'application/json',
                data: JSON.stringify(result)
            });
        }

        window.onhashchange = locationHashChanged;

        form.refresh;
        table.bootstrapTable('refresh');
    }

});

function actionFormatter(value, row, index) {
    return [
        '<a class="edit ml10" href="candidate/'+row.candidateId+'" title="Edit">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}
